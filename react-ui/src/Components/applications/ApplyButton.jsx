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
            <button
                onClick={redirectApplicationForm}
                className="px-5 py-2 bg-blue-500 text-white text-sm font-medium rounded-lg hover:bg-blue-500 transition-colors shadow"
            >
                Apply
            </button>
        </>
    );
}