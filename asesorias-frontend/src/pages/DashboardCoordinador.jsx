import { useEffect, useMemo, useState } from 'react';
import { Layout } from '../components/Layout';
import { Card } from '../components/Card';
import { Table } from '../components/Table';
import { Modal } from '../components/Modal';
import { useFetch } from '../hooks/useFetch';
import { useAuth } from '../hooks/useAuth';
import Swal from 'sweetalert2';
import { FiUsers, FiBook, FiEdit2 } from 'react-icons/fi';
import './Dashboard.css';
import axios from '../api/axiosConfig';
import { AssignmentModal } from '../components/AssignmentModal';

export default function DashboardCoordinador() {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('profesores');
  const [showModal, setShowModal] = useState(false);
  const [editingId, setEditingId] = useState(null);

  const profesoresAPI = useFetch('/api/profesores');
  const alumnosAPI = useFetch('/api/alumnos');
  const divisionesAPI = useFetch('/api/divisiones');
  const programasAPI = useFetch('/api/programas');
  const usuariosAPI = useFetch('/api/admin/usuarios');

  const [formData, setFormData] = useState({
    profesor: {},
    alumno: {}
  });

  // Estado para asignaciones
  const [showAssign, setShowAssign] = useState(false);
  const [loadingAssign, setLoadingAssign] = useState(false);

  useEffect(() => {
    cargarDatos();
  }, [activeTab]);

  const cargarDatos = async () => {
    try {
      if (activeTab === 'profesores') {
        await profesoresAPI.get();
      } else if (activeTab === 'alumnos') {
        await alumnosAPI.get();
      }
      // Datos base para asignaciones (lazy cuando se abre el modal)
    } catch (error) {
      Swal.fire('Error', 'No se pudieron cargar los datos', 'error');
    }
  };

  const openAssignModal = async () => {
    try {
      setLoadingAssign(true);
      await Promise.all([
        divisionesAPI.get(),
        programasAPI.get(),
        usuariosAPI.get()
      ]);
      setShowAssign(true);
    } finally {
      setLoadingAssign(false);
    }
  };

  const doAsignarProfesores = async ({ divisionId, programaId, usuarioIds }) => {
    try {
      await axios.post('/api/profesores/asignar', { divisionId, programaId, usuarioIds });
      Swal.fire('Éxito', 'Profesores asignados correctamente', 'success');
      await profesoresAPI.get();
    } catch (e) {
      Swal.fire('Error', e?.response?.data?.message || 'No se pudieron asignar profesores', 'error');
      throw e;
    }
  };

  const doAsignarAlumnos = async ({ divisionId, programaId, usuarioIds }) => {
    try {
      await axios.post('/api/alumnos/asignar', { divisionId, programaId, usuarioIds });
      Swal.fire('Éxito', 'Alumnos asignados correctamente', 'success');
      await alumnosAPI.get();
    } catch (e) {
      Swal.fire('Error', e?.response?.data?.message || 'No se pudieron asignar alumnos', 'error');
      throw e;
    }
  };

  const handleGuardarProfesor = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await profesoresAPI.put(editingId, formData.profesor);
      } else {
        await profesoresAPI.post(formData.profesor);
      }
      Swal.fire('Éxito', 'Profesor guardado', 'success');
      setShowModal(false);
      setFormData({ ...formData, profesor: {} });
      setEditingId(null);
      await profesoresAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar el profesor', 'error');
    }
  };

  const handleGuardarAlumno = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await alumnosAPI.put(editingId, formData.alumno);
      } else {
        await alumnosAPI.post(formData.alumno);
      }
      Swal.fire('Éxito', 'Alumno guardado', 'success');
      setShowModal(false);
      setFormData({ ...formData, alumno: {} });
      setEditingId(null);
      await alumnosAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar el alumno', 'error');
    }
  };

  const menuItems = [
    {
      id: 'profesores',
      label: 'Profesores',
      icon: <FiBook />,
      active: activeTab === 'profesores',
      onClick: () => setActiveTab('profesores')
    },
    {
      id: 'alumnos',
      label: 'Alumnos',
      icon: <FiUsers />,
      active: activeTab === 'alumnos',
      onClick: () => setActiveTab('alumnos')
    }
  ];

  return (
    <Layout menuItems={menuItems}>
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h2>Coordinación Académica</h2>
          <button
            className="btn btn-primary"
            onClick={() => {
              if (activeTab === 'profesores') {
                setFormData({ ...formData, profesor: {} });
              } else {
                setFormData({ ...formData, alumno: {} });
              }
              setEditingId(null);
              setShowModal(true);
            }}
          >
            + Agregar
          </button>
          <button
            className="btn btn-outline-success ms-2"
            onClick={openAssignModal}
            title={activeTab === 'profesores' ? 'Asignar profesores a división/programa' : 'Asignar alumnos a división/programa'}
            disabled={loadingAssign}
          >
            {loadingAssign ? 'Cargando...' : (activeTab === 'profesores' ? 'Asignar Profesores' : 'Asignar Alumnos')}
          </button>
        </div>
        {/* MODAL DE ASIGNACIÓN (UX mejorado) */}
        <AssignmentModal
          isOpen={showAssign}
          onClose={() => setShowAssign(false)}
          divisiones={divisionesAPI.data || []}
          programas={programasAPI.data || []}
          usuarios={usuariosAPI.data || []}
          loading={divisionesAPI.loading || programasAPI.loading || usuariosAPI.loading}
          onAsignarProfesores={doAsignarProfesores}
          onAsignarAlumnos={doAsignarAlumnos}
          mode={activeTab === 'profesores' ? 'PROFESORES' : 'ALUMNOS'}
          assignedProfesorUsuarioIds={(profesoresAPI.data || []).map(p => p.usuarioId)}
          assignedAlumnoUsuarioIds={(alumnosAPI.data || []).map(a => a.usuarioId)}
        />
        {/* PROFESORES */}
        {activeTab === 'profesores' && (
          <Card title="Gestión de Profesores">
            <Table
              headers={['nombre', 'apellido', 'especialidad', 'telefonoContacto', 'activo']}
              rows={profesoresAPI.data || []}
              loading={profesoresAPI.loading}
              actions={(profesor) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => {
                      setFormData({ ...formData, profesor: { ...profesor } });
                      setEditingId(profesor.id);
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
                            await profesoresAPI.delete(profesor.id);
                            Swal.fire('Éxito', 'Profesor eliminado', 'success');
                            await profesoresAPI.get();
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
              emptyMessage="No hay profesores registrados"
            />
          </Card>
        )}

        {/* ALUMNOS */}
        {activeTab === 'alumnos' && (
          <Card title="Gestión de Alumnos">
            <Table
              headers={['nombre', 'apellido', 'matricula', 'promedio', 'estado', 'activo']}
              rows={alumnosAPI.data || []}
              loading={alumnosAPI.loading}
              actions={(alumno) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-info btn-sm"
                    onClick={() => {
                      setFormData({ ...formData, alumno: { ...alumno } });
                      setEditingId(alumno.id);
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
                            await alumnosAPI.delete(alumno.id);
                            Swal.fire('Éxito', 'Alumno eliminado', 'success');
                            await alumnosAPI.get();
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
              emptyMessage="No hay alumnos registrados"
            />
          </Card>
        )}

        {/* MODAL PROFESOR */}
        {activeTab === 'profesores' && (
          <Modal
            isOpen={showModal}
            title={editingId ? 'Editar Profesor' : 'Nuevo Profesor'}
            onClose={() => {
              setShowModal(false);
              setEditingId(null);
            }}
            size="md"
          >
            <form onSubmit={handleGuardarProfesor}>
              <div className="mb-3">
                <label className="form-label">Usuario ID</label>
                <input
                  type="number"
                  className="form-control"
                  value={formData.profesor.usuarioId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      profesor: { ...formData.profesor, usuarioId: parseInt(e.target.value) }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Especialidad</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.profesor.especialidad || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      profesor: { ...formData.profesor, especialidad: e.target.value }
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Teléfono</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.profesor.telefonoContacto || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      profesor: { ...formData.profesor, telefonoContacto: e.target.value }
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Correo Alternativo</label>
                <input
                  type="email"
                  className="form-control"
                  value={formData.profesor.correoAlternativo || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      profesor: { ...formData.profesor, correoAlternativo: e.target.value }
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

        {/* MODAL ALUMNO */}
        {activeTab === 'alumnos' && (
          <Modal
            isOpen={showModal}
            title={editingId ? 'Editar Alumno' : 'Nuevo Alumno'}
            onClose={() => {
              setShowModal(false);
              setEditingId(null);
            }}
            size="md"
          >
            <form onSubmit={handleGuardarAlumno}>
              <div className="mb-3">
                <label className="form-label">Usuario ID</label>
                <input
                  type="number"
                  className="form-control"
                  value={formData.alumno.usuarioId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      alumno: { ...formData.alumno, usuarioId: parseInt(e.target.value) }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Matrícula</label>
                <input
                  type="text"
                  className="form-control"
                  value={formData.alumno.matricula || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      alumno: { ...formData.alumno, matricula: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Promedio</label>
                <input
                  type="number"
                  step="0.1"
                  className="form-control"
                  value={formData.alumno.promedio || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      alumno: { ...formData.alumno, promedio: parseFloat(e.target.value) }
                    })
                  }
                />
              </div>
              <div className="mb-3">
                <label className="form-label">Estado</label>
                <select
                  className="form-control"
                  value={formData.alumno.estado || 'ACTIVO'}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      alumno: { ...formData.alumno, estado: e.target.value }
                    })
                  }
                >
                  <option value="ACTIVO">Activo</option>
                  <option value="INACTIVO">Inactivo</option>
                  <option value="SUSPENDIDO">Suspendido</option>
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