import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

export default function DebugAuth() {
  const { user, isAuthenticated, token } = useContext(AuthContext);

  const storedUser = localStorage.getItem('user');
  const storedToken = localStorage.getItem('token');

  return (
    <div style={{ padding: '20px', background: '#f5f5f5', fontFamily: 'monospace' }}>
      <h1>🔍 Debug Autenticación</h1>
      
      <div style={{ background: 'white', padding: '10px', marginBottom: '10px', borderRadius: '5px' }}>
        <h3>Context (AuthContext)</h3>
        <pre>{JSON.stringify({ user, isAuthenticated, token }, null, 2)}</pre>
      </div>

      <div style={{ background: 'white', padding: '10px', marginBottom: '10px', borderRadius: '5px' }}>
        <h3>localStorage</h3>
        <pre>
user: {storedUser}
token: {storedToken ? storedToken.substring(0, 50) + '...' : 'null'}
        </pre>
      </div>

      <div style={{ background: 'white', padding: '10px', marginBottom: '10px', borderRadius: '5px' }}>
        <h3>Rol del usuario</h3>
        <p><strong>Del Context:</strong> {user?.rolNombre || 'NO DEFINIDO'}</p>
        <p><strong>Del localStorage:</strong> {storedUser ? JSON.parse(storedUser).rolNombre : 'NO DEFINIDO'}</p>
      </div>

      <button onClick={() => {
        localStorage.clear();
        window.location.href = '/';
      }} style={{ padding: '10px 20px', background: 'red', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer' }}>
        Limpiar localStorage y volver al login
      </button>
    </div>
  );
}
