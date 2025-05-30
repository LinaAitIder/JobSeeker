import React, {useEffect, useRef, useState} from 'react';
import CandidateMainHeader from "../../utils/headers/CandidateMainHeader";
import OfferService from '../../../services/OfferService';
import { useNavigate } from 'react-router-dom';


const CandidateMainHome = ()=>{
    const [offers, setOffers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [hasCV, setHasCV] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const currentUser = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).userId : '';
    const user = localStorage.getItem('user');
    const newUserId = user ? JSON.parse(user).userId : '';
    const [userId, setUserId] = useState('');
    const prevUserIdRef = useRef(userId);


    useEffect(() => {
        const user = localStorage.getItem('user');
        const userId = user ? JSON.parse(user).userId : null;

        if (!userId) {
            setError("User not found. Please log in.");
            setLoading(false);
            return;
        }

        const initialize = async () => {
            try {
                setLoading(true);
                setError(null);

                const cvExists = await OfferService.checkCV(userId);
                setHasCV(cvExists);

                if (cvExists) {
                    const offers = await OfferService.getRecommendedOffers(userId);
                    setOffers(offers);
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

    const LoadingSpinner = () => (
        <div className="flex flex-col items-center justify-center space-y-2">
            <div className="w-12 h-12 border-4 border-blue-600 border-t-transparent rounded-full animate-spin"></div>
            <p className="text-blue-600">loading offers ...</p>
        </div>
    );

    const renderContent = () => {
        if (loading) {
            return <LoadingSpinner />;
        }

        if (error) {
            return (
                <div className=" h-full w-full flex items-center justify-center">
                    <div className="bg-red-300 rounded-lg p-4 text-center text-red-600 col-span-full">
                    {error}
                     </div>
                </div>
            );

        }

        if (!hasCV) {
            return <div className="bg-blue-200 rounded-lg p-4 text-center text-green-950 col-span-full">Upload your CV to see recommendations</div>;
        }

        return(
            <div className="p-4 bg-white min-h-screen">
                <h3 className="text-3xl text-center font-bold text-gray-800 ">Recommended offers for you </h3>
                <div className="mx-auto grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 max-w-7xl mt-8">
                    {offers.length > 0 ? (
                        offers.map(offer => (
                            <div key={offer.id} className="bg-gray-100 rounded-lg p-4 space-y-3 flex flex-col h-full border-2 border-blue-300 ">
                                <div>
                                    <h3 className="text-lg font-bold text-gray-800">{offer.titre}</h3>
                                    <p className="text-sm text-gray-500">{offer.domaine}</p>

                                    <div className="text-sm space-y-1 text-gray-700 mt-2">
                                        <p><span className="font-semibold">Contract Type:</span> {offer.typeContrat || 'Not specified'}</p>
                                        <p><span className="font-semibold">Location:</span> {offer.ville}, {offer.pays}</p>
                                        <p><span className="font-semibold">Salary:</span> {offer.salaireMin ? `${offer.salaireMin}MAD` : 'MAD'} - {offer.salaireMax || ''}MAD</p>
                                        <p><span className="font-semibold">Published:</span> {offer.datePublication}</p>
                                        <p><span className="font-semibold">Expires:</span> {offer.dateExpiration}</p>
                                    </div>
                                </div>

                                <div className="mt-auto pt-3 flex flex-col  h-full ">
                                    <div className="flex-grow"></div>

                                    <button
                                        onClick={() => handleApply(offer.id)}
                                        className="inline-block bg-blue-500 hover:bg-blue-600 text-white  px-4 py-2  rounded-md text-sm "
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
    return (
        <div className="bg-white min-h-screen">
            <CandidateMainHeader />
            <div className="mt-2 p-4"> {/* marge haut 32px + padding vertical 16px */}
                {renderContent()}
            </div>
        </div>
    );
};
export default CandidateMainHome;