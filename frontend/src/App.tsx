import { Routes, Route, Link } from 'react-router-dom'
import HomePage from './components/HomePage'
import AdminPage from './components/AdminPage'

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