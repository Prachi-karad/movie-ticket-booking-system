import { useEffect, useState } from 'react'
import { movieApi, showApi, bookingApi, errorMessage } from '../api'
import { formatDateTime, formatRupees } from '../utils'

const emptyMovie = {
  title: '', description: '', genre: '', language: '', durationMinutes: 120,
  certificate: 'UA', posterUrl: '', releaseDate: '',
}
const emptyShow = { movieId: '', theaterName: '', screen: 'Screen 1', showTime: '', ticketPrice: 200, totalRows: 8, seatsPerRow: 12 }

export default function Admin() {
  const [movies, setMovies] = useState([])
  const [bookings, setBookings] = useState([])
  const [movie, setMovie] = useState(emptyMovie)
  const [show, setShow] = useState(emptyShow)
  const [msg, setMsg] = useState('')
  const [error, setError] = useState('')

  const load = () => {
    movieApi.list().then(setMovies)
    bookingApi.all().then(setBookings)
  }
  useEffect(load, [])

  const run = async (fn, success) => {
    setError(''); setMsg('')
    try { await fn(); setMsg(success); load() } catch (err) { setError(errorMessage(err)) }
  }

  const addMovie = (e) => {
    e.preventDefault()
    run(async () => {
      await movieApi.create({ ...movie, releaseDate: movie.releaseDate || null })
      setMovie(emptyMovie)
    }, 'Movie added')
  }

  const addShow = (e) => {
    e.preventDefault()
    run(async () => {
      await showApi.create({ ...show, movieId: Number(show.movieId) })
      setShow(emptyShow)
    }, 'Show added')
  }

  const field = (state, setState, key, props = {}) => (
    <input {...props} value={state[key]} onChange={(e) => setState({ ...state, [key]: e.target.value })} />
  )

  const revenue = bookings.filter((b) => b.status === 'CONFIRMED').reduce((sum, b) => sum + b.totalAmount, 0)

  return (
    <section>
      <h1>Admin Dashboard</h1>
      <div className="stats">
        <div className="card"><strong>{movies.length}</strong><span>Movies</span></div>
        <div className="card"><strong>{bookings.length}</strong><span>Bookings</span></div>
        <div className="card"><strong>{formatRupees(revenue)}</strong><span>Revenue</span></div>
      </div>
      {msg && <p className="success">{msg}</p>}
      {error && <p className="error">{error}</p>}

      <div className="two-col">
        <form className="card form" onSubmit={addMovie}>
          <h2>Add Movie</h2>
          {field(movie, setMovie, 'title', { placeholder: 'Title', required: true })}
          <textarea placeholder="Description" value={movie.description}
                    onChange={(e) => setMovie({ ...movie, description: e.target.value })} />
          {field(movie, setMovie, 'genre', { placeholder: 'Genre', required: true })}
          {field(movie, setMovie, 'language', { placeholder: 'Language', required: true })}
          {field(movie, setMovie, 'durationMinutes', { type: 'number', min: 1, placeholder: 'Duration (min)' })}
          {field(movie, setMovie, 'certificate', { placeholder: 'Certificate (U / UA / A)' })}
          {field(movie, setMovie, 'posterUrl', { placeholder: 'Poster image URL' })}
          {field(movie, setMovie, 'releaseDate', { type: 'date' })}
          <button className="btn">Add movie</button>
        </form>

        <form className="card form" onSubmit={addShow}>
          <h2>Add Show</h2>
          <select required value={show.movieId} onChange={(e) => setShow({ ...show, movieId: e.target.value })}>
            <option value="">Select movie</option>
            {movies.map((m) => <option key={m.id} value={m.id}>{m.title}</option>)}
          </select>
          {field(show, setShow, 'theaterName', { placeholder: 'Theater name', required: true })}
          {field(show, setShow, 'screen', { placeholder: 'Screen' })}
          {field(show, setShow, 'showTime', { type: 'datetime-local', required: true })}
          {field(show, setShow, 'ticketPrice', { type: 'number', min: 1, placeholder: 'Ticket price' })}
          {field(show, setShow, 'totalRows', { type: 'number', min: 1, max: 26, placeholder: 'Rows' })}
          {field(show, setShow, 'seatsPerRow', { type: 'number', min: 1, max: 30, placeholder: 'Seats per row' })}
          <button className="btn">Add show</button>
        </form>
      </div>

      <h2>Movies</h2>
      <table>
        <thead><tr><th>Title</th><th>Genre</th><th>Language</th><th></th></tr></thead>
        <tbody>
          {movies.map((m) => (
            <tr key={m.id}>
              <td>{m.title}</td><td>{m.genre}</td><td>{m.language}</td>
              <td><button className="btn small danger"
                          onClick={() => window.confirm(`Delete ${m.title}?`) && run(() => movieApi.remove(m.id), 'Movie deleted')}>
                Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Recent Bookings</h2>
      <table>
        <thead><tr><th>ID</th><th>Customer</th><th>Movie</th><th>Show</th><th>Seats</th><th>Amount</th><th>Status</th></tr></thead>
        <tbody>
          {bookings.slice().reverse().map((b) => (
            <tr key={b.id}>
              <td>#{b.id}</td><td>{b.customerName}<br /><small>{b.customerEmail}</small></td>
              <td>{b.show.movie.title}</td><td>{formatDateTime(b.show.showTime)}</td>
              <td>{b.seatNumbers}</td><td>{formatRupees(b.totalAmount)}</td>
              <td><span className={`badge ${b.status.toLowerCase()}`}>{b.status}</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  )
}
