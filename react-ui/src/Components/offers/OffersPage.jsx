import React, {useEffect, useState} from "react";
import OfferService from "../../services/OfferService";
import OfferList from "./OfferList";
import Message from "../utils/Message";
import CandidateMainHeader from "../utils/headers/CandidateMainHeader";

export default function OffersPage(){
    const [message, setMessage] = useState({
        type:'',
        text:''
    });
    const [offers, setOffers] = useState([]);
    useEffect(()=>{
        const fetchOffers = async()=>{
        try {
            const res = await OfferService.getOffers();

            if(res.status === 200){
                console.log("fetched json offers ", res.data);

                setOffers(res.data);
            }
        } catch(err){
            console.log("A problem occured : ", err.status)
            setMessage({type:"error", text:"A problem Occured, please retry again!"});
        }
    }; fetchOffers()}, []);

    return (
        <div className="bg-white h-full">
            <CandidateMainHeader/>
            <div className="flex flex-col justify-center align-center text-black bg-gray-50">
                <div className="text-2xl text-black m-6 flex justify-center align-center " style={{fontFamily:'poppins'}}>Explore Offers </div>
                <OfferList offers={offers} isCompanyOffers={false} />
            <Message type={message.type} text={message.text} />
        </div>
        </div>
    );

}