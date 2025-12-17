import { Navigate } from 'react-router-dom'

const ProtectedRoute = ({ children }
    : {children: React.ReactNode} ) => {
    
    const token = localStorage.getItem('token')
    
    if (!token) {
        // Replaces /admin with / in the history. 
        // Back button goes to wherever you were before trying /admin
        return <Navigate to="/" replace />
    }
    
    return <>{children}</>
}

export default ProtectedRoute