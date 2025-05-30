import React from 'react';
import HomeHeader from '../../utils/headers/HomeHeader';
import {useLocation, useNavigate} from 'react-router-dom';
import {useState, useEffect} from 'react'
import AuthService from '../../../services/AuthService';
import Message from "../../utils/Message";

const NewPassword = () => {
    const [newPassword, setNewPassword] = useState();
    const [confirmedPass, setConfirmedPass] = useState();
    const [alertMessage, setAlertMessage]= useState('');
    const navigate = useNavigate();
    const {state} = useLocation();
    let {email, userRole} = state;
    let userType = userRole;

    async function updatePassword(){
        if(newPassword !== confirmedPass){
            setAlertMessage("Passwords does not match!");
            return;
        } else {
            setAlertMessage("");
        }
        let credentials = {
            newPassword : newPassword,
            email : email,
            userType:userRole
        }
        console.log(credentials);
        try {
          const response =  await AuthService.updatePassword(credentials);
          if(response.status === 200){
            console.log("Good request!");
            navigate("/login")
          }
        } catch(error){
          console.log("errors:", error);
          if(newPassword.length<6){
              setAlertMessage("Passwords length should be longer than 6 chars!!");
          }
        }

    }

  return (
    <div>
         <HomeHeader/>
            <form className="flex flex-col justify-center items-center  bg-gradient-to-br  from-indigo-100 via-white to-indigo-200  h-screen pb-12">
                <h1 className="text-2xl font-bold text-black mb-4">Reset Your Password :</h1>
                <div className="m-3">
                    <input type="password" 
                    className="block w-80 rounded-md border border-gray-400 bg-white pl-3 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    placeholder="Enter your new Password"
                    onChange={(e)=>setNewPassword(e.target.value)} />
                 </div>
                 <div className=''>
                    <input type="password" 
                    className="block w-80 rounded-md border border-gray-400 bg-white pl-3 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    placeholder="Confirm your Password"
                    onChange={(e)=>setConfirmedPass(e.target.value)} >
                    </input>
                 </div>
                <button className="px-5 py-2 mt-5 text-white bg-blue-500 rounded" type="button" onClick={updatePassword}>Submit</button>
                <Message text={alertMessage} type="error"/>

            </form>
    </div>
  )
}

export default NewPassword
