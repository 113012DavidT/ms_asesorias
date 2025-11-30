# 🏗️ Arquitectura del Frontend

## Descripción General

El frontend está diseñado siguiendo una arquitectura modular y escalable, con separación clara de responsabilidades.

## 📦 Capas de la Aplicación

### 1. **Capa de Presentación (Components)**
- Componentes reutilizables y específicos del dominio
- Separación de estilos CSS por componente
- Componentes controlados con estado local mínimo

**Componentes principales:**
- `Navbar` - Barra de navegación con user info y logout
- `Sidebar` - Menú lateral responsivo
- `Layout` - Layout base que integra Navbar + Sidebar + Content
- `Modal` - Modal reutilizable para formularios
- `Card` - Card reutilizable para contenedores
- `Table` - Tabla reutilizable con acciones

### 2. **Capa de Contexto (Context API)**
- `AuthContext` - Gestión global de autenticación
- Persistencia de sesión en localStorage
- Datos de usuario disponibles en toda la app

### 3. **Capa de Hooks Personalizados**
- `useAuth()` - Hook para acceder a contexto de autenticación
- `useFetch()` - Hook para operaciones CRUD

**useFetch** proporciona:
```javascript
{
  data,      // Datos obtenidos
  loading,   // Estado de carga
  error,     // Mensaje de error
  get,       // GET /endpoint o GET /endpoint/{id}
  post,      // POST /endpoint
  put,       // PUT /endpoint/{id}
  delete     // DELETE /endpoint/{id}
}
```

### 4. **Capa de API (Axios)**
- `axiosConfig.js` - Instancia configurada de Axios
- Interceptor de request: Agrega token JWT automáticamente
- Interceptor de response: Maneja errores 401 y redirige a login
- Base URL: `http://localhost:8000`

### 5. **Capa de Páginas (Pages)**
- `Login.jsx` - Autenticación
- `DashboardAdmin.jsx` - Panel administrativo
- `DashboardCoordinador.jsx` - Panel coordinador
- `DashboardProfesor.jsx` - Panel profesor
- `DashboardAlumno.jsx` - Panel alumno

## 🔄 Flujo de Datos

```
Usuario Interactúa
       ↓
   Componente
       ↓
   Hook (useFetch/useAuth)
       ↓
   Context/localStorage
       ↓
   Axios (con interceptores)
       ↓
   Backend API
       ↓
   Response
       ↓
   Estado actualizado
       ↓
   Componente re-renderiza
```

## 🔐 Seguridad

### Autenticación
1. **Login**: Valida credenciales contra `/auth/login`
2. **Token Storage**: Guarda JWT en localStorage
3. **Token Injection**: Interceptor agrega token a headers
4. **Token Validation**: Cada request incluye token
5. **Token Expiry**: Si 401, redirige a login

### Rutas Protegidas
```jsx
<ProtectedRoute requiredRole="ADMIN">
  <DashboardAdmin />
</ProtectedRoute>
```

Valida:
- Existencia de token
- Rol requerido
- Redirige si no cumple

## 📊 Gestión de Estado

### Niveles de Estado

**1. Local Component State**
- Estado específico del componente
- Formularios, UI toggles
- Refetch triggers

```jsx
const [showModal, setShowModal] = useState(false);
const [editingId, setEditingId] = useState(null);
```

**2. Context State (Global)**
- Datos del usuario
- Token JWT
- Estados de autenticación

```jsx
const { user, token, login, logout } = useAuth();
```

**3. Server State (API)**
- Datos de recursos (usuarios, asesorías, etc)
- Sincronizado via useFetch

```jsx
const { data, loading, error, get, post, put, delete } = useFetch('/usuarios');
```

## 🎯 Patrón de Dashboards

Todos los dashboards siguen el mismo patrón:

