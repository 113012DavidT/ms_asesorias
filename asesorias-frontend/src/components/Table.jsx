import './Table.css';

export const Table = ({ headers, rows, actions = null, loading = false, emptyMessage = 'No hay datos' }) => {
  return (
    <div className="table-wrapper">
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Cargando...</span>
          </div>
        </div>
      ) : rows.length === 0 ? (
        <div className="text-center py-5 text-muted">{emptyMessage}</div>
      ) : (
        <table className="custom-table">
          <thead>
            <tr>
              {headers.map((header) => (
                <th key={header}>{header}</th>
              ))}
              {actions && <th>Acciones</th>}
            </tr>
          </thead>
          <tbody>
            {rows.map((row, idx) => (
              <tr key={row.id || idx}>
                {headers.map((header) => (
                  <td key={header}>{row[header] || '-'}</td>
                ))}
                {actions && (
                  <td className="actions-cell">
                    {actions(row)}
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};
