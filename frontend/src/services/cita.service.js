import { apiFetch } from './api.service';

export const getCitas      = ()           => apiFetch('/citas');
export const getCitaById   = (id)         => apiFetch(`/citas/${id}`);

export const createCita    = (data)       => apiFetch('/citas', {
  method: 'POST', body: JSON.stringify(data),
});

export const updateCita    = (id, data)   => apiFetch(`/citas/${id}`, {
  method: 'PUT', body: JSON.stringify(data),
});

export const cancelarCita  = (id, motivo) => apiFetch(`/citas/${id}/cancelar`, {
  method: 'PUT', body: JSON.stringify({ motivoCancelacion: motivo }),
});

export const cambiarEstado = (id, estado) => apiFetch(`/citas/${id}/estado`, {
  method: 'PUT', body: JSON.stringify({ estado }),
});
