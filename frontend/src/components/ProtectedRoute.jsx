import React from 'react';
import { Navigate } from 'react-router-dom';

/*
 * Decodifica el payload del token JWT y verifica que no haya expirado.
 * Esta función solo verifica la expiración del lado del cliente, no valida la firma (eso es responsabilidad del servidor).
 */

/**  @param  {string} token — Token JWT almacenado en localStorage.
* @return {boolean}       true si el token existe y no ha expirado, false en caso contrario.
 */

const isTokenValid = (token) => {
  // Si no hay token en absoluto, consideramos que no hay sesión activa
  if (!token) return false;
  try {
    // El JWT tiene 3 partes separadas por '.': header.payload.signature
    // Tomamos la parte [1] (payload) y la decodificamos desde base64 a JSON
    const payload = JSON.parse(atob(token.split('.')[1]));
    // `exp` es un Unix timestamp en segundos; multiplicamos por 1000 para comparar con Date.now() (ms)
    return payload.exp * 1000 > Date.now();
  } catch {
    // Si el token tiene formato inválido o falla el parse, lo tratamos como inválido
    return false;
  }
};

/**
 * Guard de rutas privadas.
 * Si el token no existe o expiró, limpia el storage y redirige al login.
 */
const ProtectedRoute = ({ children }) => {
  // Obtenemos el token del localStorage y verificamos su validez usando `isTokenValid`.
  const token         = localStorage.getItem('authToken');
  // Si el token no es válido (no existe o expiró), se limpia el localStorage para eliminar cualquier dato de sesión y se redirige al usuario a la página de inicio de sesión ("/") usando el componente `Navigate` de React Router.
  const authenticated = isTokenValid(token);

  // Si el usuario no está autenticado, se limpia el localStorage y se redirige al login.
  if (!authenticated) {
    // Limpia cualquier dato de sesión almacenado (como el token) para asegurar que no queden datos obsoletos.
    localStorage.clear(); 
    // Redirige al usuario a la página de inicio de sesión. El prop `replace` asegura que esta redirección reemplace la entrada actual en el historial del navegador, evitando que el usuario pueda volver a la ruta protegida usando el botón "Atrás".
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
