import React from 'react';
import RecruiterCard from "../users/recruiter/RecruiterCard";
import DataMapper from "../utils/DataMapper";
import ApplyButton from "../applications/ApplyButton";
import CandidateMainHeader from "../utils/headers/CandidateMainHeader";

const OfferList = ({ offers }) => {
    return (
        <>
        <CandidateMainHeader/>
            <span className="text-center place-items-center text-2xl text-white m-8">Offers</span>
        <div className="p-6 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {offers.map((offer) => {
                let formattedOffer =(DataMapper.mapOfferToEnglish(offer));
                let offerId =formattedOffer.id;
                console.log("Offer ID:", offerId);

                return (

                <div
                    key={formattedOffer.id}
                    className="bg-white rounded-2xl shadow-md p-5 flex flex-col gap-4"
                >
                    <div>
                        <h2 className="text-xl font-semibold text-gray-800">{formattedOffer.title}</h2>
                        <p className="text-sm text-gray-600">{formattedOffer.description}</p>
                    </div>

                    <div className="text-sm text-gray-700 space-y-1">
                        <p><strong>Domain:</strong> {formattedOffer.domain}</p>
                        <p><strong>Contract Type:</strong> {formattedOffer.contractType}</p>
                        <p><strong>Location:</strong> {formattedOffer.city}, {formattedOffer.country}</p>
                        <p><strong>Salary:</strong> {formattedOffer.salaireMin}€ - {formattedOffer.salaryMax}€</p>
                        <p><strong>Published:</strong> {formattedOffer.publicationDate}</p>
                        <p><strong>Expires:</strong> {formattedOffer.expirationDate}</p>
                    </div>

                    <div className="pt-4 border-t text-black">
                        <ApplyButton offerId={offerId}/>
                    </div>
                </div>
            )})}
        </div>
        </>
    );
};

export default OfferList;
