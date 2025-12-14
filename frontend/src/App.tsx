import { Routes, Route, Link, useNavigate, useLocation, Navigate} from 'react-router-dom'
import HomePage from './components/HomePage'
import AddCarPage from './components/AddCarPage'
import LoginPage from './components/LoginPage'
import ProtectedRoute from './components/ProtectedRoute'

function App() {

	const navigate = useNavigate()
	const location = useLocation()
	// !! converts value to boolean: string → true, null → false
	const isLoggedIn = !!localStorage.getItem('token')

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
						{ location.pathname != '/addcar' && <Link to="/addcar">Add Car</Link> }
						<button onClick={handleLogout}>Logout</button>
					</>
				)} 
			</nav>
			<Routes>
				<Route path="/" element={<HomePage/>} />
				<Route path="/login" element={<LoginPage />} />
				<Route path="/addcar" element={
					<ProtectedRoute>
						<AddCarPage />
					</ProtectedRoute>
				} />
				<Route path="*" element={<Navigate to="/" replace />} />
			</Routes>
		</>
	)
}

export default App