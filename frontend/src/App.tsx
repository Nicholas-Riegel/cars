import { Routes, Route, Link, useNavigate, useLocation, Navigate} from 'react-router-dom'
import { useState, useEffect } from 'react'
import axios, { AxiosError } from 'axios'
import AdminPage from './components/AdminPage'
import LoginPage from './components/LoginPage'
import ProtectedRoute from './components/ProtectedRoute'
import EditPage from './components/EditPage'
import HomePage from './components/HomePage'
import CarPage from './components/CarPage'
import './App.css'

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
		const confirmLogout = window.confirm('Are you sure you want to logout?')
		
		if (!confirmLogout) {
			return // User cancelled the logout
		}

		localStorage.removeItem('token')
		navigate('/')
	}

	return (
		<>
			<nav>
				{isLoggedIn && (
					<>
						<Link to="/">Home</Link>
						<div className="nav-right">
							{location.pathname != '/admin' && <Link to="/admin">Admin</Link>}
							<button onClick={handleLogout}>Logout</button>
						</div>
					</>
				)} 
			</nav>
			
			<div className="main">
				<Routes>
					
					
					{/* Login page /login */}
					<Route path="/login" element={<LoginPage />} />
					
					{/* Admin page /admin */}
					<Route path="/admin" element={
						<ProtectedRoute>
							<AdminPage 
								carsState={carsState} 
								setCarsState={setCarsState}
								errorState={errorState}
								setSingleCarState={setSingleCarState}
							/>
						</ProtectedRoute>
					} />
					
					{/* Edit page /edit/:id */}
					<Route path="/edit/:id" element={
						<ProtectedRoute>
							<EditPage 
								setCarsState={setCarsState}
								singleCarState={singleCarState}
								setSingleCarState={setSingleCarState}
							/>
						</ProtectedRoute>
					} />
					
					{/* Home page / */}
					<Route path="/" element={
						<HomePage 
							carsState={carsState}
							errorState={errorState}
						/>
					}/>

					{/* Individual car page /cars/:id */}
					<Route path="/car/:id" element={
						<CarPage 
							carsState={carsState}
						/>
					}/>

					{/* Redirect any unknown routes to home */}
					<Route path="*" element={<Navigate to="/" replace />} />

				</Routes>
			</div>
		</>
	)
}

export default App