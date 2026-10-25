import { Link, useLocation } from 'react-router-dom'
import { formatDateTime, formatRupees } from '../utils'

export default function BookingSuccess() {
  const booking = useLocation().state?.booking
  if (!booking) return <p className="empty">No booking to show. <Link to="/">Browse movies</Link></p>

  return (
    <section className="card ticket">
      <h1>✅ Booking Confirmed</h1>
      <p>Booking ID: <strong>#{booking.id}</strong></p>
      <p><strong>{booking.show.movie.title}</strong></p>
      <p>{booking.show.theaterName} · {booking.show.screen}</p>
      <p>{formatDateTime(booking.show.showTime)}</p>
      <p>Seats: <strong>{booking.seatNumbers.split(',').join(', ')}</strong></p>
      <p>Amount paid: <strong>{formatRupees(booking.totalAmount)}</strong></p>
      <Link className="btn" to="/my-bookings">View my bookings</Link>
    </section>
  )
}
