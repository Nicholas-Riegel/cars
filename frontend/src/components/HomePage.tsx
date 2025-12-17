import type { PropTypes } from '../App'

function HomePage({carsState, errorState}
	: Pick<PropTypes, 'carsState' | 'errorState'>) {

	return (
		<div className='car-container'>
			{errorState 
				? <p>{errorState}</p>
				: (
					carsState.map((car) => (
						<div key={car.id}>
							{car.make} {car.model} {car.year}: {car.description}
							<br />
							<img 
								src={`/api/images/${car.imagePath}`} 
								alt={`${car.make} ${car.model}`} 
								width="200" 
							/>
						</div>
					))
				)
			}
		</div>
	)
}

export default HomePage