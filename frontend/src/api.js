import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
})

/** Turns an API error into a readable message. */
export function errorMessage(err) {
  const data = err?.response?.data
  if (data?.errors) return Object.values(data.errors).join(', ')
  return data?.message || err.message || 'Something went wrong'
}

export const movieApi = {
  list: (params) => api.get('/movies', { params }).then((r) => r.data),
  get: (id) => api.get(`/movies/${id}`).then((r) => r.data),
  shows: (id) => api.get(`/movies/${id}/shows`).then((r) => r.data),
  create: (movie) => api.post('/movies', movie).then((r) => r.data),
  remove: (id) => api.delete(`/movies/${id}`),
}

export const showApi = {
  get: (id) => api.get(`/shows/${id}`).then((r) => r.data),
  seats: (id) => api.get(`/shows/${id}/seats`).then((r) => r.data),
  create: (show) => api.post('/shows', show).then((r) => r.data),
}

export const bookingApi = {
  create: (booking) => api.post('/bookings', booking).then((r) => r.data),
  byEmail: (email) => api.get('/bookings', { params: { email } }).then((r) => r.data),
  all: () => api.get('/bookings').then((r) => r.data),
  cancel: (id) => api.put(`/bookings/${id}/cancel`).then((r) => r.data),
}
