# 👨‍💻 Guía de Desarrollo - Sistema de Asesorías

## 🎯 Cómo Empezar

### 1. Configuración Inicial

```bash
# Clonar o descargar el proyecto
cd asesorias-frontend

# Instalar dependencias
npm install

# Crear archivo .env
cp .env.example .env

# Iniciar servidor de desarrollo
npm run dev
```

El frontend estará disponible en: `http://localhost:5173`

### 2. Estructura Recomendada para Desarrollo

```
Terminal 1: npm run dev     (Frontend)
Terminal 2: Backend running (Puerto 8000)
Terminal 3: git workflow
```

## 📝 Agregar una Nueva Funcionalidad

### Ejemplo: Agregar sección de "Reportes" al Admin

#### 1. Crear componente página

```jsx
// src/pages/DashboardAdmin.jsx - Agregar:

// En el estado
const [activeTab, setActiveTab] = useState('usuarios');

// En menuItems
{
  id: 'reportes',
  label: 'Reportes',
  icon: <FiBarChart2 />,
  active: activeTab === 'reportes',
  onClick: () => setActiveTab('reportes')
}

// En el render
{activeTab === 'reportes' && (
  <Card title="Reportes del Sistema">
    {/* Contenido */}
  </Card>
)}
```

#### 2. Crear hook para datos

```jsx
// En DashboardAdmin.jsx
const reportesAPI = useFetch('/reportes');

useEffect(() => {
  if (activeTab === 'reportes') {
    reportesAPI.get();
  }
}, [activeTab]);
```

#### 3. Crear modal si es necesario

```jsx
// En DashboardAdmin.jsx
<Modal
  isOpen={showModal && activeTab === 'reportes'}
  title="Nuevo Reporte"
  onClose={() => setShowModal(false)}
  size="md"
>
  <form onSubmit={handleGuardarReporte}>
    {/* Campos del formulario */}
  </form>
</Modal>
```

## 🔧 Crear un Nuevo Componente

### Template: Card Reutilizable

```jsx
// src/components/NewComponent.jsx
import './NewComponent.css';

export const NewComponent = ({ title, data, onAction }) => {
  return (
    <div className="new-component">
      <h3>{title}</h3>
      {/* Contenido */}
    </div>
  );
};

// src/components/NewComponent.css
.new-component {
  padding: 20px;
  border-radius: 8px;
  background: white;
}
```

### Usar el componente

```jsx
// En una página
import { NewComponent } from '../components/NewComponent';

// En el render
<NewComponent 
  title="Mi Componente" 
  data={data}
  onAction={handleAction}
/>
```

## 🔌 Agregar Nuevo Endpoint

### 1. Usar en un componente

```jsx
const miAPI = useFetch('/mi-nuevo-endpoint');

// Obtener lista
useEffect(() => {
  miAPI.get();
}, []);

// En datos
{miAPI.data?.map(item => (...))}

// Crear
const handleCrear = async (formData) => {
  await miAPI.post(formData);
  await miAPI.get(); // Refetch
};

// Editar
const handleEditar = async (id, formData) => {
  await miAPI.put(id, formData);
  await miAPI.get(); // Refetch
};

// Eliminar
const handleEliminar = async (id) => {
  await miAPI.delete(id);
  await miAPI.get(); // Refetch
};
```

## 🎨 Actualizar Estilos

### Estilos Globales
```css
/* src/index.css */
/* Aquí van estilos generales */
```

### Estilos de Componente
```css
/* src/components/MiComponente.css */
.mi-componente {
  /* Estilos específicos */
}
```

### Clases Reutilizables Bootstrap
```jsx
// Márgenes
<div className="mb-3">
<div className="mt-2">
<div className="px-4">

// Flexbox
<div className="d-flex justify-content-between align-items-center">

// Grid
<div className="row">
  <div className="col-md-6">

// Texto
<p className="text-center text-muted">
```

## 🧪 Validación de Formularios

### Validación Básica

```jsx
const handleSubmit = (e) => {
  e.preventDefault();
  
  // Validar campos
  if (!nombre || !email) {
    Swal.fire('Error', 'Completa todos los campos', 'error');
    return;
  }
  
  // Validar email
  if (!email.includes('@')) {
    Swal.fire('Error', 'Email inválido', 'error');
    return;
  }
  
  // Proceder
  handleGuardar();
};
```

### Con Expresiones Regulares

```jsx
// Email
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
if (!emailRegex.test(email)) {
  Swal.fire('Error', 'Email no válido', 'error');
}

// Teléfono
const phoneRegex = /^\d{10}$/;
if (!phoneRegex.test(telefono)) {
  Swal.fire('Error', 'Teléfono debe tener 10 dígitos', 'error');
}
```

## ⚠️ Manejo de Errores

### Patrón General

