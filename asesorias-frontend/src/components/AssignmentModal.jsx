import { useEffect, useMemo, useState } from 'react';
import { Modal } from './Modal';
import { MultiSelectList } from './MultiSelectList';

export function AssignmentModal({
  isOpen,
  onClose,
  divisiones = [],
  programas = [],
  usuarios = [],
  loading = false,
  onAsignarProfesores = async () => {},
  onAsignarAlumnos = async () => {},
  mode = 'BOTH', // 'PROFESORES' | 'ALUMNOS' | 'BOTH'
  assignedProfesorUsuarioIds = [],
  assignedAlumnoUsuarioIds = []
}) {
  const [divisionId, setDivisionId] = useState('');
  const [programaId, setProgramaId] = useState('');
  const [selProfes, setSelProfes] = useState([]);
  const [selAlumnos, setSelAlumnos] = useState([]);
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (!isOpen) return;
    setDivisionId('');
    setProgramaId('');
    setSelProfes([]);
    setSelAlumnos([]);
    setBusy(false);
  }, [isOpen]);

  const profesores = useMemo(() => {
    return (usuarios || [])
      .filter((u) => (u.rolNombre || '').toUpperCase() === 'PROFESOR')
      .map((u) => ({ id: u.id, label: `${u.nombre || ''} ${u.apellido || ''}`.trim(), sublabel: u.correoMatricula }));
  }, [usuarios]);

  const alumnos = useMemo(() => {
    return (usuarios || [])
      .filter((u) => (u.rolNombre || '').toUpperCase() === 'ALUMNO')
      .map((u) => ({ id: u.id, label: `${u.nombre || ''} ${u.apellido || ''}`.trim(), sublabel: u.correoMatricula }));
  }, [usuarios]);

  const programasFiltrados = useMemo(() => {
    if (!divisionId) return programas;
    // Si los programas tienen divisionId en el modelo, filtrar; si no, mostrar todos
    return (programas || []).filter((p) => !p.divisionId || String(p.divisionId) === String(divisionId));
  }, [programas, divisionId]);

  const toggle = (list, setter, id) => {
    if (list.includes(id)) setter(list.filter((x) => x !== id));
    else setter([...list, id]);
  };

  const toggleAll = (setter) => (ids) => setter(ids);

  const canAssignProfes = divisionId && programaId && selProfes.length > 0;
  const canAssignAlumnos = divisionId && programaId && selAlumnos.length > 0;

  const handleAsignarProfes = async () => {
    if (!canAssignProfes) return;
    setBusy(true);
    try {
      await onAsignarProfesores({
        divisionId: Number(divisionId),
        programaId: Number(programaId),
        usuarioIds: selProfes
      });
      setSelProfes([]);
    } finally {
      setBusy(false);
    }
  };

  const handleAsignarAlumnos = async () => {
    if (!canAssignAlumnos) return;
    setBusy(true);
    try {
      await onAsignarAlumnos({
        divisionId: Number(divisionId),
        programaId: Number(programaId),
        usuarioIds: selAlumnos
      });
      setSelAlumnos([]);
    } finally {
      setBusy(false);
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      size="lg"
      title="Asignar a División y Programa"
    >
      <div className="row g-3">
        <div className="col-md-4">
          <label className="form-label">División</label>
          <select
            className="form-control"
            value={divisionId}
            onChange={(e) => setDivisionId(e.target.value)}
            title="Elige la división destino"
          >
            <option value="">Selecciona división</option>
            {divisiones.map((d) => (
              <option key={d.id} value={d.id}>
                {d.nombre}
              </option>
            ))}
          </select>
        </div>
        <div className="col-md-4">
          <label className="form-label">Programa educativo</label>
          <select
            className="form-control"
            value={programaId}
            onChange={(e) => setProgramaId(e.target.value)}
            title="Elige el programa educativo destino"
          >
            <option value="">Selecciona programa</option>
            {programasFiltrados.map((p) => (
              <option key={p.id} value={p.id}>
                {p.nombre}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="row g-3 mt-1">
        {(mode === 'BOTH' || mode === 'PROFESORES') && (
        <div className="col-md-6">
          <MultiSelectList
            title={`Profesores ${selProfes.length ? `(${selProfes.length})` : ''}`}
            items={profesores}
            selectedIds={selProfes}
            onToggle={(id) => toggle(selProfes, setSelProfes, id)}
            onToggleAll={toggleAll(setSelProfes)}
            loading={loading}
            placeholder="Buscar profesor..."
            disabledIds={assignedProfesorUsuarioIds}
          />
          <div className="d-flex justify-content-end mt-2">
            <button
              className="btn btn-success"
              onClick={handleAsignarProfes}
              disabled={!canAssignProfes || busy}
              title={!canAssignProfes ? 'Selecciona división, programa y al menos un profesor' : 'Asignar profesores seleccionados'}
            >
              {busy ? 'Asignando...' : 'Asignar Profesores'}
            </button>
          </div>
        </div>
        )}
        {(mode === 'BOTH' || mode === 'ALUMNOS') && (
        <div className="col-md-6">
          <MultiSelectList
            title={`Alumnos ${selAlumnos.length ? `(${selAlumnos.length})` : ''}`}
            items={alumnos}
            selectedIds={selAlumnos}
            onToggle={(id) => toggle(selAlumnos, setSelAlumnos, id)}
            onToggleAll={toggleAll(setSelAlumnos)}
            loading={loading}
            placeholder="Buscar alumno..."
            disabledIds={assignedAlumnoUsuarioIds}
          />
          <div className="d-flex justify-content-end mt-2">
            <button
              className="btn btn-success"
              onClick={handleAsignarAlumnos}
              disabled={!canAssignAlumnos || busy}
              title={!canAssignAlumnos ? 'Selecciona división, programa y al menos un alumno' : 'Asignar alumnos seleccionados'}
            >
              {busy ? 'Asignando...' : 'Asignar Alumnos'}
            </button>
          </div>
        </div>
        )}
      </div>
    </Modal>
  );
}
