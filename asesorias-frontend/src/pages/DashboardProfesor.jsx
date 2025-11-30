import { useEffect, useState } from 'react';
import { Layout } from '../components/Layout';
import { Card } from '../components/Card';
import { Table } from '../components/Table';
import { Modal } from '../components/Modal';
import { useFetch } from '../hooks/useFetch';
import { useAuth } from '../hooks/useAuth';
import Swal from 'sweetalert2';
import { FiBook, FiUsers, FiCalendar } from 'react-icons/fi';
import './Dashboard.css';

export default function DashboardProfesor() {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('grupos');
  const [showModal, setShowModal] = useState(false);
  const [editingId, setEditingId] = useState(null);

  const gruposAPI = useFetch('/api/grupos');
  const asesoriasAPI = useFetch('/api/asesorias');
  const alumnosAPI = useFetch('/api/alumnos');

  const [formData, setFormData] = useState({
    grupo: {},
    asesoria: {}
  });

  useEffect(() => {
    cargarDatos();
  }, [activeTab]);

  const cargarDatos = async () => {
    try {
      if (activeTab === 'grupos') {
        await gruposAPI.get();
      } else if (activeTab === 'asesorias') {
        await asesoriasAPI.get();
      } else if (activeTab === 'alumnos') {
        await alumnosAPI.get();
      }
    } catch (error) {
      Swal.fire('Error', 'No se pudieron cargar los datos', 'error');
    }
  };

  const handleGuardarGrupo = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await gruposAPI.put(editingId, formData.grupo);
      } else {
        await gruposAPI.post(formData.grupo);
      }
      Swal.fire('Éxito', 'Grupo guardado', 'success');
      setShowModal(false);
      setFormData({ ...formData, grupo: {} });
      setEditingId(null);
      await gruposAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar el grupo', 'error');
    }
  };

  const handleGuardarAsesoria = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await asesoriasAPI.put(editingId, formData.asesoria);
      } else {
        await asesoriasAPI.post(formData.asesoria);
      }
      Swal.fire('Éxito', 'Asesoría guardada', 'success');
      setShowModal(false);
      setFormData({ ...formData, asesoria: {} });
      setEditingId(null);
      await asesoriasAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar la asesoría', 'error');
    }
  };

  const menuItems = [
    {
      id: 'grupos',
      label: 'Mis Grupos',
      icon: <FiBook />,
      active: activeTab === 'grupos',
      onClick: () => setActiveTab('grupos')
    },
    {
      id: 'asesorias',
      label: 'Asesorías',
      icon: <FiCalendar />,
      active: activeTab === 'asesorias',
      onClick: () => setActiveTab('asesorias')
    },
    {
      id: 'alumnos',
      label: 'Mis Alumnos',
      icon: <FiUsers />,
      active: activeTab === 'alumnos',
      onClick: () => setActiveTab('alumnos')
    }
  ];

  const getEstadoBadge = (estado) => {
    const badges = {
      'PROGRAMADA': 'warning',
      'COMPLETADA': 'success',
      'CANCELADA': 'danger',
      'PENDIENTE': 'info'
    };
    return badges[estado] || 'secondary';
  };

  return (
    <Layout menuItems={menuItems}>
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h2>Panel del Profesor</h2>
          <button
            className="btn btn-primary"
            onClick={() => {
              if (activeTab === 'grupos') {
                setFormData({ ...formData, grupo: { profesorId: user?.id } });
              } else {
                setFormData({ ...formData, asesoria: { profesorId: user?.id } });
              }
              setEditingId(null);
              setShowModal(true);
            }}
          >
            + Agregar
          </button>
        </div>

        {/* GRUPOS */}
        {activeTab === 'grupos' && (
          <Card title="Mis Grupos de Tutoría">
            <Table
              headers={['nombre', 'descripcion', 'activo']}
              rows={gruposAPI.data || []}
              loading={gruposAPI.loading}
              actions={(grupo) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => {
                      setFormData({ ...formData, grupo: { ...grupo } });
                      setEditingId(grupo.id);
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
                            await gruposAPI.delete(grupo.id);
                            Swal.fire('Éxito', 'Grupo eliminado', 'success');
                            await gruposAPI.get();
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
              emptyMessage="No tienes grupos creados"
            />
          </Card>
        )}

        {/* ASESORÍAS */}
        {activeTab === 'asesorias' && (
          <Card title="Asesorías Programadas">
            <Table
              headers={['titulo', 'fecha', 'horaInicio', 'horaFin', 'estado']}
              rows={asesoriasAPI.data || []}
              loading={asesoriasAPI.loading}
              actions={(asesoria) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => {
                      setFormData({ ...formData, asesoria: { ...asesoria } });
                      setEditingId(asesoria.id);
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
                            await asesoriasAPI.delete(asesoria.id);
                            Swal.fire('Éxito', 'Asesoría eliminada', 'success');
                            await asesoriasAPI.get();
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
              emptyMessage="No hay asesorías programadas"
            />
          </Card>
        )}

        {/* ALUMNOS */}
        {activeTab === 'alumnos' && (
          <Card title="Alumnos en Mis Grupos">
            <Table
              headers={['nombre', 'apellido', 'matricula', 'promedio', 'estado']}
              rows={alumnosAPI.data || []}
              loading={alumnosAPI.loading}
              emptyMessage="No hay alumnos asignados"
            />
          </Card>
        )}

        {/* MODAL GRUPO */}
        {activeTab === 'grupos' && (
          <Modal
            isOpen={showModal}
            title={editingId ? 'Editar Grupo' : 'Nuevo Grupo'}
            onClose={() => {
              setShowModal(false);
              setEditingId(null);
            }}
            size="md"
          >
            <form onSubmit={handleGuardarGrupo}>
              <div className="mb-3">
                <label className="form-label">Nombre del Grupo</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.grupo.nombre || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      grupo: { ...formData.grupo, nombre: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Descripción</label>
                <textarea
                  className="form-control"
                  value={formData.grupo.descripcion || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      grupo: { ...formData.grupo, descripcion: e.target.value }
                    })
                  }
                  rows="3"
                ></textarea>
              </div>
              <div className="mb-3">
                <label className="form-label">División</label>
                <input
                  type="number"
                  className="form-control"
                  value={formData.grupo.divisionId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      grupo: { ...formData.grupo, divisionId: parseInt(e.target.value) }
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Programa</label>
                <input
                  type="number"
                  className="form-control"
                  value={formData.grupo.programaId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      grupo: { ...formData.grupo, programaId: parseInt(e.target.value) }
                    })
                  }
                />
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

        {/* MODAL ASESORÍA */}
        {activeTab === 'asesorias' && (
          <Modal
            isOpen={showModal}
            title={editingId ? 'Editar Asesoría' : 'Nueva Asesoría'}
            onClose={() => {
              setShowModal(false);
              setEditingId(null);
            }}
            size="md"
          >
            <form onSubmit={handleGuardarAsesoria}>
              <div className="mb-3">
                <label className="form-label">Título</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.asesoria.titulo || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, titulo: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Descripción</label>
                <textarea
                  className="form-control"
                  value={formData.asesoria.descripcion || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, descripcion: e.target.value }
                    })
                  }
                  rows="3"
                ></textarea>
              </div>
              <div className="mb-3">
                <label className="form-label">Alumno ID</label>
                <input
                  type="number"
                  className="form-control"
                  value={formData.asesoria.alumnoId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, alumnoId: parseInt(e.target.value) }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Grupo ID</label>
                <input
                  type="number"
                  className="form-control"
                  value={formData.asesoria.grupoId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, grupoId: parseInt(e.target.value) }
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Fecha</label>
                <input
                  type="date"
                  className="form-control"
                  value={formData.asesoria.fecha || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, fecha: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="row mb-3">
                <div className="col">
                  <label className="form-label">Hora Inicio</label>
                  <input
                    type="time"
                    className="form-control"
                    value={formData.asesoria.horaInicio || ''}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        asesoria: { ...formData.asesoria, horaInicio: e.target.value }
                      })
                    }
                    required
                  />
                </div>
                <div className="col">
                  <label className="form-label">Hora Fin</label>
                  <input
                    type="time"
                    className="form-control"
                    value={formData.asesoria.horaFin || ''}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        asesoria: { ...formData.asesoria, horaFin: e.target.value }
                      })
                    }
                    required
                  />
                </div>
              </div>
              <div className="mb-3">
                <label className="form-label">Estado</label>
                <select
                  className="form-control"
                  value={formData.asesoria.estado || 'PROGRAMADA'}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, estado: e.target.value }
                    })
                  }
                >
                  <option value="PROGRAMADA">Programada</option>
                  <option value="COMPLETADA">Completada</option>
                  <option value="CANCELADA">Cancelada</option>
                </select>
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