```jsx
try {
  const result = await miAPI.post(formData);
  Swal.fire('Éxito', 'Operación completada', 'success');
} catch (error) {
  const mensaje = error.response?.data?.message || 'Error desconocido';
  Swal.fire('Error', mensaje, 'error');
  console.error(error);
}
```

### Errores Comunes

```jsx
// 401 - No autenticado (manejado por interceptor)
// 403 - No autorizado
// 404 - No encontrado
// 400 - Datos inválidos
// 500 - Error servidor
```

## 📱 Responsive Design

### Mobile First

```css
/* Mobile (por defecto) */
.component {
  width: 100%;
  padding: 10px;
}

/* Tablet y superior */
@media (min-width: 768px) {
  .component {
    width: 50%;
    padding: 20px;
  }
}

/* Desktop */
@media (min-width: 1024px) {
  .component {
    width: 33%;
  }
}
```

### Clases Bootstrap Responsivas

```jsx
// Grid responsivo
<div className="col-12 col-md-6 col-lg-4">

// Display responsivo
<div className="d-none d-md-block"> {/* Oculto en mobile */}
<div className="d-md-none"> {/* Solo en mobile */}

// Padding responsivo
<div className="p-2 p-md-4"> {/* 2 en mobile, 4 en md+ */}
```

## 🔍 Debugging

### Consola del Navegador

```javascript
// Ver token
console.log(localStorage.getItem('token'));

// Ver usuario
console.log(JSON.parse(localStorage.getItem('user')));

// Ver error de API
console.error('Error:', error.response?.data);
```

### React Developer Tools

1. Instalar extensión
2. Inspeccionar componentes
3. Ver estado y props
4. Editar estado para testing

### Network Tab

1. Abrir DevTools → Network
2. Ejecutar acción
3. Ver requests/responses
4. Verificar status, headers, payload

## 📦 Build y Deploy

### Build para Producción

```bash
npm run build
```

Genera carpeta `dist/` lista para deploy.

### Optimizaciones Pre-Deploy

```jsx
// 1. Verificar console.logs (remover)
console.log(data); // ❌ Remover

// 2. Verificar variables no usadas
import { Component } from 'react'; // ❌ Si no usa

// 3. Verificar deps en useEffect
useEffect(() => { // ✅ Incluir todas las deps
  // ...
}, [dependency]); // Importante!
```

### Environment Variables

```bash
# .env.production
VITE_API_URL=https://api.produccion.com
VITE_APP_NAME="Sistema de Asesorías"
```

Acceder en el código:

```jsx
const apiUrl = import.meta.env.VITE_API_URL;
```

## 🚀 Performance

### Medidas para Mejorar

1. **Lazy Loading**
```jsx
const DashboardAdmin = lazy(() => import('./pages/DashboardAdmin'));

<Suspense fallback={<Spinner />}>
  <DashboardAdmin />
</Suspense>
```

2. **Memoización**
```jsx
const MemoizedComponent = memo(Component);
// O
const value = useMemo(() => expensiveCalculation(), [deps]);
```

3. **Debounce en búsquedas**
```jsx
const handleBuscar = useCallback(
  debounce((valor) => {
    buscarAPI.post({ query: valor });
  }, 500),
  []
);
```

## 📚 Recursos Útiles

- React Docs: https://react.dev
- Bootstrap Docs: https://getbootstrap.com
- Axios Docs: https://axios-http.com
- SweetAlert2: https://sweetalert2.github.io
- React Router: https://reactrouter.com

## 🔗 Comandos Útiles

```bash
# Desarrollo
npm run dev          # Iniciar servidor

# Build
npm run build        # Build producción
npm run preview      # Preview del build

# Linting (si está configurado)
npm run lint         # Verificar código

# Depuración
npm run dev -- --inspect
```

## 💡 Tips y Trucos

### Abrir Modales Desde Otra Pestaña

```jsx
// En el manejador
const handleCrearDesdeOtraPestaña = () => {
  setFormData({ /* vacío */ });
  setEditingId(null);
  setActiveTab('usuarios'); // Cambiar tab
  setShowModal(true); // Abrir modal
};
```

### Cargar Relacionados

```jsx
const handleAbrirModal = async (item) => {
  setEditingId(item.id);
  // Cargar datos relacionados
  const divisiones = await divisionesAPI.get();
  const programas = await programasAPI.get();
  setFormData({ ...item });
  setShowModal(true);
};
```

### Validación en Tiempo Real

```jsx
const [errors, setErrors] = useState({});

const validateField = (name, value) => {
  switch (name) {
    case 'email':
      return value.includes('@') ? '' : 'Email inválido';
    case 'nombre':
      return value.length > 2 ? '' : 'Mínimo 3 caracteres';
    default:
      return '';
  }
};

const handleChange = (e) => {
  const { name, value } = e.target;
  setFormData({ ...formData, [name]: value });
  setErrors({ ...errors, [name]: validateField(name, value) });
};
```

---

**Última actualización**: Noviembre 2025
