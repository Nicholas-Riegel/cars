import { useParams } from 'react-router-dom'
import type { PropTypes } from '../App'

const CarPage = ({ carsState }: Pick<PropTypes, 'carsState'>) => {
    
    const { id } = useParams()

    const car = carsState.find(car => car.id === Number(id))

    if (!car) return <div>Car not found</div>
    
    return (
        // eventually display comments here
        <div className="car-container">
            <div className='car-card'>
                <img className='car-picture'
                    src={`/api/images/${car.imagePath}`} 
                    alt={`${car.make} ${car.model}`} 
                />
                <p>{car.description}</p>
            </div>
        </div>
    )
}

export default CarPage