import { useEffect, useState } from 'react'
import { movieApi, errorMessage } from '../api'
import MovieCard from '../components/MovieCard'

const GENRES = ['', 'Action', 'Comedy', 'Drama', 'Sci-Fi', 'Thriller']

export default function Home() {
  const [movies, setMovies] = useState([])
  const [search, setSearch] = useState('')
  const [genre, setGenre] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(true)
      movieApi
        .list({ search: search || undefined, genre: genre || undefined })
        .then((data) => { setMovies(data); setError('') })
        .catch((err) => setError(errorMessage(err)))
        .finally(() => setLoading(false))
    }, 300) // debounce typing
    return () => clearTimeout(timer)
  }, [search, genre])

  return (
    <section>
      <h1>Now Showing</h1>
      <div className="filters">
        <input
          placeholder="Search movies..."
          value={search}
          onChange={(e) => { setSearch(e.target.value); setGenre('') }}
        />
        <select value={genre} onChange={(e) => { setGenre(e.target.value); setSearch('') }}>
          {GENRES.map((g) => <option key={g} value={g}>{g || 'All genres'}</option>)}
        </select>
      </div>
      {error && <p className="error">{error}</p>}
      {loading ? <p className="empty">Loading...</p> : (
        movies.length === 0 ? <p className="empty">No movies found.</p> : (
          <div className="grid">
            {movies.map((m) => <MovieCard key={m.id} movie={m} />)}
          </div>
        )
      )}
    </section>
  )
}
