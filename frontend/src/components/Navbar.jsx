import { NavLink, Link } from 'react-router-dom'

export default function Navbar() {
  return (
    <header className="navbar">
      <Link to="/" className="brand">🎬 CineBook</Link>
      <nav>
        <NavLink to="/" end>Movies</NavLink>
        <NavLink to="/my-bookings">My Bookings</NavLink>
        <NavLink to="/admin">Admin</NavLink>
      </nav>
    </header>
  )
}
