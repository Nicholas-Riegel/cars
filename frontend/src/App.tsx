import { Routes, Route, Link } from 'react-router-dom'
import HomePage from './assets/HomePage'
import AdminPage from './assets/AdminPage'

function App() {

	return (
		<>
			<nav>
				<Link to="/">Home</Link> 
				<Link to="/admin">Admin</Link>
			</nav>
			<Routes>
				<Route path="/" element={< HomePage/>} />
				<Route path="/admin" element={< AdminPage />} />
			</Routes>
		</>
	)
}

export default App