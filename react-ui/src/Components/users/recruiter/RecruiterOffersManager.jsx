import {useEffect, useState} from "react";
import OfferService from "../../../services/OfferService";
import OfferList from "../../offers/OfferList";
import Message from "../../utils/Message";
import RecruiterService from "../../../services/RecruiterService";
import RecruiterOfferList from "./RecruiterOfferList";

const USER_ID = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';
const RECRUITER_ID=JSON.parse(localStorage.getItem('recruiter'))
export default function RecruiterOffersManager({recruiterId}){
    const [message, setMessage] = useState({
        type:'',
        text:''
    });
    const [offers, setOffers] = useState([]);
    useEffect(()=>{
        const fetchOffers = async()=>{
            try {
                console.log("USER id :",USER_ID);
                console.log("recruteur id :", RECRUITER_ID);
                const res = await RecruiterService.getOffersRequest(recruiterId);

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
        <div className="flex flex-col justify-center align-center text-black">
            <RecruiterOfferList offers={offers}/>
            <Message type={message.type} text={message.text} />
        </div>
    );

}