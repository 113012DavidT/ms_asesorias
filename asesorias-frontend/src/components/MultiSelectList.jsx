import { useMemo, useState } from 'react';

export function MultiSelectList({
  title,
  items = [], // [{ id, label, sublabel }]
  selectedIds = [],
  onToggle = () => {},
  onToggleAll = () => {},
  loading = false,
  placeholder = 'Buscar...',
  disabledIds = []
}) {
  const [query, setQuery] = useState('');

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return items;
    return items.filter((it) =>
      (it.label || '').toLowerCase().includes(q) || (it.sublabel || '').toLowerCase().includes(q)
    );
  }, [items, query]);

  const allVisibleSelected = filtered.length > 0 && filtered.every((it) => selectedIds.includes(it.id));

  return (
    <div className="card h-100">
      <div className="card-header d-flex align-items-center justify-content-between">
        <strong>{title}</strong>
        <div className="btn-group btn-group-sm">
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={() => onToggleAll(filtered.map((f) => f.id))}
            title="Seleccionar visibles"
          >
            {allVisibleSelected ? '✓ Todos' : 'Seleccionar todos'}
          </button>
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={() => onToggleAll([])}
            title="Limpiar selección"
          >
            Limpiar
          </button>
        </div>
      </div>
      <div className="card-body p-2 d-flex flex-column" style={{ minHeight: 260 }}>
        <input
          type="text"
          className="form-control form-control-sm mb-2"
          placeholder={placeholder}
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          title="Filtra por nombre, apellido o correo"
        />
        <div className="list-group flex-grow-1 overflow-auto">
          {loading && <div className="text-center text-muted py-3">Cargando...</div>}
          {!loading && filtered.length === 0 && (
            <div className="text-center text-muted py-3">Sin resultados</div>
          )}
          {!loading &&
            filtered.map((u) => {
              const disabled = disabledIds.includes(u.id);
              return (
              <label key={u.id} className={`list-group-item list-group-item-action d-flex align-items-center ${disabled ? 'opacity-50' : ''}`} title={disabled ? 'Ya asignado' : (u.sublabel || '')}>
                <input
                  type="checkbox"
                  className="form-check-input me-2"
                  checked={selectedIds.includes(u.id)}
                  onChange={() => !disabled && onToggle(u.id)}
                  disabled={disabled}
                />
                <span className="flex-grow-1">
                  <div>{u.label}</div>
                  {u.sublabel && <small className="text-muted">{u.sublabel}</small>}
                </span>
              </label>
            )})}
        </div>
      </div>
    </div>
  );
}
