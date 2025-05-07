import React from 'react';
import HomeHeader from '../../utils/headers/HomeHeader';
import {useLocation, useNavigate} from 'react-router-dom';
import {useState, useEffect} from 'react'
import AuthService from '../../services/AuthService';

const NewPassword = () => {
    const [newPassword, setNewPassword] = useState();
    const [confirmedPass, setConfirmedPass] = useState();
    const [alertMessage, setAlertMessage]= useState('');
    const navigate = useNavigate();
    const {state} = useLocation();
    let {email, userRole} = state;
    let userType = userRole;

    //BE SURE THA TI GET INFORMATION USING LOCATION

    useEffect(()=>{
      if(newPassword !== confirmedPass){
        setAlertMessage("the passwords does not match!");
      } else {
        setAlertMessage("");
      }
    }, [newPassword, confirmedPass])
   
    async function updatePassword(){
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
          } else {
            console.log("Bad request!")
          }
        } catch(error){
          console.log("errors:", error);
        }

    }

  return (
    <div>
         <HomeHeader/>
            <form className="flex flex-col justify-center items-center bg-black h-screen pb-12">
                <h1 className="text-2xl font-bold text-white mb-4">Reset Your Password :</h1>
                <div className="m-3">
                    <input type="password" 
                    className="block rounded-md bg-white pl-3 sm:pr-20 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                    placeholder="Enter your new Password"
                    onChange={(e)=>setNewPassword(e.target.value)} />
                 </div>
                 <div className=''>
                    <input type="password" 
                    className="block rounded-md bg-white pl-3 sm:pr-20 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                    placeholder="Confirm your Password"
                    onChange={(e)=>setConfirmedPass(e.target.value)} >
                    </input>
                 </div>
                <button className="px-5 py-2 mt-5 text-white bg-blue-500 rounded" type="button" onClick={updatePassword}>Submit</button>
            </form>
            <p>{alertMessage}</p>
    </div>
  )
}

export default NewPassword
