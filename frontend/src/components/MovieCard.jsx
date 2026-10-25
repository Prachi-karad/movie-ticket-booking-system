import { Link } from 'react-router-dom'

export default function MovieCard({ movie }) {
  return (
    <Link to={`/movies/${movie.id}`} className="movie-card">
      <img src={movie.posterUrl || 'https://picsum.photos/400/600'} alt={movie.title} />
      <div className="movie-card-body">
        <h3>{movie.title}</h3>
        <p className="muted">{movie.genre} · {movie.language} · {movie.certificate}</p>
      </div>
    </Link>
  )
}
