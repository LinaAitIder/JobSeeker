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
    <form className="flex flex-col justify-center items-center bg-black h-screen pb-12">
        <h1 className="text-2xl font-bold text-white mb-4">Verification Code</h1>
        <p className="text-white mb-4">Please enter your verification Code.</p>
        <input type="email" 
         className="block rounded-md bg-white pl-3 sm:pr-20 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
         placeholder="Enter the verification code" 
         onChange={(e)=>setVerifCode(e.target.value)}/>
        <button className="px-5 py-2 mt-5 text-white bg-blue-500 rounded" type="button" onClick={validateCode}>Submit</button>

    </form>
    <p className='text-white'>{message}</p>
</div>
  )
}

export default VerificationCodeService
