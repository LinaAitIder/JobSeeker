import {useEffect, useState} from "react";
import OfferService from "../services/OfferService";
import OfferList from "./OfferList";

export default function OffersPage(){

    const [offers, setOffers] = useState([]);
    useEffect(()=>{
        const fetchOffers = async()=>{
        try {
            const res = await OfferService.getOffers();

            if(res.status === 200){
                console.log("fetched json offers ", res.data);

                setOffers(res.data);
                setMessage({type:"success", text:"offers fetched successfully"});
            }
        } catch(err){
            console.log("A problem occured : ", err.status)
            setMessage({type:"error", text:"A problem Occured, please retry again!"});
        }
    }; fetchOffers()}, []);

    return (
        <div className="flex flex-col justify-center align-center text-black">
            <OfferList offers={offers}/>
        </div>
    );

}