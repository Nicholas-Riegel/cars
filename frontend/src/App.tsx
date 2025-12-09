import { useState, useEffect } from 'react'
import axios from 'axios'

function App() {

  const [message, setMessage] = useState('')
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    axios.get('/api/home')
      .then(response => {
        setMessage(response.data)
      })
      .catch(error => {
        console.error('Error fetching message:', error)
        setError(error.message)
      })
  }, [])

  return (
    <>
      {error ? (
        <p style={{ color: 'red' }}>{error}</p>
      ) : (
        <p>Backend says: <strong>{message}</strong></p>
      )}
    </>
  )
}

export default App
