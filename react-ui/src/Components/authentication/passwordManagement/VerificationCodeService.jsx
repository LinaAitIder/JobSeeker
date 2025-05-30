import React from 'react';
import {useState} from 'react';
import HomeHeader from '../../utils/headers/HomeHeader';
import { useLocation, useNavigate } from 'react-router-dom';


const VerificationCodeService = () => {
    const [verifCode, setVerifCode] = useState();
    const {state} = useLocation();
    const navigate = useNavigate();
    let {code, email, data} = state;
    const [message, setMessage] = useState('');

    function validateCode(){
        console.log(email);
        console.log(data.role);
        console.log(code);
        console.log(verifCode);
        if (String(code) === String(verifCode)){
            navigate("/newPasswordPage", {state:{email:email, userRole:data.role}});
        } else {
            console.log("please enter the right verification Code!");
            setMessage("please enter the right code !")
        }
    }
  return (
    <div>
    <HomeHeader/>
    <form className="flex flex-col justify-center items-center h-screen  bg-gradient-to-br from-indigo-100 via-white to-indigo-200 pb-12">
        <h1 className="text-2xl font-bold text-black mb-4">Verification Code</h1>
        <p className="text-gray-600 italic mb-4">Please enter your verification Code.</p>
        <input type="email" 
         className="block w-80 rounded-md border border-gray-400 bg-white pl-3 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
         placeholder="Enter the verification code" 
         onChange={(e)=>setVerifCode(e.target.value)}/>
        <button className="px-5 py-2 mt-5 text-white bg-blue-500 rounded" type="button" onClick={validateCode}>Submit</button>

    </form>
    <p className='text-white'>{message}</p>
</div>
  )
}

export default VerificationCodeService
