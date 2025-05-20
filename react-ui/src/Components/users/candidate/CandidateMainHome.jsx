import React, { useEffect, useState } from 'react';
import OfferService from '../../services/OfferService';
import { useNavigate } from 'react-router-dom';

const USER_ID = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).userId : '';

const CandidateMainHome = () => {
    const [offers, setOffers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [hasCV, setHasCV] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const initialize = async () => {
            try {
                const cvExists = await OfferService.checkCV(USER_ID);
                setHasCV(cvExists);

                if (cvExists) {
                    const recommendedOffers = await OfferService.getRecommendedOffers(USER_ID);
                    setOffers(recommendedOffers);
                }
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        initialize();
    }, []);

    const handleApply = (offerId) => {
        navigate('/ApplicationForm', { state: { offreId: offerId } });
    };

    if (loading) {
        return <div className="flex justify-center p-8 text-white">Loading...</div>;
    }

    if (error) {
        return <div className="p-4 text-red-400">{error}</div>;
    }

    if (!hasCV) {
        return <div className="p-4 text-gray-300">Upload your CV to see recommendations</div>;
    }

    return (
        <div className="p-4 bg-black min-h-screen">
            <div className="mx-auto grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 max-w-7xl">
                {offers.length > 0 ? (
                    offers.map(offer => (
                        <div key={offer.id} className="bg-white rounded-lg p-4 space-y-3 flex flex-col h-full">
                            <div>
                                <h3 className="text-lg font-bold text-gray-800">{offer.titre}</h3>
                                <p className="text-sm text-gray-500">{offer.domaine}</p>

                                <div className="text-sm space-y-1 text-gray-700 mt-2">
                                    <p><span className="font-semibold">Contract Type:</span> {offer.typeContrat || 'Not specified'}</p>
                                    <p><span className="font-semibold">Location:</span> {offer.ville}, {offer.pays}</p>
                                    <p><span className="font-semibold">Salary:</span> {offer.salaireMin ? `${offer.salaireMin}€` : '€'} - {offer.salaireMax || ''}€</p>
                                    <p><span className="font-semibold">Published:</span> {offer.datePublication}</p>
                                    <p><span className="font-semibold">Expires:</span> {offer.dateExpiration}</p>
                                </div>
                            </div>

                            <div className="mt-auto pt-3 border-t border-gray-200">
                                <button
                                    onClick={() => handleApply(offer.id)}
                                    className="w-full bg-blue-500 hover:bg-blue-600 text-white py-2 rounded-md transition-colors"
                                >
                                    Apply
                                </button>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="bg-white rounded-lg p-4 text-center text-gray-500 col-span-full">
                        No recommended offers available
                    </div>
                )}
            </div>
        </div>
    );
};

export default CandidateMainHome;