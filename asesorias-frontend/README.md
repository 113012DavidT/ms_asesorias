# 📚 Sistema de Asesorías - Frontend

Sistema web para gestión de asesorías escolares. Frontend desarrollado con React + Vite.

## 🚀 Requisitos

- Node.js v16+ 
- npm o yarn
- Acceso a la API (Puerto 8000)

## 📦 Instalación

```bash
# Instalar dependencias
npm install

# Iniciar servidor de desarrollo
npm run dev

# Build para producción
npm run build

# Preview de build
npm run preview
```

## 🌐 URL de Acceso

- **Desarrollo**: `http://localhost:5173`
- **API**: `http://localhost:8000`

## 🔐 Credenciales de Prueba

```
Admin:
  Email: admin@uteq.edu
  Password: admin123

Coordinador:
  Email: coordinador1@uteq.edu
  Password: pass123

Profesor:
  Email: profesor1@uteq.edu
  Password: pass123

Alumno:
  Email: alumno1@uteq.edu
  Password: pass123
```

## 📁 Estructura del Proyecto

```
src/
├── api/
│   └── axiosConfig.js          # Configuración de Axios con interceptores
├── components/
│   ├── Navbar.jsx              # Barra de navegación
│   ├── Sidebar.jsx             # Menú lateral
│   ├── Layout.jsx              # Layout base para dashboards
│   ├── Modal.jsx               # Componente modal reutilizable
│   ├── Card.jsx                # Componente card reutilizable
│   ├── Table.jsx               # Tabla reutilizable
│   ├── ProtectedRoute.jsx       # Rutas protegidas
│   └── *.css                   # Estilos de componentes
├── context/
│   └── AuthContext.jsx         # Context de autenticación
├── hooks/
│   ├── useAuth.js              # Hook para acceder al contexto de auth
│   └── useFetch.js             # Hook para llamadas CRUD a la API
├── pages/
│   ├── Login.jsx               # Página de login
│   ├── DashboardAdmin.jsx      # Panel administrativo
│   ├── DashboardCoordinador.jsx # Panel coordinador
│   ├── DashboardProfesor.jsx   # Panel profesor
│   ├── DashboardAlumno.jsx     # Panel alumno
│   └── Dashboard.css           # Estilos compartidos de dashboards
├── App.jsx                     # Componente raíz
├── main.jsx                    # Entrada de la aplicación
└── index.css                   # Estilos globales
```

## 🔄 Flujo de Autenticación

1. Usuario ingresa credenciales en Login
2. Se valida contra `/auth/login`
3. Se recibe token JWT y datos del usuario
4. Token se guarda en localStorage
5. Token se envía en header `Authorization: Bearer <token>` en todas las requests
6. Si token expira (401), se redirige a login
7. Usuario accede al dashboard según su rol

## 📋 Roles y Permisos

### Admin
- Crear/editar/eliminar usuarios
- Crear/editar/eliminar divisiones
- Crear/editar/eliminar programas
- Crear/editar/eliminar roles
- Acceso total al sistema

### Coordinador
- Asignar profesores a divisiones/programas
- Asignar alumnos a divisiones/programas
- Crear perfiles de profesores
- Crear perfiles de alumnos
- Ver divisiones y programas

### Profesor
- Ver grupos de tutoría asignados
- Crear grupos
- Programar asesorías
- Asignar alumnos a grupos
- Ver asesorías programadas

### Alumno
- Ver profesores disponibles
- Solicitar asesorías
- Ver historial de asesorías
- Ver estado de solicitudes

## 🔌 Endpoints Principales

### Autenticación
- `POST /auth/login` - Login

### Usuarios
- `GET /usuarios` - Listar usuarios
- `POST /usuarios` - Crear usuario
- `PUT /usuarios/{id}` - Editar usuario
- `DELETE /usuarios/{id}` - Eliminar usuario

### Divisiones
- `GET /divisiones` - Listar divisiones
- `POST /divisiones` - Crear división
- `PUT /divisiones/{id}` - Editar división
- `DELETE /divisiones/{id}` - Eliminar división

### Programas
- `GET /programas` - Listar programas
- `POST /programas` - Crear programa
- `PUT /programas/{id}` - Editar programa
- `DELETE /programas/{id}` - Eliminar programa

### Profesores
- `GET /profesores` - Listar profesores
- `POST /profesores` - Crear profesor
- `PUT /profesores/{id}` - Editar profesor
- `DELETE /profesores/{id}` - Eliminar profesor

### Alumnos
- `GET /alumnos` - Listar alumnos
- `POST /alumnos` - Crear alumno
- `PUT /alumnos/{id}` - Editar alumno
- `DELETE /alumnos/{id}` - Eliminar alumno

### Asesorías
- `GET /asesorias` - Listar asesorías
- `GET /asesorias/profesor/{id}` - Asesorías del profesor
- `GET /asesorias/alumno/{id}` - Asesorías del alumno
- `POST /asesorias` - Crear asesoría
- `PUT /asesorias/{id}` - Editar asesoría
- `DELETE /asesorias/{id}` - Eliminar asesoría

## 🎨 Tecnologías Utilizadas

- **React 18.2.0** - Librería de UI
- **Vite 5.1.0** - Build tool y dev server
- **React Router DOM 6.30.1** - Enrutamiento
- **Axios 1.13.2** - Cliente HTTP
- **Bootstrap 5.3.8** - Framework CSS
- **SweetAlert2 11.26.3** - Alertas y modales
- **React Icons 5.5.0** - Iconos

## 🛠️ Funcionalidades Implementadas

✅ Autenticación con JWT
✅ Rutas protegidas por rol
✅ Login con validación
✅ Interceptores de Axios
✅ Persistencia de sesión
✅ Dashboard Admin (CRUD completo)
✅ Dashboard Coordinador (Gestión de profesores y alumnos)
✅ Dashboard Profesor (Gestión de grupos y asesorías)
✅ Dashboard Alumno (Solicitud de asesorías)
✅ Modal reutilizable
✅ Tabla reutilizable
✅ Sidebar responsivo
✅ Alertas SweetAlert
✅ Validación de formularios
✅ Manejo de errores

## 🔄 Próximas Mejoras

- [ ] Implementar búsqueda y filtros avanzados
- [ ] Agregar paginación
- [ ] Implementar notificaciones en tiempo real
- [ ] Agregar exportación a PDF/Excel
- [ ] Mejorar responsive design
- [ ] Agregar dark mode
- [ ] Implementar carga de archivos
- [ ] Agregar estadísticas y gráficos
- [ ] Implementar confirmación de asesorías
- [ ] Agregar calificación de asesorías

## 🐛 Troubleshooting

### Error de CORS
- Asegúrate que el backend esté corriendo en el puerto 8000
- Verifica que el baseURL en `axiosConfig.js` sea correcto

### Token expirado
- El interceptor redirige automáticamente a login
- El token se valida en cada request

### No se cargan datos
- Verifica que el servidor esté corriendo
- Abre la consola del navegador para ver errores
- Verifica que el token sea válido

## 📞 Soporte

Para problemas o sugerencias, contacta al equipo de desarrollo.

---

**Última actualización**: Noviembre 2025
