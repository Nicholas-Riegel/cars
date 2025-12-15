import { Routes, Route, Link, useNavigate, useLocation, Navigate} from 'react-router-dom'
import { useState, useEffect } from 'react'
import axios, { AxiosError } from 'axios'
import HomePage from './components/HomePage'
import AdminPage from './components/AdminPage'
import LoginPage from './components/LoginPage'
import ProtectedRoute from './components/ProtectedRoute'

export type Car = {
	id: number
	make: string
	model: string
	year: number
	description: string
	imagePath: string
}

export type PageProps = {
	carsState: Car[]
	errorState: string | null
}

function App() {

	const navigate = useNavigate()
	const location = useLocation()
	// !! converts value to boolean: string → true, null → false
	const isLoggedIn = !!localStorage.getItem('token')
	const [carsState, setCarsState] = useState<Car[]>([])
	const [errorState, setErrorState] = useState<string | null>(null)

	useEffect(() => {
		(async () => {
			try {
				const response = await axios.get('/api/cars')
				setCarsState(response.data)
			} catch (err: unknown) {
				setErrorState(err instanceof AxiosError ? err.message : 'An unknown error occurred')
			}
		})()
	}, [])

	const handleLogout = () => {
		localStorage.removeItem('token')
		navigate('/')
	}

	return (
		<>
			<nav>
				<Link to="/">Home</Link>
				{isLoggedIn && (
					<>
						{ location.pathname != '/admin' && <Link to="/admin">Add Car</Link> }
						<button onClick={handleLogout}>Logout</button>
					</>
				)} 
			</nav>
			<Routes>
				<Route path="/" element={
						<HomePage {...{carsState, errorState}}/>
					} 
				/>
				<Route path="/login" element={<LoginPage />} />
				<Route path="/admin" element={
					<ProtectedRoute>
						<AdminPage {...{carsState, errorState}} />
					</ProtectedRoute>
				} />
				<Route path="*" element={<Navigate to="/" replace />} />
			</Routes>
		</>
	)
}

export default App