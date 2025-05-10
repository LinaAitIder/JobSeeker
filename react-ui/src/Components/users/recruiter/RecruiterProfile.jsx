import React from 'react'
import RecruiterMainHeader from "../../utils/headers/RecruiterMainHeader";
import {Link, useNavigate} from "react-router-dom";
import {MagnifyingGlassIcon} from "@heroicons/react/16/solid";
import {Menu, MenuButton, MenuItem, MenuItems} from "@headlessui/react";
const RecruiterProfile = () => {
    const navigate = useNavigate();
    function navigateToOfferForm(){
        navigate('/offerForm');
    }
  return (
    <div>
    <RecruiterMainHeader/>
         {/*Testing the functionality , design until functionality is working will be improved*/}
        <button className="flex flex-col align-items justify-center bg-blue-500 rounded-2xl" onClick={navigateToOfferForm}> Add Offer </button>
    </div>
  )
}

export default RecruiterProfile
