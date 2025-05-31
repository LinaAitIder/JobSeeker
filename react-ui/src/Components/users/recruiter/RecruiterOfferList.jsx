import React, {useState} from 'react';
import DataMapper from "../../utils/DataMapper"
import {useNavigate} from "react-router-dom";
import OfferApplications from "../../applications/OfferApplications";
import RecruiterService from "../../../services/RecruiterService";
import OfferService from "../../../services/OfferService";

const OfferList = ({ offers }) => {
    const navigate= useNavigate();
    const [message, setMessage] = useState({
        type:'',
        text:''
    });

    async function deleteOffer (e,offerId){
        e.stopPropagation();
        try{
            const response = await OfferService.deleteOfferRequest(offerId);
            window.alert("✅ Offer successfully deleted!")
            window.location.reload();

        }catch(err){
            window.alert("⚠️ A problem Occured, please retry Again!")
        }
    }

    return (
        <div className="h-full bg-gray-10">
            <div className="p-6 grid gap-6 md:grid-cols-2 lg:grid-cols-3 0" >
                {offers.map((offer) => {
                    let formattedOffer =(DataMapper.mapOfferToEnglish(offer));
                    let offerId =formattedOffer.id;
                    console.log("Offer ID:", offerId);

                    return (

                        <div
                            onClick={() => navigate('/OfferApplications', { state: { offerId: formattedOffer.id } })}
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
                                <p><strong>Salary:</strong> {formattedOffer.salaryMin}MAD - {formattedOffer.salaryMax}MAD</p>
                                <p><strong>Published:</strong> {formattedOffer.publicationDate}</p>
                                <p><strong>Expires:</strong> {formattedOffer.expirationDate}</p>
                            </div>
                            <div className="mt-auto pt-3 flex flex-col justify-center align-middle h-full ">
                                <div className="flex-grow "></div>
                                <button className="w-full bg-blue-500 text-white rounded p-2"
                                        onClick={(e)=>deleteOffer(e, formattedOffer.id)}
                                >
                                    Delete Offer
                                </button>

                            </div>
                        </div>
                    )})}
            </div>
        </div>
    );
};

export default OfferList;
