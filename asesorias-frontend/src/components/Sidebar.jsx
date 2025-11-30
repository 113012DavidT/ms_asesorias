import './Sidebar.css';

export const Sidebar = ({ isOpen, items, onItemClick }) => {
  return (
    <div className={`sidebar ${isOpen ? 'open' : ''}`}>
      <div className="sidebar-header">
        <h5>Menú</h5>
      </div>
      <nav className="sidebar-nav">
        {items.map((item) => (
          <button
            key={item.id}
            className={`sidebar-item ${item.active ? 'active' : ''}`}
            onClick={() => onItemClick(item)}
          >
            <span className="sidebar-icon">{item.icon}</span>
            <span className="sidebar-text">{item.label}</span>
          </button>
        ))}
      </nav>
    </div>
  );
};
