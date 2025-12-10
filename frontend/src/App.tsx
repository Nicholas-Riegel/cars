import { useState, useEffect } from 'react'
import axios, { AxiosError } from 'axios'

function App() {

  const [messageState, setMessageState] = useState('')
  const [errorState, setErrorState] = useState<string | null>(null)

  useEffect(() => {
    (async () => {
      try {
        const response = await axios.get('/api/home')
        setMessageState(response.data)
      } catch (err: unknown) {
        setErrorState(err instanceof AxiosError ? err.message : 'An unknown error occurred')
      }
    })()
  }, [])

  return (
    <>
      {errorState 
        ? <p>{errorState}</p> 
        : <p>Backend says: {messageState}</p>
      }
    </>
  )
}

export default App