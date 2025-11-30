import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute';
import Login from './pages/Login';
import DashboardAdmin from './pages/DashboardAdmin';
import DashboardCoordinador from './pages/DashboardCoordinador';
import DashboardProfesor from './pages/DashboardProfesor';
import DashboardAlumno from './pages/DashboardAlumno';
import DebugAuth from './pages/DebugAuth';
import 'bootstrap/dist/css/bootstrap.min.css';

export default function App(){
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Login/>}/>
          <Route path='/debug-auth' element={<DebugAuth/>}/>
          <Route path='/admin' element={<ProtectedRoute requiredRole="ADMIN"><DashboardAdmin/></ProtectedRoute>}/>
          <Route path='/coordinador' element={<ProtectedRoute requiredRole="COORDINADOR"><DashboardCoordinador/></ProtectedRoute>}/>
          <Route path='/profesor' element={<ProtectedRoute requiredRole="PROFESOR"><DashboardProfesor/></ProtectedRoute>}/>
          <Route path='/alumno' element={<ProtectedRoute requiredRole="ALUMNO"><DashboardAlumno/></ProtectedRoute>}/>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}