import React from 'react';
import type { Person } from '../types/Person'; // Upewnij się, że ścieżka jest poprawna

interface CourierTileProps {
    courierData: Person;
}

const CourierTile: React.FC<CourierTileProps> = ({ courierData }) => {
    const { id, name, surname,  phoneNumber } = courierData;

    return (
        <div className="bg-sky-50 border border-sky-200 rounded-lg p-4 shadow hover:shadow-md transition-shadow duration-200">
            <div className="mb-3">
                <h3 className="text-lg font-semibold text-sky-700">{name} {surname}</h3>
                <p className="text-sm text-gray-500">ID: {id}</p>
            </div>

            <div className="space-y-1 text-sm text-gray-700">
                {/* <p><strong>Email:</strong> {email}</p> */}
                <p><strong>Telefon:</strong> {phoneNumber}</p>

            </div>
            {/* Możesz tu dodać przyciski akcji, np. do wyświetlenia paczek kuriera */}
        </div>
    );
};

export default CourierTile;