# ✅ Checklist de Verificación - Sistema de Asesorías Frontend

## Antes de Iniciar

- [ ] Node.js v16+ instalado
- [ ] npm instalado
- [ ] Backend ejecutándose en puerto 8000
- [ ] Git configurado (opcional)

## Setup Inicial

- [ ] Clonar/descargar repositorio
- [ ] `npm install` ejecutado sin errores
- [ ] `.env` creado desde `.env.example`
- [ ] `npm run dev` funciona sin errores
- [ ] Frontend accesible en http://localhost:5173

## Autenticación

- [ ] Login con admin@uteq.edu / admin123 funciona
- [ ] Login con coordinador1@uteq.edu / pass123 funciona
- [ ] Login con profesor1@uteq.edu / pass123 funciona
- [ ] Login con alumno1@uteq.edu / pass123 funciona
- [ ] Token se guarda en localStorage
- [ ] Error con credenciales incorrectas muestra alerta
- [ ] Logout limpia localStorage y redirige a login

## Rutas y Navegación

- [ ] Login accesible sin autenticación
- [ ] Admin dashboard solo con rol ADMIN
- [ ] Coordinador dashboard solo con rol COORDINADOR
- [ ] Profesor dashboard solo con rol PROFESOR
- [ ] Alumno dashboard solo con rol ALUMNO
- [ ] Redirect a login sin token
- [ ] Sidebar se expande/colapsa en mobile
- [ ] Navbar muestra rol correcto

## Dashboard Admin

- [ ] Tab "Usuarios" muestra lista
- [ ] Tab "Divisiones" muestra lista
- [ ] Tab "Programas" muestra lista
- [ ] Botón "+ Agregar" abre modal
- [ ] Crear usuario funciona
- [ ] Editar usuario funciona
- [ ] Eliminar usuario funciona con confirmación
- [ ] Crear división funciona
- [ ] Crear programa funciona
- [ ] Tabla carga datos correctamente
- [ ] Paginación (si aplica)

## Dashboard Coordinador

- [ ] Tab "Profesores" muestra lista
- [ ] Tab "Alumnos" muestra lista
- [ ] Crear profesor funciona
- [ ] Editar profesor funciona
- [ ] Eliminar profesor funciona
- [ ] Crear alumno funciona
- [ ] Editar alumno funciona
- [ ] Eliminar alumno funciona

## Dashboard Profesor

- [ ] Tab "Mis Grupos" muestra lista
- [ ] Tab "Asesorías" muestra lista
- [ ] Tab "Mis Alumnos" muestra lista
- [ ] Crear grupo funciona
- [ ] Editar grupo funciona
- [ ] Eliminar grupo funciona
- [ ] Crear asesoría funciona
- [ ] Editar asesoría funciona
- [ ] Eliminar asesoría funciona
- [ ] Estados de asesoría correctos (PROGRAMADA, COMPLETADA, etc)

## Dashboard Alumno

- [ ] Tab "Solicitar Asesoría" muestra profesores
- [ ] Tab "Mis Asesorías" muestra historial
- [ ] Tarjetas de profesores muestran información
- [ ] Botón "Solicitar Asesoría" abre modal
- [ ] Modal tiene campos: Título, Descripción, Fecha, Horas
- [ ] Enviar solicitud funciona
- [ ] Validación de campos requeridos

## Componentes y UI

### Navbar
- [ ] Logo/título visible
- [ ] Rol del usuario mostrado
- [ ] Nombre y apellido del usuario
- [ ] Botón logout funciona
- [ ] Se expande correctamente

### Sidebar
- [ ] Items de menú visibles
- [ ] Item activo resaltado
- [ ] Click cambia de sección
- [ ] Responsive (colapsable en mobile)

### Modal
- [ ] Se abre correctamente
- [ ] Se cierra con botón X
- [ ] Se cierra con Cancelar
- [ ] Formulario valida
- [ ] Submit funciona
- [ ] Datos se actualizan en tabla

### Tabla
- [ ] Encabezados visibles
- [ ] Datos mostrados correctamente
- [ ] Botones de acción funcionan
- [ ] Empty state si no hay datos
- [ ] Loading state mientras carga

### Formularios
- [ ] Campos requeridos validados
- [ ] Mensajes de error claros
- [ ] Checkboxes funcionan
- [ ] Dropdowns funcionan
- [ ] Textareas redimensionables

