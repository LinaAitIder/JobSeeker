import {React, useEffect, useState} from 'react'
import { useNavigate} from 'react-router-dom';
import AuthService from '../../services/AuthService';
import HomeHeader from '../utils/headers/HomeHeader';
import CandidateService from "../../services/CandidateService";
import RecruiterService from "../../services/RecruiterService";

export const Login = () => {
  const [email, setEmail] = useState('');
  const [motDePasse ,setPassword] = useState('');
  const navigate = useNavigate();
  const [message, setMessage]= useState('');


  async function getInitialUserData(userId, userRole){
    if(userRole === 'CANDIDAT'){
      try {
        const response = await CandidateService.getCandidateProfileRequest(userId);
        if(response.status === 200){
          localStorage.setItem('candidat', JSON.stringify(response.data));
        }
      } catch(error){
          console.error(error);
      }
    } else if(userRole === 'RECRUTEUR'){
      try {
        const response = await RecruiterService.getRecruiterProfileRequest(userId);
        if(response.status === 200){
          localStorage.setItem('recruiter', JSON.stringify(response.data));
        }
      } catch(error){
        console.error(error);
      }
    }

  }

  //Handling the login logic
  const handleSignIn = async(e)=>{
    e.preventDefault(); 
    try{
      const response = await AuthService.login({email, motDePasse});
      console.log("email:", email);
      console.log("motDePasse", motDePasse);

      if(response.data.code === 'AUTH_ERROR'){
        setMessage("Email ou mot de passe incorrect");
      }
      if(response.status === 200 && response.data.role=== 'CANDIDAT'){
        localStorage.setItem('user', JSON.stringify(response.data));
        if(localStorage.getItem('user')){
          console.log(localStorage.getItem('user'));
        }
        await getInitialUserData(response.data.id,response.data.role);
        navigate('/CandidateMainHome');
      } else if(response.status === 200 && response.data.role==='RECRUTEUR'){
        localStorage.setItem('user', JSON.stringify(response.data));
        if(localStorage.getItem('user')){
          console.log(localStorage.getItem('user'));
        }
        await getInitialUserData(response.data.id,response.data.role);
        navigate('/recruiterProfile');
      } else {
        setMessage('Invalid credentials');
      }
    } catch(error){
      document.getElementById("error-msg").className="text-black bg-red-400 border p-4 m-4 text-center";
      setMessage('Invalid credentials');


    }
  };

  return (
      <>
        <HomeHeader />
        <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-white to-indigo-200 flex items-center justify-center py-16 px-4 pt-0 sm:px-6 lg:px-8">
          <div className="bg-white shadow-2xl rounded-2xl p-10 w-full max-w-md">
            <h2 className="text-center text-2xl/9 font-bold text-gray-900 font-serif">
              Sign In
            </h2>

            <form onSubmit={handleSignIn} className="mt-8 space-y-6">
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-gray-700 p-1 ">
                  Email address
                </label>
                <input
                    id="email"
                    name="email"
                    type="email"
                    required
                    autoComplete="email"
                    className="block w-full rounded-md border border-gray-200 bg-white  mt-1 p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    onChange={(e) => setEmail(e.target.value)}
                />
              </div>

              <div>
                <label htmlFor="motDepasse" className="block text-sm font-medium text-gray-700 p-1">
                  Password
                </label>
                <input
                    id="motDepasse"
                    name="motDepasse"
                    type="password"
                    required
                    autoComplete="current-password"
                    className="block w-full rounded-md border border-gray-200 bg-white  mt-1 pl-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              <div className="flex items-center justify-between">
                <a
                    href="/passwordForgotten"
                    className="text-sm text-indigo-600 hover:text-indigo-500 font-medium"
                >
                  Forgot password?
                </a>
              </div>

              <button
                  type="submit"
                  className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Sign in
              </button>

              <div id="error-msg" className="text-red-500 text-sm mt-2 text-center">
                {message}
              </div>
            </form>
          </div>
        </div>
      </>

  )
}
export default Login;
