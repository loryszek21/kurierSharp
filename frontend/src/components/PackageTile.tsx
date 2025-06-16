import React from 'react';
import type { Package } from '../types/Package'; // Upewnij się, że ścieżka jest poprawna

interface PackageTileProps {
    packageData: Package;
}

const PackageTile: React.FC<PackageTileProps> = ({ packageData }) => {
    const { id, trackingNumber, status, address, sender, recipient, courier, weightKg } = packageData;

    // Funkcja do formatowania statusu (opcjonalnie)
    const formatStatus = (statusValue: string | number) => {
        // Jeśli status to liczba z backendu, możesz tu zmapować na string
        // np. if (statusValue === 0) return "Utworzona";
        return String(statusValue);
    };

    return (
        <div className="bg-gray-50 border border-gray-200 rounded-lg p-4 shadow hover:shadow-md transition-shadow duration-200">
            <div className="mb-3">
                <h3 className="text-lg font-semibold text-indigo-700">Paczka #{id}</h3>
                <p className="text-sm text-gray-500">TN: {trackingNumber}</p>
            </div>

            <div className="space-y-1 text-sm text-gray-700">
                <p>
                    <strong>Status:</strong>
                    <span className={`ml-2 px-2 py-0.5 rounded-full text-xs font-medium ${
                        status === 'Delivered' || Number(status) === 3 ? 'bg-green-100 text-green-700' :
                        status === 'InTransit' || Number(status) === 2 ? 'bg-blue-100 text-blue-700' :
                        status === 'PickedUp' || Number(status) === 1 ? 'bg-yellow-100 text-yellow-700' :
                        'bg-gray-200 text-gray-700'
                    }`}>
                        {formatStatus(status)}
                    </span>
                </p>
                {weightKg && <p><strong>Waga:</strong> {weightKg} kg</p>}
                <p><strong>Adres dostawy:</strong> {address.street}, {address.postalCode} {address.city}</p>
                <p><strong>Nadawca:</strong> {sender.name} {sender.surname}</p>
                <p><strong>Odbiorca:</strong> {recipient.name} {recipient.surname}</p>
                {courier ? (
                    <p><strong>Kurier:</strong> {courier.name} {courier.surname}</p>
                ) : (
                    <p className="text-orange-600"><strong>Kurier:</strong> Nieprzypisany</p>
                )}
            </div>
            {/* Możesz tu dodać przyciski akcji, np. do przypisania kuriera */}
        </div>
    );
};

export default PackageTile;