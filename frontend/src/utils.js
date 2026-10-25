export const formatDateTime = (iso) =>
  new Date(iso).toLocaleString('en-IN', {
    weekday: 'short', day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit',
  })

export const formatRupees = (n) => `₹${Number(n).toLocaleString('en-IN')}`
