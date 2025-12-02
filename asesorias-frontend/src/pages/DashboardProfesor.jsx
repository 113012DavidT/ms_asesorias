import { useEffect, useState } from 'react';
import axios from '../api/axiosConfig';
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
  const solicitudesAPI = useFetch('/api/asesorias/profesor');
  const dispAPI = useFetch('/api/disponibilidades');
  // Usar listado curado del backend: alumnos del profesor
  const alumnosAPI = useFetch('/api/profesores/mis-alumnos');

  const [formData, setFormData] = useState({
    grupo: {},
    asesoria: {}
  });
  const [perfil, setPerfil] = useState(null);
  const [divisionInfo, setDivisionInfo] = useState(null);
  const [programaInfo, setProgramaInfo] = useState(null);
  const [savingGrupo, setSavingGrupo] = useState(false);
  const [savingAsesoria, setSavingAsesoria] = useState(false);

  useEffect(() => {
    cargarDatos();
  }, [activeTab]);

  const cargarDatos = async () => {
    try {
      if (activeTab === 'grupos') {
        await gruposAPI.get();
        // Cargar perfil del profesor para precargar División/Programa
        if (user?.usuarioId) {
          try {
            const resp = await axios.get(`/api/profesores/${user.usuarioId}/perfil`);
            const p = resp.data?.perfil || null;
            setPerfil(p);
            // Cargar nombres de división/programa si hay IDs
            if (p?.division?.id) {
              try { const d = await axios.get(`/api/divisiones/${p.division.id}`); setDivisionInfo(d.data || null); } catch { setDivisionInfo(null); }
            } else { setDivisionInfo(null); }
            if (p?.programa?.id) {
              try { const pr = await axios.get(`/api/programas/${p.programa.id}`); setProgramaInfo(pr.data || null); } catch { setProgramaInfo(null); }
            } else { setProgramaInfo(null); }
          } catch (_) {
            setPerfil(null);
            setDivisionInfo(null);
            setProgramaInfo(null);
          }
        }
      } else if (activeTab === 'asesorias') {
        // Para el modal de asesorías necesitamos las listas (no IDs manuales)
        await Promise.all([
          asesoriasAPI.get(),
          alumnosAPI.get(),
          gruposAPI.get()
        ]);
      } else if (activeTab === 'solicitudes') {
        if (user?.usuarioId) {
          await solicitudesAPI.get(`/${user.usuarioId}?estatus=PENDIENTE`);
          await dispAPI.get(`/profesor/${user.usuarioId}`);
        }
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
      setSavingGrupo(true);
      const payload = { nombre: formData.grupo.nombre, descripcion: formData.grupo.descripcion };
      if (editingId) await gruposAPI.put(editingId, payload);
      else await gruposAPI.post(payload);
      Swal.fire('Éxito', 'Grupo guardado', 'success');
      setShowModal(false);
      setFormData({ ...formData, grupo: {} });
      setEditingId(null);
      await gruposAPI.get();
    } catch (error) {
      Swal.fire('Error', 'No se pudo guardar el grupo', 'error');
    } finally {
      setSavingGrupo(false);
    }
  };

  const handleGuardarAsesoria = async (e) => {
    e.preventDefault();
    try {
      setSavingAsesoria(true);
      if (editingId) {
        await asesoriasAPI.put(editingId, formData.asesoria);
        Swal.fire('Éxito', 'Asesoría actualizada', 'success');
      } else {
        // Crear por profesor: endpoint correcto /api/asesorias/profesor
        if (!user?.usuarioId) throw new Error('Profesor no identificado');
        const payload = {
          profesorId: user.usuarioId,
          alumnoId: formData.asesoria.alumnoId,
          fecha: formData.asesoria.fecha,
          horaInicio: formData.asesoria.horaInicio,
          horaFin: formData.asesoria.horaFin,
          titulo: formData.asesoria.titulo,
          materia: formData.asesoria.titulo,
          observaciones: formData.asesoria.descripcion
        };
        await solicitudesAPI.post(payload);
        Swal.fire('Éxito', 'Asesoría creada', 'success');
      }
      setShowModal(false);
      setFormData({ ...formData, asesoria: {} });
      setEditingId(null);
      // Refrescar solo lo necesario
      await asesoriasAPI.get();
      if (user?.usuarioId) {
        await solicitudesAPI.get(`/${user.usuarioId}?estatus=PENDIENTE`);
      }
    } catch (error) {
      const msg = error?.response?.data?.error || error?.response?.data?.message || error.message || 'No se pudo guardar la asesoría';
      Swal.fire('Error', msg, 'error');
    } finally {
      setSavingAsesoria(false);
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
      id: 'solicitudes',
      label: 'Solicitudes',
      icon: <FiCalendar />,
      active: activeTab === 'solicitudes',
      onClick: () => setActiveTab('solicitudes')
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
                setFormData({ ...formData, grupo: { profesorId: user?.usuarioId } });
              } else {
                setFormData({ ...formData, asesoria: { profesorId: user?.usuarioId } });
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
              rows={(asesoriasAPI.data || []).map((a) => ({
                ...a,
                titulo: a.titulo || a.materia || '',
                horaInicio: a.horaInicio || a.hora || '',
                horaFin: a.horaFin || '',
                estado: a.estatus
              }))}
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

        {/* SOLICITUDES PENDIENTES */}
        {activeTab === 'solicitudes' && (
          <Card title="Solicitudes Pendientes">
            <Table
              headers={['titulo', 'fecha', 'horaInicio', 'horaFin', 'estado']}
              rows={(solicitudesAPI.data || []).map((a) => ({
                ...a,
                titulo: a.titulo || a.materia || '',
                horaInicio: a.horaInicio || a.hora || '',
                horaFin: a.horaFin || '',
                estado: a.estatus
              }))}
              loading={solicitudesAPI.loading}
              actions={(a) => (
                <div className="btn-group btn-group-sm">
                  <button
                    className="btn btn-success btn-sm"
                    onClick={async () => {
                      // Elegir disponibilidad si existe; si no, solo marcar PROGRAMADA usando la primera disponible
                      try {
                        if (!user?.usuarioId) throw new Error('Profesor no identificado');
                        if (!Array.isArray(dispAPI.data) || dispAPI.data.length === 0) {
                          await axios.put(`/api/asesorias/${a.id}/estatus/PROGRAMADA`);
                        } else {
                          const libres = (dispAPI.data || []).filter(d => d.disponible);
                          if (libres.length === 0) {
                            await axios.put(`/api/asesorias/${a.id}/estatus/PROGRAMADA`);
                          } else {
                            await axios.put(`/api/asesorias/${a.id}/asignar/${libres[0].id}`);
                          }
                        }
                        Swal.fire('Éxito', 'Solicitud aceptada', 'success');
                        // Refrescar solo lo necesario
                        await solicitudesAPI.get(`/${user.usuarioId}?estatus=PENDIENTE`);
                        await asesoriasAPI.get();
                        await dispAPI.get(`/profesor/${user.usuarioId}`);
                      } catch (e) {
                        Swal.fire('Error', 'No se pudo aceptar la solicitud', 'error');
                      }
                    }}
                  >Aceptar</button>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={async () => {
                      try {
                        await axios.put(`/api/asesorias/${a.id}/estatus/CANCELADA`);
                        Swal.fire('Hecho', 'Solicitud rechazada', 'success');
                        await solicitudesAPI.get();
                      } catch (e) {
                        Swal.fire('Error', 'No se pudo rechazar', 'error');
                      }
                    }}
                  >Rechazar</button>
                </div>
              )}
              emptyMessage="No hay solicitudes pendientes"
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
            footer={(
              <>
                <button type="submit" form="form-grupo" className="btn btn-primary" disabled={savingGrupo}>
                  {savingGrupo ? 'Guardando...' : 'Guardar'}
                </button>
                <button type="button" className="btn btn-secondary" onClick={() => { setShowModal(false); setEditingId(null); }}>Cancelar</button>
              </>
            )}
          >
            <form id="form-grupo" onSubmit={handleGuardarGrupo}>
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
              <div className="row g-3">
                <div className="col-md-6">
                  <label className="form-label">División</label>
                  <input type="text" className="form-control" disabled
                         value={divisionInfo?.nombre || (perfil?.division?.id ? `ID ${perfil.division.id}` : '')}
                         placeholder="Se carga desde tu perfil" />
                </div>
                <div className="col-md-6">
                  <label className="form-label">Programa educativo</label>
                  <input type="text" className="form-control" disabled
                         value={programaInfo?.nombre || (perfil?.programa?.id ? `ID ${perfil.programa.id}` : '')}
                         placeholder="Se carga desde tu perfil" />
                </div>
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
            footer={(
              <>
                <button type="submit" form="form-asesoria" className="btn btn-primary" disabled={savingAsesoria}>
                  {savingAsesoria ? 'Guardando...' : 'Guardar'}
                </button>
                <button type="button" className="btn btn-secondary" onClick={() => { setShowModal(false); setEditingId(null); }}>Cancelar</button>
              </>
            )}
          >
            <form id="form-asesoria" onSubmit={handleGuardarAsesoria}>
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
                <label className="form-label">Alumno</label>
                <select
                  className="form-select"
                  value={formData.asesoria.alumnoId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, alumnoId: Number(e.target.value) }
                    })
                  }
                  required
                >
                  <option value="">Selecciona un alumno</option>
                  {(alumnosAPI.data || []).map((a) => (
                    <option key={a.id ?? a.usuarioId} value={(a.id ?? a.usuarioId)}>
                      {`${a.nombre || ''} ${a.apellido || ''}`.trim()} {a.matricula ? `- ${a.matricula}` : ''}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mb-3">
                <label className="form-label">Grupo</label>
                <select
                  className="form-select"
                  value={formData.asesoria.grupoId || ''}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      asesoria: { ...formData.asesoria, grupoId: Number(e.target.value) }
                    })
                  }
                >
                  <option value="">Selecciona un grupo (opcional)</option>
                  {(gruposAPI.data || []).map((g) => (
                    <option key={g.id} value={g.id}>
                      {g.nombre}
                    </option>
                  ))}
                </select>
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
                  className="form-select"
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
            </form>
          </Modal>
        )}
      </div>
    </Layout>
  );
}