## Errores y Validación

- [ ] Validación email en login
- [ ] Validación contraseña requerida
- [ ] Validación campos requeridos en modales
- [ ] Mensajes de error claros (SweetAlert)
- [ ] Manejo de errores 401 (Token expirado)
- [ ] Manejo de errores 404 (No encontrado)
- [ ] Manejo de errores 500 (Servidor)

## API Integration

- [ ] GET /usuarios funciona
- [ ] POST /usuarios funciona
- [ ] PUT /usuarios/{id} funciona
- [ ] DELETE /usuarios/{id} funciona
- [ ] GET /divisiones funciona
- [ ] POST /divisiones funciona
- [ ] GET /programas funciona
- [ ] POST /programas funciona
- [ ] GET /profesores funciona
- [ ] POST /profesores funciona
- [ ] GET /alumnos funciona
- [ ] POST /alumnos funciona
- [ ] GET /asesorias funciona
- [ ] POST /asesorias funciona
- [ ] PUT /asesorias/{id} funciona
- [ ] DELETE /asesorias/{id} funciona

## Responsive Design

### Desktop (> 1024px)
- [ ] Layout correcto
- [ ] Sidebar visible
- [ ] Contenido legible
- [ ] Botones clickeables

### Tablet (768px - 1024px)
- [ ] Layout adaptado
- [ ] Sidebar colapsable
- [ ] Tablas adaptadas
- [ ] Modales centradas

### Mobile (< 768px)
- [ ] Layout vertical
- [ ] Sidebar oculto por defecto
- [ ] Botón menú funciona
- [ ] Tablas horizontales (scroll)
- [ ] Modales pantalla completa
- [ ] Inputs grandes y clickeables

## Performance

- [ ] Página carga en < 3 segundos
- [ ] Modales abren rápido
- [ ] Tablas cargan sin lag
- [ ] No hay memory leaks
- [ ] Console sin errores
- [ ] Network requests optimizadas

## Seguridad

- [ ] Token en localStorage (no en cookies)
- [ ] JWT incluido en headers
- [ ] Rutas protegidas por rol
- [ ] Logout limpia token
- [ ] No se expone datos sensibles en console
- [ ] CORS configurado correctamente
- [ ] Validación en frontend (+ backend)

## Almacenamiento Local

- [ ] Token guardado en localStorage
- [ ] Usuario guardado en localStorage
- [ ] Datos persisten al actualizar página
- [ ] localStorage se limpia al logout
- [ ] No se pierde sesión con F5

## Integración Backend

- [ ] API gateway en puerto 8000 funciona
- [ ] Auth endpoint en /auth/login
- [ ] Usuarios endpoint en /usuarios
- [ ] Divisiones endpoint en /divisiones
- [ ] Programas endpoint en /programas
- [ ] Profesores endpoint en /profesores
- [ ] Alumnos endpoint en /alumnos
- [ ] Asesorías endpoint en /asesorias

## Documentación

- [ ] README.md accesible y actualizado
- [ ] ARQUITECTURA.md completo
- [ ] DESARROLLO.md con ejemplos
- [ ] Comentarios en código donde sea necesario
- [ ] Estructura de carpetas clara

## Build y Producción

- [ ] `npm run build` sin errores
- [ ] Carpeta `dist/` generada
- [ ] `npm run preview` funciona
- [ ] Build optimizado (archivos minificados)

## Checklist Final

- [ ] Todas las funcionalidades del spec completadas
- [ ] No hay console errors
- [ ] No hay console warnings importantes
- [ ] UI consistente y profesional
- [ ] Manejo de errores robusto
- [ ] Performance aceptable
- [ ] Mobile responsive
- [ ] Documentación completa
- [ ] Código limpio y organizado

## Issues Conocidos (Si aplica)

- [ ] Documento cualquier issue encontrado aquí
- [ ] Incluir pasos para reproducir
- [ ] Incluir impacto en funcionalidad

---

## Firma de Aceptación

| Ítem | Responsable | Fecha | OK |
|------|-------------|-------|-----|
| Desarrollo Completado | - | - | ✅ |
| Pruebas Funcionales | - | - | - |
| Revisión de Código | - | - | - |
| Aprobación QA | - | - | - |
| Ready para Deploy | - | - | - |

---

**Última actualización**: Noviembre 2025
**Estado**: LISTO PARA VALIDACIÓN
