import type { PropTypes } from '../App'
import { useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'

const EditPage = ({setCarsState, singleCarState, setSingleCarState}
	: Pick<PropTypes, 'setCarsState' | 'singleCarState' | 'setSingleCarState'>) => {

	const car = singleCarState;

	const [make, setMake] = useState(car ? car.make : '')
	const [model, setModel] = useState(car ? car.model : '')
	const [year, setYear] = useState(car ? String(car.year) : '')
	const [description, setDescription] = useState(car ? car.description : '')
	const [file, setFile] = useState<File | null>(null)
	const fileInputRef = useRef<HTMLInputElement>(null)
	const navigate = useNavigate()
	
	// Early return AFTER hooks
	if (!car) {
		return <div>No car selected for editing</div>
	}

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault()
		
		if (!car) return // Guard clause
		
		const formData = new FormData()
		
		// Only send changed fields
		if (make !== car.make) formData.append('make', make)
		if (model !== car.model) formData.append('model', model)
		if (year !== String(car.year)) formData.append('year', year)
		if (description !== car.description) formData.append('description', description)
		if (file) formData.append('file', file) // Always send if file selected
		
		// Don't send request if nothing changed and no new file
		if (formData.entries().next().done && !file) {
			console.log('No changes detected')
			return
		}

		try {
			const response = await axios.patch(
				`/api/cars/${car.id}`, 
				formData, 
				{headers: {
					Authorization: `Bearer ${localStorage.getItem('token')}`, 
					'Content-Type': 'multipart/form-data' 
					}
				}
			)
			
			// Update the single car state
			setSingleCarState(response.data)
			
			// Update the car in the main cars array
			setCarsState(prevCars => 
				prevCars.map(c => c.id === car.id ? response.data : c)
			)
			
			// Navigate back to admin page
			navigate('/admin')
			
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
		<div>EditPage</div>
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
			<button type="submit">Submit</button>
		</form>
		</>
	)
}

export default EditPage