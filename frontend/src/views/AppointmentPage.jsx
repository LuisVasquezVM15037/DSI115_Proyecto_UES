import React, { useState } from 'react';
import { useAgenda } from '../hooks/useAgenda';
import AppointmentCard from '../components/AppointmentCard';
import AppointmentForm from '../components/AppointmentForm';
import ReprogramModal from '../components/ReprogramModal';
import Button from '../components/ui/Button';
import { LoadingSpinner, EmptyState } from '../components/ui/LoadingSpinner';
import { formatFechaHeader, normalizarFecha, obtenerFechaLocalISO} from '../utils/cita.utils';
import 'bootstrap-icons/font/bootstrap-icons.css';

const DAYS_AHEAD = 6; // días extra a mostrar en la vista semana

/** Genera un array de Date desde hoy hasta hoy + DAYS_AHEAD */
const getWeekDays = () =>
  Array.from({ length: DAYS_AHEAD + 1 }, (_, i) => {
    const d = new Date();
    d.setDate(d.getDate() + i);
    return d;
  });

/** Construye las celdas del mes (lunes primero); null = relleno antes del día 1 */
const buildCalendar = (viewDate) => {
  const year = viewDate.getFullYear();
  const month = viewDate.getMonth();
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const startOffset = (new Date(year, month, 1).getDay() + 6) % 7; // lunes = 0
  return [
    ...Array.from({ length: startOffset }, () => null),
    ...Array.from({ length: daysInMonth }, (_, i) => new Date(year, month, i + 1)),
  ];
};

const WEEKDAY_LABELS = ['L', 'M', 'X', 'J', 'V', 'S', 'D'];

/**
 * Página de agenda de citas.
 * Lógica en useAgenda; vista en Tailwind.
 */
