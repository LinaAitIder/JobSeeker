import React from 'react';
import RecruiterCard from "../users/recruiter/RecruiterCard";
import DataMapper from "../utils/DataMapper";
import ApplyButton from "../applications/ApplyButton";
import CandidateMainHeader from "../utils/headers/CandidateMainHeader";

const OfferList = ({ offers  , isCompanyOffers=false}) => {
    return (
        <>
            {isCompanyOffers &&
                <div className="mb-8 text-center">
                    <h2 className="text-3xl font-bold text-gray-800">Company Offers</h2>
                    <p className="text-sm text-gray-500 mt-1">An overview of the company offers</p>
                </div>
            }
            {offers.length>0 ? (
            <div className="flex flex-col gap-4 p-4 shadow-xl rounded-2xl border-gray-200  bg-gray-50">

                {offers.map((offer) => {
                    const formattedOffer = DataMapper.mapOfferToEnglish(offer);
                    const offerId = formattedOffer.id;

                    return (
                        <div
                            key={offerId}
                            className="bg-white rounded-2xl shadow hover:shadow-lg transition-shadow p-4 relative border border-gray-100"
                        >

                            {(!isCompanyOffers) && (
                                <div className="absolute top-4 right-4">
                                    <ApplyButton offerId={offerId} />
                                </div>
                            )}
                            <div className="flex flex-col gap-2 max-w-3xl">
                                <h2 className="text-lg font-semibold text-gray-900">
                                    {formattedOffer.title}
                                </h2>

                                <p className="text-sm text-gray-700 line-clamp-3">
                                    {formattedOffer.description}
                                </p>

                                <div className="flex flex-wrap items-center gap-2 text-xs text-gray-500 mt-1">
            <span className="bg-gray-100 px-2 py-1 rounded-full">
              {formattedOffer.domain}
            </span>
                                    <span className="bg-gray-100 px-2 py-1 rounded-full">
              {formattedOffer.contractType}
            </span>

                                    {formattedOffer.salaryMin && formattedOffer.salaryMax && (
                                        <span className="bg-green-50 text-green-700 px-2 py-1 rounded-full">
                {formattedOffer.salaryMin} MAD - {formattedOffer.salaryMax} MAD
              </span>
                                    )}
                                </div>

                                <span className="text-xs text-gray-400 mt-1">
            {formattedOffer.date}
          </span>
                            </div>
                        </div>
                    );
                })}
            </div>
            ): (
                    <div className="m-6 text-gray-500 flex-inlin text-center">
                        No Offers Posted Yet !
                    </div>
            )

            }
        </>
    );
};

export default OfferList;
