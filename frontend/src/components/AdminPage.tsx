import { useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import type { PropTypes } from '../App'
import axios from 'axios'

function AdminPage({carsState, setCarsState, errorState, setSingleCarState}
    : Pick<PropTypes, 'carsState' | 'setCarsState' | 'errorState' | 'setSingleCarState'>) {
        
    const [make, setMake] = useState('')
    const [model, setModel] = useState('')
    const [year, setYear] = useState('')
    const [description, setDescription] = useState('')
    const [file, setFile] = useState<File | null>(null)
    const fileInputRef = useRef<HTMLInputElement>(null)
    const navigate = useNavigate()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        
        const formData = new FormData()
        formData.append('make', make)
        formData.append('model', model)
        if (year) formData.append('year', year)
        if (description) formData.append('description', description)
        if (file) formData.append('file', file)

        try {
            await axios.post(
                '/api/cars/upload-with-image', 
                formData, 
                {headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}`, 
                    'Content-Type': 'multipart/form-data' 
                    }
                }
            )
            // Clear form or show success message
            setMake('')
            setModel('')
            setYear('')
            setDescription('')
            // Clear the file input
            if (fileInputRef.current) fileInputRef.current.value = ''
            setFile(null)
        } catch (err) {
            if (axios.isAxiosError(err) && err.response?.status === 401) {
                // Token invalid or expired - redirect to login
                localStorage.removeItem('token')
                navigate('/login')
            }
            console.error(err)
        }
    }

    const handleEdit = (carId: number) => {
        const carToEdit = carsState.find(car => car.id === carId)
        if (carToEdit) {
            setSingleCarState(carToEdit)
            navigate('/edit/' + carId)
        } else {
            console.error(`Car with ID ${carId} not found`)
            // Optionally show user feedback
        }
    }

    const handleDelete = async (carId: number) => {
        try {
            await axios.delete(
                `/api/cars/${carId}`, 
                {headers: {
                    Authorization: `Bearer ${localStorage.getItem('token')}` 
                    }
                }
            )
            // Remove the deleted car from state
            setCarsState(prevCars => prevCars.filter(car => car.id !== carId))
        } catch (err) {
            if (axios.isAxiosError(err) && err.response?.status === 401) {
                // Token invalid or expired - redirect to login
                localStorage.removeItem('token')
                navigate('/login')
            }
            console.error(err)
        }
    }

    return (
        <>
                <form onSubmit={handleSubmit}>
                <input 
                    type="text" 
                    value={make} 
                    onChange={(e) => setMake(e.target.value)}
                    placeholder="Make"
                    required
                />
                <br />
                <input 
                    type="text" 
                    value={model} 
                    onChange={(e) => setModel(e.target.value)}
                    placeholder="Model"
                    required
                />
                <br />
                <input 
                    type="number" 
                    value={year} 
                    onChange={(e) => setYear(e.target.value)}
                    placeholder="Year"
                />
                <br />
                <textarea 
                    value={description} 
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="Description"
                />
                <br />
                <input 
                    ref={fileInputRef}
                    type="file" 
                    accept="image/*"
                    onChange={(e) => setFile(e.target.files?.[0] || null)}
                />
                <br />
                <button type="submit">Add Car</button>
            </form>

            {errorState 
				? <p>{errorState}</p>
				: (
					<ul>
						{carsState.map((car) => (
							<li key={car.id}>
								{car.make} {car.model} {car.year}: {car.description}
                                <br />
								<img 
									src={`/api/images/${car.imagePath}`} 
									alt={`${car.make} ${car.model}`} 
									width="200" 
								/>
                                <button onClick={()=>handleEdit(car.id)}>Edit</button>
                                <button onClick={()=>handleDelete(car.id)}>Delete</button>
							</li>
						))}
					</ul>
				)
			}
        </>
    )
}

export default AdminPage