import { useEffect, useState } from 'react';
import { Layout } from '../components/Layout';
import { Card } from '../components/Card';
import { Table } from '../components/Table';
import { Modal } from '../components/Modal';
import { useFetch } from '../hooks/useFetch';
import { useAuth } from '../hooks/useAuth';
import Swal from 'sweetalert2';
import { FiCalendar, FiBook, FiUsers } from 'react-icons/fi';
import './Dashboard.css';

export default function DashboardAlumno() {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('disponibles');
  const [showModal, setShowModal] = useState(false);
  const [selectedProfesor, setSelectedProfesor] = useState(null);

  const asesoriasAPI = useFetch('/api/asesorias');
  const asesoriasProfesorAPI = useFetch('/api/asesorias/profesor');
  const profesoresAPI = useFetch('/api/profesores');

  const [formData, setFormData] = useState({
    solicitud: {}
  });

  useEffect(() => {
    cargarDatos();
  }, [activeTab]);

  const cargarDatos = async () => {
    try {
      if (activeTab === 'disponibles') {
        await profesoresAPI.get();
      } else {
        if (user?.usuarioId) {
          await asesoriasAPI.get(`/alumno/${user.usuarioId}`);
        }
      }
    } catch (error) {
      Swal.fire('Error', 'No se pudieron cargar los datos', 'error');
    }
  };

  const handleSolicitarAsesoria = async (e) => {
    e.preventDefault();
    try {
      await asesoriasProfesorAPI.post({
        profesorId: selectedProfesor?.usuarioId,
        alumnoId: user?.usuarioId,
        fecha: formData.solicitud?.fecha,
        horaInicio: formData.solicitud?.horaInicio,
        horaFin: formData.solicitud?.horaFin,
        titulo: formData.solicitud?.titulo,
        materia: formData.solicitud?.titulo,
        observaciones: formData.solicitud?.descripcion
      });
      Swal.fire('Éxito', 'Solicitud de asesoría enviada', 'success');
      setShowModal(false);
      setFormData({ solicitud: {} });
      // Refrescar historial del alumno explícitamente
      if (user?.usuarioId) {
        await asesoriasAPI.get(`/alumno/${user.usuarioId}`);
      }
    } catch (error) {
      const msg = error?.response?.data?.error || 'No se pudo enviar la solicitud';
      Swal.fire('Error', msg, 'error');
    }
  };

  const menuItems = [
    {
      id: 'disponibles',
      label: 'Solicitar Asesoría',
      icon: <FiUsers />,
      active: activeTab === 'disponibles',
      onClick: () => setActiveTab('disponibles')
    },
    {
      id: 'historial',
      label: 'Mis Asesorías',
      icon: <FiCalendar />,
      active: activeTab === 'historial',
      onClick: () => setActiveTab('historial')
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
          <h2>Mi Portal de Asesorías</h2>
        </div>

        {/* PROFESORES DISPONIBLES */}
        {activeTab === 'disponibles' && (
          <>
            {profesoresAPI.loading && (
              <div className="text-center text-muted p-4">Cargando profesores…</div>
            )}
            {!profesoresAPI.loading && Array.isArray(profesoresAPI.data) && profesoresAPI.data.length === 0 && (
              <Card title="No hay profesores disponibles">
                <p className="mb-2">Aún no hay profesores asignados a tu programa.</p>
                <div className="d-flex gap-2">
                  <button className="btn btn-outline-primary btn-sm" onClick={() => profesoresAPI.get()}>
                    Recargar
                  </button>
                </div>
              </Card>
            )}
            <div className="dashboard-grid">
              {(profesoresAPI.data || []).map((profesor) => (
                <Card
                  key={profesor.id}
                  title={`${profesor.nombre || ''}`}
                  className="clickable"
                >
                  <div className="profesor-card-content">
                    <div className="row g-2 mb-2">
                      <div className="col-6">
                        <small className="text-muted">Especialidad</small>
                        <div>{profesor.especialidad || 'No especificada'}</div>
                      </div>
                      <div className="col-6">
                        <small className="text-muted">Email</small>
                        <div>{profesor.correoMatricula || 'No disponible'}</div>
                      </div>
                    </div>
                    <p className="mb-0"><small className="text-muted">Selecciona para solicitar una asesoría</small></p>
                    <button
                      className="btn btn-primary btn-sm w-100 mt-3"
                      onClick={() => {
                        setSelectedProfesor(profesor);
                        setShowModal(true);
                      }}
                    >
                      Solicitar Asesoría
                    </button>
                  </div>
                </Card>
              ))}
            </div>
          </>
        )}

        {/* MIS ASESORÍAS */}
        {activeTab === 'historial' && (
          <Card title="Historial de Asesorías">
            <Table
              headers={['titulo', 'fecha', 'horaInicio', 'horaFin', 'estado']}
              rows={(asesoriasAPI.data || []).map((a) => ({
                ...a,
                titulo: a.titulo || a.materia || '',
                horaInicio: a.horaInicio || a.hora || '',
                horaFin: a.horaFin || '',
                estado: (
                  <span className={`badge bg-${getEstadoBadge(a.estatus)}`}>
                    {a.estatus}
                  </span>
                )
              }))}
              loading={asesoriasAPI.loading}
              emptyMessage="No tienes asesorías registradas"
            />
          </Card>
        )}

        {/* MODAL SOLICITUD */}
        <Modal
          isOpen={showModal && selectedProfesor}
          title={`Solicitar Asesoría con ${selectedProfesor?.nombre}`}
          onClose={() => {
            setShowModal(false);
            setSelectedProfesor(null);
            setFormData({ solicitud: {} });
          }}
          size="md"
        >
          <form onSubmit={handleSolicitarAsesoria}>
            <div className="mb-3">
              <label className="form-label">Título de la Asesoría</label>
              <input
                type="text"
                className="form-control"
                value={formData.solicitud.titulo || ''}
                onChange={(e) =>
                  setFormData({
                    solicitud: { ...formData.solicitud, titulo: e.target.value }
                  })
                }
                placeholder="Ej: Ayuda con Cálculo II"
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Descripción</label>
              <textarea
                className="form-control"
                value={formData.solicitud.descripcion || ''}
                onChange={(e) =>
                  setFormData({
                    solicitud: { ...formData.solicitud, descripcion: e.target.value }
                  })
                }
                placeholder="Describe qué necesitas ayuda..."
                rows="4"
                required
              ></textarea>
            </div>
            <div className="row g-2 mb-3">
              <div className="col-6">
                <label className="form-label">Fecha Preferida</label>
                <input
                  type="date"
                  className="form-control"
                  value={formData.solicitud.fecha || ''}
                  onChange={(e) =>
                    setFormData({
                      solicitud: { ...formData.solicitud, fecha: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="col-3">
                <label className="form-label">Hora Inicio</label>
                <input
                  type="time"
                  className="form-control"
                  value={formData.solicitud.horaInicio || ''}
                  onChange={(e) =>
                    setFormData({
                      solicitud: { ...formData.solicitud, horaInicio: e.target.value }
                    })
                  }
                  required
                />
              </div>
              <div className="col-3">
                <label className="form-label">Hora Fin</label>
                <input
                  type="time"
                  className="form-control"
                  value={formData.solicitud.horaFin || ''}
                  onChange={(e) =>
                    setFormData({
                      solicitud: { ...formData.solicitud, horaFin: e.target.value }
                    })
                  }
                  required
                />
              </div>
            </div>
            <div className="alert alert-info" role="alert">
              <small>Tu solicitud será revisada por el profesor. Te notificaremos cuando sea aceptada.</small>
            </div>
            <div className="d-flex gap-2">
              <button type="submit" className="btn btn-primary">
                Enviar Solicitud
              </button>
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => {
                  setShowModal(false);
                  setSelectedProfesor(null);
                  setFormData({ solicitud: {} });
                }}
              >
                Cancelar
              </button>
            </div>
          </form>
        </Modal>
      </div>
    </Layout>
  );
}

// CSS adicional para las tarjetas de profesores
const styles = `
  .profesor-card-content {
    padding: 10px 0;
  }

  .profesor-card-content p {
    margin-bottom: 8px;
    font-size: 14px;
    color: #666;
  }

  .profesor-card-content strong {
    color: #333;
  }
`;