import axios from 'axios';

// Usar siempre el API Gateway para centralizar auth y routing
// Usamos directamente el API Gateway para evitar inconsistencias del proxy.
// Permitir configurar la URL del API en despliegue (Vite env)
const API_BASE = import.meta?.env?.VITE_API_BASE_URL || 'http://localhost:8000';

const instance = axios.create({
  baseURL: API_BASE,
  headers: { 'Content-Type': 'application/json' },
  withCredentials: false
});

// Interceptor para agregar el token a todas las requests
instance.interceptors.request.use(
  (config) => {
    // Obtener token desde localStorage, con fallback al objeto user
    let token = localStorage.getItem('token');
    if (!token) {
      try {
        const userRaw = localStorage.getItem('user');
        if (userRaw) {
          const userObj = JSON.parse(userRaw);
          token = userObj?.token || null;
        }
      } catch (_) {
        // Ignorar errores de parseo
      }
    }
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    } else {
      // Aviso útil para depurar 401 por falta de token
      // Nota: no bloquea la request; el Gateway responderá 401 si es requerida auth
      // Puedes comentar esta línea si no deseas logs en consola
      if (typeof window !== 'undefined') {
        console.warn('[axios] No se encontró token para la petición', config.method, config.url);
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor para manejar errores de respuesta
instance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Manejar token expirado (401)
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        
        // Si no hay refreshToken, ir al login
        if (!refreshToken) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          localStorage.removeItem('refreshToken');
          window.location.href = '/';
          return Promise.reject(error);
        }

        // Intentar refrescar el token
        const response = await axios.post(
          'http://localhost:8000/api/auth/refresh',
          {},
          {
            headers: {
              'Authorization': `Bearer ${refreshToken}`,
              'Content-Type': 'application/json'
            }
          }
        );

        // Guardar nuevos tokens
        localStorage.setItem('token', response.data.token);
        if (response.data.refreshToken) {
          localStorage.setItem('refreshToken', response.data.refreshToken);
        }

        // Reintentar request original con nuevo token
        originalRequest.headers.Authorization = `Bearer ${response.data.token}`;
        return instance(originalRequest);
      } catch (refreshError) {
        // Token refresh falló, ir al login
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('refreshToken');
        window.location.href = '/';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default instance;