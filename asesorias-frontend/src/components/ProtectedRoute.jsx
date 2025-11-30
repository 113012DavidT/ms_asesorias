import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

export const ProtectedRoute = ({ children, requiredRole = null }) => {
  const { user, loading, isAuthenticated } = useContext(AuthContext);

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
      </div>
    );
  }

  console.log('ProtectedRoute check:', {
    isAuthenticated,
    requiredRole,
    userRole: user?.rolNombre,
    fullUser: user
  });

  if (!isAuthenticated) {
    console.warn('No autenticado - redirigiendo a login');
    return <Navigate to="/" replace />;
  }

  // Validar rol
  if (requiredRole) {
    const userRole = user?.rolNombre;
    
    if (!userRole) {
      console.error('Usuario no tiene rolNombre definido:', user);
      return <Navigate to="/" replace />;
    }
    
    if (userRole !== requiredRole) {
      console.error(`ACCESO DENEGADO: Rol ${userRole} no permitido para ${requiredRole}`);
      return (
        <div className="alert alert-danger m-5">
          <h2>❌ Acceso Denegado</h2>
          <p>Tu rol ({userRole}) no tiene permisos para acceder a esta sección.</p>
          <p>Se requiere rol: {requiredRole}</p>
          <a href="/" className="btn btn-primary">Volver al Login</a>
        </div>
      );
    }
  }

  console.log('✅ Acceso permitido');
  return children;
};