```jsx
export default function Dashboard() {
  // 1. Hook de autenticación
  const { user } = useAuth();
  
  // 2. Estado local
  const [activeTab, setActiveTab] = useState('recursos');
  const [showModal, setShowModal] = useState(false);
  
  // 3. APIs
  const recursoAPI = useFetch('/recursos');
  
  // 4. Cargar datos
  useEffect(() => {
    recursoAPI.get();
  }, [activeTab]);
  
  // 5. Handlers
  const handleGuardar = async (formData) => {
    if (editingId) {
      await recursoAPI.put(editingId, formData);
    } else {
      await recursoAPI.post(formData);
    }
  };
  
  // 6. Menu items
  const menuItems = [
    { id: 'recursos', label: 'Recursos', icon: <Icon /> }
  ];
  
  // 7. Render
  return (
    <Layout menuItems={menuItems}>
      {/* Contenido */}
    </Layout>
  );
}
```

## 📱 Responsividad

### Breakpoints
- **Mobile**: < 576px
- **Tablet**: 576px - 992px
- **Desktop**: > 992px

### Estrategia
- Mobile-first en CSS
- Grid responsivo
- Sidebar colapsable
- Tablas horizontales en mobile

## 🎨 Tema de Color

```css
Primario:     #007bff (Azul)
Secundario:   #6c757d (Gris)
Éxito:        #28a745 (Verde)
Peligro:      #dc3545 (Rojo)
Advertencia:  #ffc107 (Amarillo)
Información:  #17a2b8 (Cyan)
```

## 📋 Convenciones

### Naming
- **Componentes**: PascalCase (DashboardAdmin)
- **Archivos**: PascalCase (Login.jsx)
- **Variables**: camelCase (showModal)
- **Funciones**: camelCase (handleGuardar)
- **CSS Classes**: kebab-case (.modal-overlay)

### Estructura de Carpetas
```
Cada componente reutilizable = carpeta separada
Estilos junto a componentes
Páginas en /pages
Contexto en /context
Hooks en /hooks
API en /api
```

### Imports
```jsx
// Componentes
import { Modal } from '../components/Modal';

// Contexto/Hooks
import { useAuth } from '../hooks/useAuth';

// Estilos
import './Dashboard.css';
```

## 🔄 Actualización de Datos

### Trigger de Refetch
```jsx
// Después de crear/editar/eliminar
await recursoAPI.post(data);
await recursoAPI.get(); // Refetch lista
```

### Patrones
- POST/PUT/DELETE → Refetch
- GET en useEffect cuando cambia tab
- Validación antes de submit

## 🚀 Performance

### Optimizaciones
- Componentes funcionales
- Hooks locales para efectos
- Lazy loading potencial
- Memoización donde sea necesario
- Key props en listas

### Mejoras Futuras
- Code splitting por rutas
- Caching de datos
- Paginación
- Búsqueda client-side
- Debounce en búsquedas

## 📚 Librerías Utilizadas

| Librería | Propósito | Versión |
|----------|-----------|---------|
| React | UI Framework | 18.2.0 |
| React Router | Enrutamiento | 6.30.1 |
| Axios | HTTP Client | 1.13.2 |
| Bootstrap | CSS Framework | 5.3.8 |
| SweetAlert2 | Alertas/Modales | 11.26.3 |
| React Icons | Iconos | 5.5.0 |

## 🔗 Integración Backend

### Autenticación
- Endpoint: `POST /auth/login`
- Response: token + user data
- Headers: `Authorization: Bearer <token>`

### CRUD Pattern
- GET `/recurso` - Listar
- GET `/recurso/{id}` - Obtener
- POST `/recurso` - Crear
- PUT `/recurso/{id}` - Editar
- DELETE `/recurso/{id}` - Eliminar

## 🧪 Testing (Futuro)

Estructura preparada para:
- Unit tests (Jest)
- Component tests (React Testing Library)
- E2E tests (Cypress)

---

**Última actualización**: Noviembre 2025
