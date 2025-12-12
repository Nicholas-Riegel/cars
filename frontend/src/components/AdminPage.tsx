import { useState, useRef } from 'react'
import axios from 'axios'

function AdminPage() {
        
    const [make, setMake] = useState('')
    const [model, setModel] = useState('')
    const [year, setYear] = useState('')
    const [description, setDescription] = useState('')
    const [file, setFile] = useState<File | null>(null)
    const fileInputRef = useRef<HTMLInputElement>(null)

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
                {headers: { 'Content-Type': 'multipart/form-data' }
            })
            // Clear form or show success message
            setMake('')
            setModel('')
            setYear('')
            setDescription('')
            // Clear the file input
            if (fileInputRef.current) fileInputRef.current.value = ''
            setFile(null)
        } catch (err) {
            console.error(err)
        }
    }

    return (
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
    )
}

export default AdminPage