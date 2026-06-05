import { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';
import LoadingScreen from './components/ui/LoadingScreen';

/**
 * Lazy imports — cada módulo genera su propio chunk en el build.
 * El bundle inicial solo carga Login + el shell de la aplicación.
 */
const LoginPage            = lazy(() => import('./views/LoginPage'));
const DashboardPage        = lazy(() => import('./views/DashboardPage'));
const AppointmentPage      = lazy(() => import('./views/AppointmentPage'));
const PatientManagementPage= lazy(() => import('./views/PatientManagementPage'));
const UserManagementPage   = lazy(() => import('./views/UserManagementPage'));
const ConsultaIndexPage    = lazy(() => import('./views/ConsultaIndexPage'));
const ActiveConsultationPage=lazy(() => import('./views/ActiveConsultationPage'));

const NotFound = () => (
  <div className="flex flex-col items-center justify-center h-full py-20 gap-4">
    <i className="bi bi-exclamation-circle text-5xl text-slate-300" />
    <h2 className="text-2xl font-bold text-slate-600">404 — Página no encontrada</h2>
    <p className="text-slate-400 text-sm">La ruta que buscas no existe.</p>
  </div>
);

export default function App() {
  return (
    <BrowserRouter>
      <Suspense fallback={<LoadingScreen />}>
        <Routes>
          {/* ── Ruta pública ─────────────────────────────────────────── */}
          <Route path="/" element={<LoginPage />} />

          {/* ── Rutas privadas: dentro del Layout ────────────────────── */}
          <Route
            element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }
          >
            {/* FIX BUG-08: unificado a /dashboard (antes era /iniciodashboard) */}
            <Route path="/dashboard"          element={<DashboardPage />} />
            <Route path="/agenda"             element={<AppointmentPage />} />
            <Route path="/pacientes"          element={<PatientManagementPage />} />
            <Route path="/usuarios"           element={<UserManagementPage />} />
            <Route path="/consulta"           element={<ConsultaIndexPage />} />
            <Route path="/consulta/:citaId"   element={<ActiveConsultationPage />} />

            {/* Catch-all dentro del layout */}
            <Route path="*" element={<NotFound />} />
          </Route>
        </Routes>
      </Suspense>
    </BrowserRouter>
  );
}
