# 🎯 GUÍA PASO A PASO - CORRECCIÓN DEL FRONTEND

## 📋 PROBLEMA IDENTIFICADO

El frontend intenta hacer login a `http://localhost:8000/auth/login` pero el endpoint correcto es `http://localhost:8000/api/auth/login`

---

## 🔧 SOLUCIÓN: 5 PASOS

### PASO 1: Encontrar el archivo AuthContext (o equivalente)

**Ubicación típica:**
```
src/
  ├── context/
  │   └── AuthContext.jsx          ← AQUÍ
  ├── services/
  │   └── authService.js           ← O AQUÍ
  ├── utils/
  │   └── api.js                   ← O AQUÍ
  └── pages/
      └── Login.jsx
```

**Busca en tu proyecto archivos que contengan:**
```javascript
// Buscar estas líneas:
"localhost:8000"
"/auth/login"
"login"
"token"
```

---

### PASO 2: Actualizar URL de Login

**ANTES (❌ Incorrecto):**
```javascript
// AuthContext.jsx o authService.js
const login = async (correoMatricula, password) => {
  const response = await axios.post(
    "http://localhost:8000/auth/login",  // ❌ FALTA /api
    {
      correoMatricula,
      password
    }
  );
  return response.data;
};
```

**DESPUÉS (✅ Correcto):**
```javascript
// AuthContext.jsx o authService.js
const login = async (correoMatricula, password) => {
  const response = await axios.post(
    "http://localhost:8000/api/auth/login",  // ✅ CON /api
    {
      correoMatricula,
      password
    }
  );
  
  // Guardar el token
  if (response.data.token) {
    localStorage.setItem("token", response.data.token);
    localStorage.setItem("refreshToken", response.data.refreshToken);
    localStorage.setItem("user", JSON.stringify({
      usuarioId: response.data.usuarioId,
      nombre: response.data.nombre,
      rolNombre: response.data.rolNombre
    }));
  }
  
  return response.data;
};
```

---

### PASO 3: Crear Interceptor para Token Automático

**Crea archivo:** `src/utils/axiosConfig.js`

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8000'
});

// Interceptor: Agregar token a TODAS las peticiones
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor: Manejar token expirado
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Si es 401 y no hemos reintentado
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await axios.post(
          'http://localhost:8000/api/auth/refresh',
          {},
          {
            headers: {
              'Authorization': `Bearer ${refreshToken}`
            }
          }
        );

        // Actualizar tokens
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('refreshToken', response.data.refreshToken);

        // Reintentar request original con nuevo token
        originalRequest.headers.Authorization = `Bearer ${response.data.token}`;
        return api(originalRequest);
      } catch (refreshError) {
        // Token refresh falló, ir a login
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
```

---

### PASO 4: Usar el Interceptor en Todos los Services

**ANTES (❌ Sin token):**
```javascript
// src/services/userService.js
import axios from 'axios';

const getUsers = () => {
  return axios.get('http://localhost:8000/api/admin/usuarios');  // ❌ Sin token
};
```

**DESPUÉS (✅ Con token automático):**
```javascript
// src/services/userService.js
import api from '../utils/axiosConfig';  // ← USAR ESTE

const getUsers = () => {
  return api.get('/api/admin/usuarios');  // ✅ Con interceptor
};
```

---

### PASO 5: Actualizar Login Component

**Archivo:** `src/pages/Login.jsx` (o similar)

**ANTES (❌ Incorrecto):**
```jsx
import React, { useState } from 'react';
import axios from 'axios';

export const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        'http://localhost:8000/auth/login',  // ❌ FALTA /api
        {
          correoMatricula: email,
          password
        }
      );
      // NO guarda token
      console.log(response.data);
    } catch (err) {
      setError(err.message);  // ❌ Manejo básico
    }
  };

  return (
    <form onSubmit={handleLogin}>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Email o Matrícula"
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Contraseña"
      />
      <button type="submit">Ingresar</button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </form>
  );
};
```

**DESPUÉS (✅ Correcto):**
```jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/axiosConfig';

export const Login = () => {
  const [email, setEmail] = useState('admin@uteq.edu');
  const [password, setPassword] = useState('admin123');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await api.post('/api/auth/login', {  // ✅ /api/auth/login
        correoMatricula: email,
        password
      });

      // ✅ Guardar datos
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('refreshToken', response.data.refreshToken);
      localStorage.setItem('user', JSON.stringify({
        usuarioId: response.data.usuarioId,
        nombre: response.data.nombre,
        rolNombre: response.data.rolNombre
      }));

      // ✅ Redirigir al dashboard
      navigate('/dashboard');
    } catch (err) {
      // ✅ Manejo mejorado de errores
      if (err.response?.status === 404) {
        setError('Endpoint no encontrado');
      } else if (err.response?.status === 401) {
        setError('Usuario o contraseña incorrectos');
      } else if (err.response?.status === 500) {
        setError('Error en el servidor');
      } else {
        setError(err.message || 'Error de conexión');
      }
      console.error('Error de login:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleLogin}>
        <h2>Sistema de Asesorías</h2>
        
        <div className="form-group">
          <label>Email o Matrícula:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="admin@uteq.edu"
            disabled={loading}
          />
        </div>

        <div className="form-group">
          <label>Contraseña:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            disabled={loading}
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Ingresando...' : 'Ingresar'}
        </button>

        {error && <p className="error-message">{error}</p>}
      </form>
    </div>
  );
};
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

- [ ] Cambié `/auth/login` a `/api/auth/login`
- [ ] Creé `axiosConfig.js` con interceptores
- [ ] Actualicé todos los services para usar `api` en lugar de `axios`
- [ ] Agregué guardado de token en `localStorage`
- [ ] Implementé manejo de errores mejorado
- [ ] Probé login con credenciales: admin@uteq.edu / admin123
- [ ] Verifico que el token se guarda en localStorage
- [ ] Probé acceso a endpoint protegido (/api/admin/usuarios)
- [ ] Implementé refresh token automático
- [ ] Todo funciona sin errores en consola

---

## 🧪 PRUEBA RÁPIDA EN CONSOLA

Abre la consola del navegador (F12) y ejecuta:

```javascript
// Paso 1: Login
const loginResponse = await fetch('http://localhost:8000/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    correoMatricula: 'admin@uteq.edu',
    password: 'admin123'
  })
});

const loginData = await loginResponse.json();
console.log('✅ Login response:', loginData);

// Guardar token
localStorage.setItem('token', loginData.token);

// Paso 2: Usar token
const usersResponse = await fetch('http://localhost:8000/api/admin/usuarios', {
  headers: {
    'Authorization': 'Bearer ' + loginData.token
  }
});

const usersData = await usersResponse.json();
console.log('✅ Usuarios:', usersData);
```

Si ves `✅` en ambas respuestas, ¡todo está funcionando!

---

## 📞 SOPORTE

Si encuentras problemas:

1. Abre la consola del navegador (F12)
2. Intenta hacer login
3. Copia el error que aparezca
4. Verifica que:
   - La URL es `http://localhost:8000/api/auth/login`
   - Las credenciales son `admin@uteq.edu` / `admin123`
   - El backend está corriendo (verifica con `curl`)
   - No hay bloqueadores CORS

---

**Versión:** 1.0.0
**Fecha:** 2025-11-25
**Prioridad:** 🔴 ALTA - Bloquea funcionalidad
