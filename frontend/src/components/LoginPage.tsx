import { useState } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"

const LoginPage = () => {

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const navigate = useNavigate()

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        setError('')
        
        try {
            const response = await axios.post(
                '/api/auth/login', 
                { email, password }
            )

            localStorage.setItem('token', response.data)
            // Clear form or show success message
            setEmail('')
            setPassword('')
            setError('')
            navigate('/admin')  // Redirect to admin page after login
        } catch (err) {
            setError('Login failed. Please check your credentials.')
            console.error(err)
        }
    }
    return (
        <div className='car-container'>
            <div>Login</div>
            <form onSubmit={handleSubmit}>

                {error && <div style={{color: 'red'}}>{error}</div>}
                
                <label htmlFor="email">Email</label>
                <input 
                    id="email" 
                    type="email" 
                    placeholder="Email" 
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <br />
                <label htmlFor="password">Password</label>
                <input 
                    id="password" 
                    type="password" 
                    placeholder="Password" 
                    required 
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <br />
                <button type="submit">Login</button>
            </form>
        </div>
    )
}

export default LoginPage