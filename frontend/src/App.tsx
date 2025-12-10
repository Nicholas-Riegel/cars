import { useState, useEffect } from 'react'
import axios, { AxiosError } from 'axios'

function App() {

	type Car = {
		id: number
		make: string
		model: string
		year: number
		description: string
	}

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

	return (
		<>
			{errorState 
				? <p>{errorState}</p>
				: (carsState.map((car) => (
						<div key={car.id}>
							{car.year} {car.make} {car.model}: {car.description}
							{/* {JSON.stringify(car)} */}
						</div>
					))
				)
			}
		</>
	)
}

export default App