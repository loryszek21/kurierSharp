// src/components/Panel.tsx
import React, { useEffect, useState } from 'react';
import type { Package } from "../types/Package"; // Twoja definicja typu Package
import type { Person } from "../types/Person";   // Twoja definicja typu Courier

// Komponenty dla list (stworzymy je poniżej)
import PackageTile from '../components/PackageTile';
import CourierTile from '../components/CourierTile';

const Panel = () => {
    const [isLoadingPackages, setIsLoadingPackages] = useState<boolean>(true);
    const [packageError, setPackageError] = useState<string | null>(null);
    const [packagesData, setPackagesData] = useState<Package[]>([]);

    const [isLoadingCouriers, setIsLoadingCouriers] = useState<boolean>(true);
    const [courierError, setCourierError] = useState<string | null>(null);
    const [couriersData, setCouriersData] = useState<Person[]>([]);

    // Efekt do pobierania paczek
    useEffect(() => {
        const fetchPackages = async () => {
            setIsLoadingPackages(true);
            setPackageError(null);
            try {
                const response = await fetch('http://localhost:5000/api/Package');
                if (!response.ok) {
                    let errorMessage = `Błąd HTTP paczek! Status: ${response.status}`;
                    try { const errorBody = await response.json(); errorMessage += ` - ${errorBody.message || JSON.stringify(errorBody)}`; } catch (e) { console.error('Błąd parsowania odpowiedzi paczek:', e); }
                    throw new Error(errorMessage);
                }
                const data: Package[] = await response.json();
                setPackagesData(data);
            } catch (err) {
                if (err instanceof Error) { setPackageError(err.message); } else { setPackageError('Nieznany błąd paczek.'); }
                console.error('Błąd podczas pobierania paczek:', err);
            } finally {
                setIsLoadingPackages(false);
            }
        };
        fetchPackages();
    }, []);

    // Efekt do pobierania kurierów
    useEffect(() => {
        const fetchCouriers = async () => {
            setIsLoadingCouriers(true);
            setCourierError(null);
            try {
                const response = await fetch('http://localhost:5000/api/Courier');
                if (!response.ok) {
                    let errorMessage = `Błąd HTTP kurierów! Status: ${response.status}`;
                    try { const errorBody = await response.json(); errorMessage += ` - ${errorBody.message || JSON.stringify(errorBody)}`; } catch (e) { console.error('Błąd parsowania odpowiedzi kurierów:', e); }
                    throw new Error(errorMessage);
                }
                const data: Person[] = await response.json();
                setCouriersData(data);
            } catch (err) {
                if (err instanceof Error) { setCourierError(err.message); } else { setCourierError('Nieznany błąd kurierów.'); }
                console.error('Błąd podczas pobierania kurierów:', err);
            } finally {
                setIsLoadingCouriers(false);
            }
        };
        fetchCouriers();
    }, []);

    const isLoading = isLoadingPackages || isLoadingCouriers;
    const error = packageError || courierError; // Można wyświetlić pierwszy napotkany błąd

    if (isLoading) {
        return (
            <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
                <p className="text-lg text-gray-700">Ładowanie danych...</p>
                {/* Można dodać bardziej szczegółowe wskaźniki ładowania */}
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
                <h1 className="text-4xl font-bold mb-4 text-red-600">Błąd!</h1>
                <p className="text-lg text-red-700">Nie udało się pobrać danych: {error}</p>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-100 p-4 sm:p-6 lg:p-8">
            <header className="mb-8 text-center">
                <h1 className="text-3xl sm:text-4xl font-bold text-gray-800">Panel Zarządzania</h1>
            </header>

            <div className="flex flex-col lg:flex-row gap-6">
                {/* Kolumna Paczek */}
                <section className="flex-1 bg-white p-6 rounded-lg shadow-lg">
                    <h2 className="text-2xl font-semibold text-gray-700 mb-6 border-b pb-3">
                        Paczki ({packagesData.length})
                    </h2>
                    {packagesData.length > 0 ? (
                        <div className="space-y-4 max-h-[70vh] overflow-y-auto pr-2">
                            {packagesData.map((pkg) => (
                                <PackageTile key={pkg.id} packageData={pkg} />
                            ))}
                        </div>
                    ) : (
                        <p className="text-gray-500">Brak paczek do wyświetlenia.</p>
                    )}
                </section>

                {/* Kolumna Kurierów */}
                <section className="flex-1 bg-white p-6 rounded-lg shadow-lg">
                    <h2 className="text-2xl font-semibold text-gray-700 mb-6 border-b pb-3">
                        Kurierzy ({couriersData.length})
                    </h2>
                    {couriersData.length > 0 ? (
                        <div className="space-y-4 max-h-[70vh] overflow-y-auto pr-2">
                            {couriersData.map((courier) => (
                                <CourierTile key={courier.id} courierData={courier} />
                            ))}
                        </div>
                    ) : (
                        <p className="text-gray-500">Brak kurierów do wyświetlenia.</p>
                    )}
                </section>
            </div>
        </div>
    );
};

export default Panel;