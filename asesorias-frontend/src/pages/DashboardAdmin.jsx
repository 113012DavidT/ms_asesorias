import { useEffect, useState } from 'react';
import { Layout } from '../components/Layout';
import { Card } from '../components/Card';
import { Table } from '../components/Table';
import { Modal } from '../components/Modal';
import { useFetch } from '../hooks/useFetch';
import Swal from 'sweetalert2';
import { FiUsers, FiMapPin, FiBook, FiSettings } from 'react-icons/fi';
import './Dashboard.css';

export default function DashboardAdmin() {
  const [activeTab, setActiveTab] = useState('usuarios');
  const [showModal, setShowModal] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // Hooks para cada recurso
  const usuariosAPI = useFetch('/api/admin/usuarios');
  const rolesAPI = useFetch('/api/admin/roles');
  const divisionesAPI = useFetch('/api/divisiones');
  const programasAPI = useFetch('/api/programas');

  // State para formularios
  const [formData, setFormData] = useState({
    usuarios: {},
    divisiones: {},
    programas: {}
  });

  useEffect(() => {
    cargarDatos();
    cargarRoles();
    cargarDivisiones(); // Cargar divisiones siempre para el formulario de programas
  }, [activeTab]);

  const cargarRoles = async () => {
    try {
      await rolesAPI.get();
    } catch (error) {
      console.error('Error al cargar roles:', error);
    }
  };

  const cargarDivisiones = async () => {
    try {
      await divisionesAPI.get();
    } catch (error) {
      console.error('Error al cargar divisiones:', error);
    }
  };

  const cargarDatos = async () => {
    try {
      if (activeTab === 'usuarios') {
        await usuariosAPI.get();
      } else if (activeTab === 'divisiones') {
        await divisionesAPI.get();
      } else if (activeTab === 'programas') {
        await programasAPI.get();
      }
    } catch (error) {
      Swal.fire('Error', 'No se pudieron cargar los datos', 'error');
    }
  };

  // === USUARIOS ===
  const handleGuardarUsuario = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await usuariosAPI.put(editingId, formData.usuarios);
        Swal.fire('Éxito', 'Usuario actualizado', 'success');
      } else {
        await usuariosAPI.post(formData.usuarios);
        Swal.fire('Éxito', 'Usuario creado', 'success');
      }
      setShowModal(false);
      setFormData({ ...formData, usuarios: {} });
      setEditingId(null);
      await usuariosAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar el usuario', 'error');
    }
  };

  const handleEditarUsuario = (usuario) => {
    setFormData({ ...formData, usuarios: { ...usuario } });
    setEditingId(usuario.id);
    setShowModal(true);
  };

  const handleEliminarUsuario = async (id) => {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await usuariosAPI.delete(id);
          Swal.fire('Éxito', 'Usuario eliminado', 'success');
          await usuariosAPI.get();
        } catch (error) {
          Swal.fire('Error', 'No se pudo eliminar el usuario', 'error');
        }
      }
    });
  };

  // === DIVISIONES ===
  const handleGuardarDivision = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await divisionesAPI.put(editingId, formData.divisiones);
      } else {
        await divisionesAPI.post(formData.divisiones);
      }
      Swal.fire('Éxito', 'División guardada', 'success');
      setShowModal(false);
      setFormData({ ...formData, divisiones: {} });
      setEditingId(null);
      await divisionesAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar la división', 'error');
    }
  };

  // === PROGRAMAS ===
  const handleGuardarPrograma = async (e) => {
    e.preventDefault();
    try {
      // Preparar datos con la estructura correcta
      const datosPrograma = {
        nombre: formData.programas.nombre,
        descripcion: formData.programas.descripcion,
        division: {
          id: parseInt(formData.programas.divisionId)
        }
      };

      if (editingId) {
        await programasAPI.put(editingId, datosPrograma);
      } else {
        await programasAPI.post(datosPrograma);
      }
      Swal.fire('Éxito', 'Programa guardado', 'success');
      setShowModal(false);
      setFormData({ ...formData, programas: {} });
      setEditingId(null);
      await programasAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar el programa', 'error');
    }
  };

  const abrirNuevoUsuario = () => {
    setFormData({ ...formData, usuarios: {} });
    setEditingId(null);
    setShowModal(true);
  };

  const abrirNuevaDivision = () => {
    setFormData({ ...formData, divisiones: {} });
    setEditingId(null);
    setShowModal(true);
  };

  const abrirNuevoPrograma = () => {
    setFormData({ ...formData, programas: {} });
    setEditingId(null);
    setShowModal(true);
  };

  const menuItems = [
    {
      id: 'usuarios',
      label: 'Usuarios',
      icon: <FiUsers />,
      active: activeTab === 'usuarios',
      onClick: () => setActiveTab('usuarios')
    },
    {
      id: 'divisiones',
      label: 'Divisiones',
      icon: <FiMapPin />,
      active: activeTab === 'divisiones',
      onClick: () => setActiveTab('divisiones')
    },
    {
      id: 'programas',
      label: 'Programas',
      icon: <FiBook />,
      active: activeTab === 'programas',
      onClick: () => setActiveTab('programas')
    }
  ];

  return (
    <Layout menuItems={menuItems}>
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h2>Administración del Sistema</h2>
          <button
            className="btn btn-primary"
            onClick={() => {
              if (activeTab === 'usuarios') abrirNuevoUsuario();
              else if (activeTab === 'divisiones') abrirNuevaDivision();
              else if (activeTab === 'programas') abrirNuevoPrograma();
            }}
          >
            + Agregar
          </button>
        </div>

        {/* USUARIOS */}
        {activeTab === 'usuarios' && (
          <Card title="Gestión de Usuarios">
            <Table
              headers={['correoMatricula', 'nombre', 'apellido', 'rolNombre', 'activo']}
              rows={usuariosAPI.data || []}
              loading={usuariosAPI.loading}
              actions={(usuario) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => handleEditarUsuario(usuario)}
                  >
                    Editar
                  </button>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => handleEliminarUsuario(usuario.id)}
                  >
                    Eliminar
                  </button>
                </div>
              )}
              emptyMessage="No hay usuarios registrados"
            />
          </Card>
        )}

        {/* DIVISIONES */}
        {activeTab === 'divisiones' && (
          <Card title="Gestión de Divisiones">
            <Table
              headers={['nombre', 'descripcion', 'activo']}
              rows={divisionesAPI.data || []}
              loading={divisionesAPI.loading}
              actions={(division) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => {
                      setFormData({ ...formData, divisiones: { ...division } });
                      setEditingId(division.id);
                      setShowModal(true);
                    }}
                  >
                    Editar
                  </button>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => {
                      Swal.fire({
                        title: '¿Estás seguro?',
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonText: 'Sí, eliminar'
                      }).then(async (result) => {
                        if (result.isConfirmed) {
                          try {
                            await divisionesAPI.delete(division.id);
                            Swal.fire('Éxito', 'División eliminada', 'success');
                            await divisionesAPI.get();
                          } catch (error) {
                            Swal.fire('Error', 'No se pudo eliminar', 'error');
                          }
                        }
                      });
                    }}
                  >
                    Eliminar
                  </button>
                </div>
              )}
              emptyMessage="No hay divisiones registradas"
            />
          </Card>
        )}

        {/* PROGRAMAS */}
        {activeTab === 'programas' && (
          <Card title="Gestión de Programas">
            <Table
              headers={['nombre', 'descripcion', 'activo']}
              rows={programasAPI.data || []}
              loading={programasAPI.loading}
              actions={(programa) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => {
                      setFormData({ ...formData, programas: { ...programa } });
                      setEditingId(programa.id);
                      setShowModal(true);
                    }}
                  >
                    Editar
                  </button>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => {
                      Swal.fire({
                        title: '¿Estás seguro?',
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonText: 'Sí, eliminar'
                      }).then(async (result) => {
                        if (result.isConfirmed) {
                          try {
                            await programasAPI.delete(programa.id);
                            Swal.fire('Éxito', 'Programa eliminado', 'success');
                            await programasAPI.get();
                          } catch (error) {
                            Swal.fire('Error', 'No se pudo eliminar', 'error');
                          }
                        }
                      });
                    }}
                  >
                    Eliminar
                  </button>
                </div>
              )}
              emptyMessage="No hay programas registrados"
            />
          </Card>
        )}

        {/* MODALES */}
        {activeTab === 'usuarios' && (
          <Modal
            isOpen={showModal}
            title={editingId ? 'Editar Usuario' : 'Nuevo Usuario'}
            onClose={() => {
              setShowModal(false);
              setEditingId(null);
            }}
            size="md"
          >
            <form onSubmit={handleGuardarUsuario}>
              <div className="mb-3">
                <label className="form-label">Correo/Matrícula</label>
                <input
                  type="email"
                  className="form-control"
                  value={formData.usuarios.correoMatricula || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      usuarios: { ...formData.usuarios, correoMatricula: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Nombre</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.usuarios.nombre || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      usuarios: { ...formData.usuarios, nombre: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Apellido</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.usuarios.apellido || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      usuarios: { ...formData.usuarios, apellido: e.target.value }
                    })
                  }
                  required
                />
              </div>
              {!editingId && (
                <div className="mb-3">
                  <label className="form-label">Contraseña</label>
                  <input
                    type="password"
                    className="form-control"
                    value={formData.usuarios.password || ''}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        usuarios: { ...formData.usuarios, password: e.target.value }
                      })
                    }
                    required
                  />
                </div>
              )}
              <div className="mb-3">
                <label className="form-label">Rol</label>
                <select
                  className="form-control"
                  value={formData.usuarios.rolId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      usuarios: { ...formData.usuarios, rolId: parseInt(e.target.value) }
                    })
                  }
                  required
                >
                  <option value="">Selecciona un rol</option>
                  {(rolesAPI.data || []).map((rol) => (
                    <option key={rol.id} value={rol.id}>
                      {rol.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mb-3 form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  id="activo"
                  checked={formData.usuarios.activo || false}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      usuarios: { ...formData.usuarios, activo: e.target.checked }
                    })
                  }
                />
                <label className="form-check-label" htmlFor="activo">
                  Activo
                </label>
              </div>
              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary">
                  Guardar
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => {
                    setShowModal(false);
                    setEditingId(null);
                  }}
                >
                  Cancelar
                </button>
              </div>
            </form>
          </Modal>
        )}

        {activeTab === 'divisiones' && (
          <Modal
            isOpen={showModal}
            title={editingId ? 'Editar División' : 'Nueva División'}
            onClose={() => {
              setShowModal(false);
              setEditingId(null);
            }}
            size="md"
          >
            <form onSubmit={handleGuardarDivision}>
              <div className="mb-3">
                <label className="form-label">Nombre</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.divisiones.nombre || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      divisiones: { ...formData.divisiones, nombre: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Descripción</label>
                <textarea
                  className="form-control"
                  value={formData.divisiones.descripcion || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      divisiones: { ...formData.divisiones, descripcion: e.target.value }
                    })
                  }
                  rows="3"
                ></textarea>
              </div>
              <div className="mb-3 form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  id="divisiones-activo"
                  checked={formData.divisiones.activo !== false}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      divisiones: { ...formData.divisiones, activo: e.target.checked }
                    })
                  }
                />
                <label className="form-check-label" htmlFor="divisiones-activo">
                  Activo
                </label>
              </div>
              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary">
                  Guardar
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => {
                    setShowModal(false);
                    setEditingId(null);
                  }}
                >
                  Cancelar
                </button>
              </div>
            </form>
          </Modal>
        )}

        {activeTab === 'programas' && (
          <Modal
            isOpen={showModal}
            title={editingId ? 'Editar Programa' : 'Nuevo Programa'}
            onClose={() => {
              setShowModal(false);
              setEditingId(null);
            }}
            size="md"
          >
            <form onSubmit={handleGuardarPrograma}>
              <div className="mb-3">
                <label className="form-label">Nombre *</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.programas.nombre || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      programas: { ...formData.programas, nombre: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">División *</label>
                <select
                  className="form-control"
                  value={formData.programas.divisionId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      programas: { ...formData.programas, divisionId: e.target.value }
                    })
                  }
                  required
                >
                  <option value="">Selecciona una división</option>
                  {(divisionesAPI.data || []).map((division) => (
                    <option key={division.id} value={division.id}>
                      {division.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mb-3">
                <label className="form-label">Descripción</label>
                <textarea
                  className="form-control"
                  value={formData.programas.descripcion || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      programas: { ...formData.programas, descripcion: e.target.value }
                    })
                  }
                  rows="3"
                ></textarea>
              </div>
              <div className="mb-3 form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  id="programas-activo"
                  checked={formData.programas.activo !== false}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      programas: { ...formData.programas, activo: e.target.checked }
                    })
                  }
                />
                <label className="form-check-label" htmlFor="programas-activo">
                  Activo
                </label>
              </div>
              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary">
                  Guardar
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => {
                    setShowModal(false);
                    setEditingId(null);
                  }}
                >
                  Cancelar
                </button>
              </div>
            </form>
          </Modal>
        )}
      </div>
    </Layout>
  );
}