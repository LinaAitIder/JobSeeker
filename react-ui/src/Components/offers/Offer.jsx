import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import DataMapper from "../utils/DataMapper";
import ApplyButton from "../applications/ApplyButton";
import CandidateMainHeader from "../utils/headers/CandidateMainHeader";
import offerService from "../../services/OfferService";

const OfferPage = () => {
    const { id } = useParams();
    const [offer, setOffer] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOffer = async () => {
            try {
                const response = await offerService.getOfferById(id);
                setOffer(response.data);
            } catch (err) {
                console.error("Error fetching offer:", err);
                setError("Failed to load offer details");
            } finally {
                setLoading(false);
            }
        };

        fetchOffer();
    }, [id]);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-screen bg-gray-100">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="m-6 text-red-500 text-center">
                {error}
            </div>
        );
    }

    if (!offer) {
        return (
            <div className="m-6 bg-gray-100 text-center">
                Offer not found
            </div>
        );
    }

    const formattedOffer = DataMapper.mapOfferToEnglish(offer);

    return (
        <div className="bg-gray-100 h-full">
            <CandidateMainHeader />
            <div className="max-w-4xl mx-auto p-6  bg-gray-100">
                <div className="bg-white rounded-2xl shadow-lg p-6">
                    <div className="flex justify-between items-start">
                        <div>
                            <h1 className="text-2xl font-bold text-gray-900 mb-2">
                                {formattedOffer.title}
                            </h1>
                            <div className="flex items-center gap-2 text-sm text-gray-600 mb-4">
                                <span>{formattedOffer.company}</span>
                                <span>•</span>
                                <span>{formattedOffer.country || ' Location Not specified'}</span>
                            </div>
                        </div>
                        <div className="flex gap-2">
                            <ApplyButton offerId={formattedOffer.id} />
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6">
                        <div className="md:col-span-2">
                            <div className="mb-6">
                                <h2 className="text-lg font-semibold mb-3">Job Description</h2>
                                <div className="prose max-w-none">
                                    {formattedOffer.description.split('\n').map((paragraph, i) => (
                                        <p key={i} className="mb-4">{paragraph}</p>
                                    ))}
                                </div>
                            </div>


                        </div>

                        <div className="space-y-4">
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <h3 className="font-medium mb-3">Offer Details</h3>
                                <div className="space-y-3">
                                    <div>
                                        <span className="text-sm text-gray-500">Contract Type</span>
                                        <p className="font-medium">{formattedOffer.contractType || 'Not specified'}</p>
                                    </div>
                                    <div>
                                        <span className="text-sm text-gray-500">Domain</span>
                                        <p className="font-medium">{formattedOffer.domain || 'Not specified'}</p>
                                    </div>
                                    <div>
                                        <span className="text-sm text-gray-500">Location</span>
                                        <p className="font-medium">{formattedOffer.city || 'Not specified'}</p>
                                    </div>
                                    {formattedOffer.salaryMin && formattedOffer.salaryMax && (
                                        <div>
                                            <span className="text-sm text-gray-500">Salary Range</span>
                                            <p className="font-medium">
                                                {formattedOffer.salaryMin || '0'} - {formattedOffer.salaryMax || 'Not Specified'} MAD
                                            </p>
                                        </div>
                                    )}

                                </div>
                            </div>

                            <div className="bg-gray-50 p-4 rounded-lg">
                                <div>
                                    <span className="text-sm text-gray-500">Posted on</span>
                                    <p className="font-medium">{formattedOffer.publicationDate}</p>
                                </div>
                                <div>
                                    <span className="text-sm text-gray-500">Expires at</span>
                                    <p className="font-medium">{formattedOffer.expirationDate}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OfferPage;