import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { showApi, bookingApi, errorMessage } from '../api'
import { formatDateTime, formatRupees } from '../utils'

const MAX_SEATS = 10

export default function SeatSelection() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [show, setShow] = useState(null)
  const [layout, setLayout] = useState(null)
  const [selected, setSelected] = useState([])
  const [form, setForm] = useState({ customerName: '', customerEmail: '', phone: '' })
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const loadSeats = () => showApi.seats(id).then(setLayout)

  useEffect(() => {
    Promise.all([showApi.get(id), showApi.seats(id)])
      .then(([s, l]) => { setShow(s); setLayout(l) })
      .catch((err) => setError(errorMessage(err)))
  }, [id])

  if (!layout || !show) return error ? <p className="error">{error}</p> : <p className="empty">Loading...</p>

  const booked = new Set(layout.bookedSeats)
  const rows = Array.from({ length: layout.totalRows }, (_, i) => String.fromCharCode(65 + i))

  const toggleSeat = (seat) => {
    if (booked.has(seat)) return
    setSelected((cur) => {
      if (cur.includes(seat)) return cur.filter((s) => s !== seat)
      if (cur.length >= MAX_SEATS) return cur
      return [...cur, seat]
    })
  }

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      const booking = await bookingApi.create({ showId: Number(id), ...form, seats: selected })
      navigate('/booking/success', { state: { booking } })
    } catch (err) {
      setError(errorMessage(err))
      setSelected([])
      loadSeats() // someone else may have taken the seats
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section>
      <h1>{show.movie.title}</h1>
      <p className="muted">{show.theaterName} · {show.screen} · {formatDateTime(show.showTime)}</p>

      <div className="seat-map">
        <div className="screen">SCREEN THIS WAY</div>
        {rows.map((row) => (
          <div key={row} className="seat-row">
            <span className="row-label">{row}</span>
            {Array.from({ length: layout.seatsPerRow }, (_, i) => {
              const seat = `${row}${i + 1}`
              const cls = booked.has(seat) ? 'booked' : selected.includes(seat) ? 'selected' : ''
              return (
                <button key={seat} type="button" className={`seat ${cls}`}
                        disabled={booked.has(seat)} onClick={() => toggleSeat(seat)} title={seat}>
                  {i + 1}
                </button>
              )
            })}
          </div>
        ))}
        <div className="legend">
          <span><i className="seat" /> Available</span>
          <span><i className="seat selected" /> Selected</span>
          <span><i className="seat booked" /> Booked</span>
        </div>
      </div>

      <form className="card form" onSubmit={submit}>
        <h2>Your details</h2>
        <input required placeholder="Full name" value={form.customerName}
               onChange={(e) => setForm({ ...form, customerName: e.target.value })} />
        <input required type="email" placeholder="Email" value={form.customerEmail}
               onChange={(e) => setForm({ ...form, customerEmail: e.target.value })} />
        <input placeholder="Phone (10 digits, optional)" value={form.phone} pattern="[0-9]{10}"
               onChange={(e) => setForm({ ...form, phone: e.target.value })} />
        <p>
          Seats: <strong>{selected.length ? selected.join(', ') : 'none'}</strong><br />
          Total: <strong>{formatRupees(selected.length * layout.ticketPrice)}</strong>
        </p>
        {error && <p className="error">{error}</p>}
        <button className="btn" disabled={!selected.length || submitting}>
          {submitting ? 'Booking...' : `Book ${selected.length} ticket(s)`}
        </button>
      </form>
    </section>
  )
}
