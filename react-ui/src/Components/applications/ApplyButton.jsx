import {useNavigate} from "react-router-dom";
import React, {useState} from "react";


export default function ApplyButton ({offerId}){
    const navigate = useNavigate();
    const data = {offreId : offerId};
    console.log(offerId);
    console.log(data.offreId);

    function redirectApplicationForm(){

        navigate('/ApplicationForm',  {state: data});
    }
    return(
        <>
            <button className="w-full bg-blue-400 rounded-xl" onClick={redirectApplicationForm}>Apply</button>
        </>
    );
}