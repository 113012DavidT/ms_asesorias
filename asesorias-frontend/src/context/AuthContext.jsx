import { createContext, useState, useEffect } from 'react';
import axios from '../api/axiosConfig';
import { decodeJWT } from '../utils/jwtDecoder';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(null);

  // Cargar usuario y token del localStorage al iniciar
  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');
    
    if (storedToken && storedUser) {
      setToken(storedToken);
      const userData = JSON.parse(storedUser);
      
      // Intentar obtener rol del JWT si no viene en la respuesta
      if (!userData.rolNombre || userData.rolNombre === 'null') {
        const decoded = decodeJWT(storedToken);
        // El backend firma el claim como 'rol', no 'rolNombre'
        if (decoded && decoded.rol) {
          userData.rolNombre = decoded.rol;
        }
      }
      
      setUser(userData);
    }
    setLoading(false);
  }, []);

  const login = async (correoMatricula, password) => {
    try {
      const res = await axios.post('/api/auth/login', {
        correoMatricula,
        password
      });
      
      console.log('Login response:', res.data); // DEBUG
      
      // Evitar sombra de parámetros (correoMatricula) y conflictos de scope al destructurar
      const {
        token: jwtToken,
        refreshToken,
        rolNombre: rolNombreBackend,
        rolId,
        usuarioId,
        nombre,
        apellido,
        correoMatricula: correoMatriculaBackend
      } = res.data;

      let rolNombre = rolNombreBackend;
      const token = jwtToken; // mantener nombre esperado más adelante
      const correoMatriculaFinal = correoMatriculaBackend;
      
      // Si rolNombre no viene o es null, intentar extraerlo del JWT (claim 'rol')
      if (!rolNombre || rolNombre === 'null') {
        const decoded = decodeJWT(token);
        console.log('JWT Decoded:', decoded); // DEBUG
        rolNombre = decoded?.rol || null; // NO usar fallback ADMIN inseguro
      }
      
      // Guardar en localStorage
      localStorage.setItem('token', token);
      if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
      }
      
      // Guardar usuario con todos los datos incluyendo rol
      const userData_completo = { 
        usuarioId,
        nombre,
        apellido,
        correoMatricula: correoMatriculaFinal,
        rolNombre,
        rolId,
        token
      };
      
      localStorage.setItem('user', JSON.stringify(userData_completo));
      
      // Actualizar estado
      setToken(token);
      setUser(userData_completo);
      
      console.log('User saved:', userData_completo); // DEBUG
      
      return { success: true, rolNombre };
    } catch (error) {
      console.error('Login error:', error);
      // Diferenciar tipos de error
      if (error.response) {
        // Respuesta del servidor con status distinto a 2xx
        const status = error.response.status;
        if (status === 401 || status === 403) {
          return { success: false, error: 'Credenciales inválidas' };
        }
        return { success: false, error: error.response.data?.message || `Error ${status}` };
      } else if (error.request) {
        // Petición enviada pero sin respuesta (CORS, red caída, timeout)
        return { success: false, error: 'Error de red o CORS. Verifica configuración.' };
      } else {
        // Error al preparar la petición
        return { success: false, error: 'Error inesperado en login' };
      }
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  };

  const isAuthenticated = !!user && !!token;

  return (
    <AuthContext.Provider value={{ user, loading, token, login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};