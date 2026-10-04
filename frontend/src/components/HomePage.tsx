import { Link } from 'react-router-dom'
import type { PropTypes } from '../App'
import './HomePage.css'

function HomePage({carsState, errorState}
	: Pick<PropTypes, 'carsState' | 'errorState'>) {

	return (
		<div className='car-container'>
			<header>
				<h1>Nick's Favorite Cars</h1>
			</header>
			{errorState 
				? <p>{errorState}</p>
				: (
					carsState.map((car) => (
						<Link 
							to={`car/${car.id}`}
							key={car.id} 
							className='car-card'
						>							
							<img className='car-picture'
								src={`/api/images/${car.imagePath}`} 
								alt={`${car.make} ${car.model}`} 
							/>
							<p>{car.description}</p>
						</Link>
					))
				)
			}
		</div>
	)
}

export default HomePage