const AppointmentPage = () => {
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [showForm, setShowForm] = useState(false);
  const [reprogramCita, setReprogramCita] = useState(null);
  const [activeTab, setActiveTab] = useState('dia'); // 'dia' | 'semana'
  const [viewDate, setViewDate] = useState(new Date()); // mes mostrado en el calendario

  const agenda = useAgenda(selectedDate);
  const weekDays = getWeekDays();

  const calendarCells = buildCalendar(viewDate);
  const cambiarMes = (delta) =>
    setViewDate(prev => new Date(prev.getFullYear(), prev.getMonth() + delta, 1));

  const handleEditar = (cita) => {
    agenda.prepararEditarCita(cita);
    setShowForm(true);
  };

  const handleNueva = () => {
    agenda.prepararNuevaCita();
    setShowForm(true);
  };

  const handleFormSuccess = () => setShowForm(false);

  return (
    <div className="flex h-full p-5 gap-5 bg-surface overflow-hidden">

      {/* ── SIDEBAR IZQUIERDO: Calendario mini ─────────────────────────── */}
      <aside className="w-72 flex-shrink-0 flex flex-col gap-4">

        {/* Calendario del mes */}
        <div className="bg-white rounded-2xl border border-slate-200 shadow-card p-4">

          {/* Navegación de mes */}
          <div className="flex items-center justify-between mb-3">
            <button
              type="button"
              onClick={() => cambiarMes(-1)}
              className="w-7 h-7 flex items-center justify-center rounded-lg text-slate-500 hover:bg-slate-100 transition-colors"
              aria-label="Mes anterior"
            >
              <i className="bi bi-chevron-left" />
            </button>
            <h5 className="font-bold text-slate-800 text-sm capitalize">
              {viewDate.toLocaleDateString('es-ES', { month: 'long', year: 'numeric' })}
            </h5>
            <button
              type="button"
              onClick={() => cambiarMes(1)}
              className="w-7 h-7 flex items-center justify-center rounded-lg text-slate-500 hover:bg-slate-100 transition-colors"
              aria-label="Mes siguiente"
            >
              <i className="bi bi-chevron-right" />
            </button>
          </div>

          {/* Encabezado de días */}
          <div className="grid grid-cols-7 gap-1 mb-1">
            {WEEKDAY_LABELS.map((d, i) => (
              <div key={i} className="text-center text-[10px] font-semibold text-slate-400 py-1">
                {d}
              </div>
            ))}
          </div>

          {/* Cuadrícula de días */}
          <div className="grid grid-cols-7 gap-1">
            {calendarCells.map((day, idx) => {
              if (!day) return <div key={`empty-${idx}`} />;

              // Usamos la fecha local segura en lugar de toISOString()
              const key = obtenerFechaLocalISO(day);
              const isActive = normalizarFecha(selectedDate) === key;
              const isToday = key === obtenerFechaLocalISO(new Date());
              const count = agenda.citasPorFecha[key]?.length ?? 0;

              return (
                <button
                  key={key}
                  type="button"
                  onClick={() => setSelectedDate(new Date(day))}
                  className={`relative aspect-square flex items-center justify-center rounded-lg text-sm
                              transition-all duration-150
                              ${isActive
                      ? 'bg-primary-600 text-white font-bold shadow-sm shadow-primary-200'
                      : isToday
                        ? 'bg-primary-50 text-primary-700 font-semibold'
                        : 'text-slate-700 hover:bg-slate-100'}`}
                >
                  {day.getDate()}
                  {count > 0 && (
                    <span className={`absolute bottom-1 w-1.5 h-1.5 rounded-full
                                      ${isActive ? 'bg-white' : 'bg-primary-500'}`} />
                  )}
                </button>
              );
            })}
          </div>

          {/* Acción */}
          <Button
            variant="primary"
            size="sm"
            onClick={handleNueva}
            icon={<i className="bi bi-plus-lg" />}
            className="w-full mt-4"
          >
            Nueva cita
          </Button>
        </div>

        {/* Resumen del día seleccionado */}
        <div className="bg-white rounded-2xl border border-slate-200 shadow-card p-4">
          <h6 className="text-xs font-semibold text-slate-500 uppercase tracking-wide mb-3">
            Resumen del día
          </h6>
          <div className="grid grid-cols-2 gap-2 text-center">
            {[
              { label: 'Total', val: agenda.citasDelDia.length, color: 'text-primary-600' },
              { label: 'Pendientes', val: agenda.citasDelDia.filter(c => ['PROGRAMADA', 'PENDIENTE'].includes(c.estadoCita)).length, color: 'text-amber-500' },
              { label: 'Completadas', val: agenda.citasDelDia.filter(c => ['COMPLETADA', 'FINALIZADA'].includes(c.estadoCita)).length, color: 'text-emerald-600' },
              { label: 'Canceladas', val: agenda.citasDelDia.filter(c => c.estadoCita === 'CANCELADA').length, color: 'text-red-500' },
            ].map(({ label, val, color }) => (
              <div key={label} className="bg-slate-50 rounded-xl p-2">
                <p className={`text-xl font-bold tabular-nums ${color}`}>{val}</p>
                <p className="text-[10px] text-slate-500">{label}</p>
              </div>
            ))}
          </div>
        </div>
      </aside>

      {/* ── ÁREA PRINCIPAL ───────────────────────────────────────────────── */}
      <div className="flex-1 flex flex-col overflow-hidden">

        {/* Header del área */}
        <div className="flex items-center justify-between mb-4 flex-shrink-0">
          <div>
            <h4 className="font-bold text-slate-800 capitalize">
              {formatFechaHeader(obtenerFechaLocalISO(selectedDate))}
            </h4>
            <p className="text-xs text-slate-400 mt-0.5">
              {agenda.citasDelDia.length} cita{agenda.citasDelDia.length !== 1 ? 's' : ''} programada{agenda.citasDelDia.length !== 1 ? 's' : ''}
            </p>
          </div>

          {/* Tabs de vista */}
          <div className="flex items-center gap-1 bg-slate-100 rounded-xl p-1">
            {[['dia', 'bi-list-ul', 'Día'], ['semana', 'bi-grid', 'Semana']].map(([tab, icon, label]) => (
              <button
                key={tab}
                type="button"
                onClick={() => setActiveTab(tab)}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-medium
                            transition-all ${activeTab === tab
                    ? 'bg-white text-slate-800 shadow-sm'
                    : 'text-slate-500 hover:text-slate-700'}`}
              >
                <i className={`bi ${icon}`} />{label}
              </button>
            ))}
          </div>
        </div>

        {/* Contenido */}
        <div className="flex-1 overflow-y-auto">
          {agenda.loading && <LoadingSpinner text="Cargando citas..." />}

          {!agenda.loading && showForm && (
            <AppointmentForm
              isEditing={agenda.isEditing}
              date={selectedDate}
              formData={agenda.formData}
              pacientes={agenda.pacientes}
              odontologos={agenda.odontologos}
              loading={agenda.loading}
              onChange={agenda.handleChange}
              onSubmit={() => agenda.handleSubmit(handleFormSuccess)}
              onCancelar={() => setShowForm(false)}
            />
          )}

          {!agenda.loading && !showForm && activeTab === 'dia' && (
            agenda.citasDelDia.length === 0
              ? <EmptyState
                icon="bi-calendar-x"
                title="Sin citas para este día"
                description="No hay citas programadas."
                action={<Button size="sm" onClick={handleNueva} icon={<i className="bi bi-plus" />}>Agendar cita</Button>}
              />
              : <div className="space-y-1">
                {agenda.citasDelDia.map(app => (
                  <AppointmentCard
                    key={app.idCitas}
                    app={app}
                    onEditar={handleEditar}
                    onCancelar={agenda.handleCancelar}
                    onReprogram={(cita) => { agenda.setSelectedCita(cita); setReprogramCita(cita); }}
                  />
                ))}
              </div>
          )}

          {!agenda.loading && !showForm && activeTab === 'semana' && (
            <div className="grid grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-4">
              {weekDays.map(day => {
                const key = obtenerFechaLocalISO(day);
                const citas = agenda.citasPorFecha[key] ?? [];
                return (
                  <div key={key} className="bg-white rounded-2xl border border-slate-200 shadow-card p-4">
                    <div className="mb-3 pb-2 border-b border-slate-100">
                      <p className="text-xs font-bold text-primary-700 uppercase tracking-wide capitalize">
                        {day.toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric', month: 'short' })}
                      </p>
                    </div>
                    {citas.length === 0
                      ? <p className="text-xs text-slate-400 py-2 text-center">Sin citas</p>
                      : citas.map(app => (
                        <AppointmentCard
                          key={app.idCitas}
                          app={app}
                          compact
                          onEditar={handleEditar}
                          onCancelar={agenda.handleCancelar}
                        />
                      ))}
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>

      {/* Modal de reprogramación */}
      <ReprogramModal
        cita={reprogramCita}
        loading={agenda.loading}
        onConfirmar={(data) => agenda.handleReprogramar(reprogramCita?.idCitas, data, () => setReprogramCita(null))}
        onCerrar={() => setReprogramCita(null)}
      />
    </div>
  );
};

export default AppointmentPage;