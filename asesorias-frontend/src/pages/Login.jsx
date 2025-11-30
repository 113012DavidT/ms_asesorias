import { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import Swal from 'sweetalert2';
import './Login.css';

export default function Login() {
  const [correoMatricula, setCorreoMatricula] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!correoMatricula || !password) {
      Swal.fire('Error', 'Por favor completa todos los campos', 'error');
      return;
    }

    setLoading(true);
    try {
      const result = await login(correoMatricula, password);
      
      console.log('Login result:', result); // DEBUG
      
      if (result.success) {
        Swal.fire('Éxito', 'Sesión iniciada correctamente', 'success');
        
        // Redirigir según el rol
        const roleRoutes = {
          'ADMIN': '/admin',
          'COORDINADOR': '/coordinador',
          'PROFESOR': '/profesor',
          'ALUMNO': '/alumno'
        };
        
        const targetRoute = roleRoutes[result.rolNombre] || '/';
        console.log('Redirigiendo a:', targetRoute, 'Rol:', result.rolNombre); // DEBUG
        
        navigate(targetRoute);
      } else {
        Swal.fire('Error', result.error, 'error');
      }
    } catch (error) {
      console.error('Error al iniciar sesión:', error);
      Swal.fire('Error', 'Error al iniciar sesión', 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <div className="login-header">
          <h1>📚 Sistema de Asesorías</h1>
          <p>Universidad Técnica Equinoccial</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="correo" className="form-label">Email o Matrícula</label>
            <input
              type="email"
              className="form-control"
              id="correo"
              placeholder="ejemplo@uteq.edu"
              value={correoMatricula}
              onChange={(e) => setCorreoMatricula(e.target.value)}
              disabled={loading}
            />
          </div>

          <div className="mb-3">
            <label htmlFor="password" className="form-label">Contraseña</label>
            <input
              type="password"
              className="form-control"
              id="password"
              placeholder="Ingresa tu contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary w-100 mb-3"
            disabled={loading}
          >
            {loading ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                Iniciando sesión...
              </>
            ) : (
              'Iniciar Sesión'
            )}
          </button>
        </form>

        <div className="credentials-info">
          <h5>📋 Credenciales de Prueba</h5>
          <div className="credential-item">
            <strong>Admin:</strong>
            <p>admin@uteq.edu / pass123</p>
          </div>
          <div className="credential-item">
            <strong>Coordinador:</strong>
            <p>coordinador1@uteq.edu / pass123</p>
          </div>
          <div className="credential-item">
            <strong>Profesor:</strong>
            <p>profesor1@uteq.edu / pass123</p>
          </div>
          <div className="credential-item">
            <strong>Alumno:</strong>
            <p>alumno1@uteq.edu / pass123</p>
          </div>
        </div>
      </div>
    </div>
  );
}