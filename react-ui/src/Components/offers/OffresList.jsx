import React from 'react';
import RecruiterCard from "../users/recruiter/RecruiterCard";

const OffersList = ({ offers }) => {
    return (
        <div className="p-6 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {offers.map((offer) => (
                <div
                    key={offer.id}
                    className="bg-white rounded-2xl shadow-md p-5 flex flex-col gap-4"
                >
                    <div>
                        <h2 className="text-xl font-semibold text-gray-800">{offer.title}</h2>
                        <p className="text-sm text-gray-600">{offer.description}</p>
                    </div>

                    <div className="text-sm text-gray-700 space-y-1">
                        <p><strong>Domain:</strong> {offer.domain}</p>
                        <p><strong>Contract Type:</strong> {offer.contractType}</p>
                        <p><strong>Location:</strong> {offer.city}, {offer.country}</p>
                        <p><strong>Salary:</strong> {offer.salaryMin}€ - {offer.salaryMax}€</p>
                        <p><strong>Published:</strong> {offer.publicationDate}</p>
                        <p><strong>Expires:</strong> {offer.expirationDate}</p>
                    </div>

                    <div className="pt-4 border-t">
                        <RecruiterCard recruiter={offer.recruiter} />
                    </div>
                </div>
            ))}
        </div>
    );
};

export default OffersList;
