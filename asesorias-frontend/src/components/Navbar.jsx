import { useAuth } from '../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import { FiLogOut, FiMenu } from 'react-icons/fi';
import { useState } from 'react';
import './Navbar.css';

export const Navbar = ({ toggleSidebar }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [showDropdown, setShowDropdown] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const getRolColor = (rol) => {
    const colors = {
      'ADMIN': 'danger',
      'COORDINADOR': 'info',
      'PROFESOR': 'warning',
      'ALUMNO': 'success'
    };
    return colors[rol] || 'secondary';
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
      <div className="container-fluid">
        <button className="btn btn-link text-white me-3" onClick={toggleSidebar}>
          <FiMenu size={24} />
        </button>
        
        <span className="navbar-brand mb-0 h1">📚 Sistema de Asesorías</span>
        
        <div className="d-flex align-items-center gap-3">
          <div className="user-info">
            <span className={`badge bg-${getRolColor(user?.rolNombre)}`}>
              {user?.rolNombre}
            </span>
            <small className="text-white ms-2">
              {user?.nombre} {user?.apellido}
            </small>
          </div>
          
          <div className="dropdown">
            <button
              className="btn btn-link text-white"
              onClick={() => setShowDropdown(!showDropdown)}
            >
              ⚙️
            </button>
            {showDropdown && (
              <div className="dropdown-menu show">
                <button
                  className="dropdown-item"
                  onClick={handleLogout}
                >
                  <FiLogOut className="me-2" />
                  Cerrar Sesión
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};
