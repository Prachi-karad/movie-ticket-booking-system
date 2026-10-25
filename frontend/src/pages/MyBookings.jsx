import { useState } from 'react'
import { bookingApi, errorMessage } from '../api'
import { formatDateTime, formatRupees } from '../utils'

export default function MyBookings() {
  const [email, setEmail] = useState('')
  const [bookings, setBookings] = useState(null)
  const [error, setError] = useState('')

  const search = async (e) => {
    e?.preventDefault()
    try {
      setBookings(await bookingApi.byEmail(email))
      setError('')
    } catch (err) {
      setError(errorMessage(err))
    }
  }

  const cancel = async (id) => {
    if (!window.confirm('Cancel this booking?')) return
    try {
      await bookingApi.cancel(id)
      search()
    } catch (err) {
      setError(errorMessage(err))
    }
  }

  return (
    <section>
      <h1>My Bookings</h1>
      <form className="filters" onSubmit={search}>
        <input required type="email" placeholder="Enter the email you booked with"
               value={email} onChange={(e) => setEmail(e.target.value)} />
        <button className="btn">Find</button>
      </form>
      {error && <p className="error">{error}</p>}
      {bookings && bookings.length === 0 && <p className="empty">No bookings found for this email.</p>}
      <div className="list">
        {bookings?.map((b) => (
          <div key={b.id} className={`card booking ${b.status.toLowerCase()}`}>
            <div>
              <h3>{b.show.movie.title} <span className={`badge ${b.status.toLowerCase()}`}>{b.status}</span></h3>
              <p className="muted">{b.show.theaterName} · {formatDateTime(b.show.showTime)}</p>
              <p>Seats: {b.seatNumbers.split(',').join(', ')} · {formatRupees(b.totalAmount)}</p>
            </div>
            {b.status === 'CONFIRMED' && new Date(b.show.showTime) > new Date() && (
              <button className="btn danger" onClick={() => cancel(b.id)}>Cancel</button>
            )}
          </div>
        ))}
      </div>
    </section>
  )
}
