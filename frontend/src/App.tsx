import { Routes, Route, Link, useNavigate, useLocation, Navigate} from 'react-router-dom'
import { useState, useEffect } from 'react'
import axios, { AxiosError } from 'axios'
import HomePage from './components/HomePage'
import AdminPage from './components/AdminPage'
import LoginPage from './components/LoginPage'
import ProtectedRoute from './components/ProtectedRoute'
import EditPage from './components/EditPage'

export type Car = {
	id: number
	make: string
	model: string
	year: number
	description: string
	imagePath: string
}

export type PropTypes = {
	carsState: Car[]
	setCarsState: React.Dispatch<React.SetStateAction<Car[]>>
	errorState: string | null
	singleCarState: Car | null
	setSingleCarState: React.Dispatch<React.SetStateAction<Car | null>>
}

function App() {

	const navigate = useNavigate()
	const location = useLocation()
	// !! converts value to boolean: string → true, null → false
	const isLoggedIn = !!localStorage.getItem('token')
	const [carsState, setCarsState] = useState<Car[]>([])
	const [errorState, setErrorState] = useState<string | null>(null)
	const [singleCarState, setSingleCarState] = useState<Car | null>(null)

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
						<AdminPage {...{
							carsState, setCarsState, errorState, singleCarState, setSingleCarState
							}} />
					</ProtectedRoute>
				} />
				<Route path="/edit/:id" element={
					<ProtectedRoute>
						<EditPage {...{singleCarState, setSingleCarState}}/>
					</ProtectedRoute>
				} />
				<Route path="*" element={<Navigate to="/" replace />} />
			</Routes>
		</>
	)
}

export default App