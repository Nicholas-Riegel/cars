import type { PropTypes } from '../App'

function HomePage({carsState, errorState}
	: Pick<PropTypes, 'carsState' | 'errorState'>) {

	return (
		<>
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
							</li>
						))}
					</ul>
				)
			}
		</>
	)
}

export default HomePage