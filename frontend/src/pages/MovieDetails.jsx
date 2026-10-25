import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { movieApi, errorMessage } from '../api'
import { formatDateTime, formatRupees } from '../utils'

export default function MovieDetails() {
  const { id } = useParams()
  const [movie, setMovie] = useState(null)
  const [shows, setShows] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([movieApi.get(id), movieApi.shows(id)])
      .then(([m, s]) => { setMovie(m); setShows(s) })
      .catch((err) => setError(errorMessage(err)))
  }, [id])

  if (error) return <p className="error">{error}</p>
  if (!movie) return <p className="empty">Loading...</p>

  // Group shows by theater
  const byTheater = shows.reduce((acc, s) => {
    (acc[s.theaterName] ||= []).push(s)
    return acc
  }, {})

  return (
    <section>
      <div className="movie-hero">
        <img src={movie.posterUrl} alt={movie.title} />
        <div>
          <h1>{movie.title}</h1>
          <p className="muted">
            {movie.genre} · {movie.language} · {movie.durationMinutes} min · {movie.certificate}
          </p>
          <p>{movie.description}</p>
        </div>
      </div>

      <h2>Select a show</h2>
      {shows.length === 0 && <p className="empty">No upcoming shows.</p>}
      {Object.entries(byTheater).map(([theater, list]) => (
        <div key={theater} className="theater">
          <h3>{theater}</h3>
          <div className="show-times">
            {list.map((s) => (
              <Link key={s.id} to={`/shows/${s.id}/seats`} className="show-chip">
                <strong>{formatDateTime(s.showTime)}</strong>
                <span>{s.screen} · {formatRupees(s.ticketPrice)}</span>
              </Link>
            ))}
          </div>
        </div>
      ))}
    </section>
  )
}
