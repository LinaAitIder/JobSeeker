import {React, useEffect, useState} from 'react'
import { useNavigate} from 'react-router-dom';
import AuthService from '../services/AuthService';
import HomeHeader from '../utils/headers/HomeHeader';
import CandidateService from "../services/CandidateService";
import RecruiterService from "../services/RecruiterService";

export const Login = () => {
  const [email, setEmail] = useState('');
  const [motDePasse ,setPassword] = useState('');
  const navigate = useNavigate();
  const [message, setMessage]= useState('');

  useEffect(() => {

  }, []);

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
        navigate('/candidateProfile');
      } else if(response.status === 200 && response.data.role==='RECRUTEUR'){
        localStorage.setItem('user', JSON.stringify(response.data));
        if(localStorage.getItem('user')){
          console.log(localStorage.getItem('user'));
        }
        await getInitialUserData(response.data.id,response.data.role);
        console.log("User Type : Recruiter")
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
    <HomeHeader/>
    <div className="flex min-h-screen flex-1 flex-col justify-start px-20 py-16 lg:px-3 bg-black  ">
      <div className="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 className="mt-0 text-center text-2xl/9 font-bold tracking-tight text-white">
           Sign in to your account
        </h2>
      </div>
      <div className='flex justify-center items-center mt-5'
      >

      </div>
      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form onSubmit={handleSignIn} className="space-y-6" >
          <div>
            <label htmlFor="email" className="block text-sm/6 font-medium text-white">
              Email address
            </label>
            <div className="mt-2">
              <input
                id="email"
                name="email"
                type="email"
                required
                autoComplete="email"
                className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                onChange={(e)=>{
                  setEmail(e.target.value)
                }}
              />
            </div>
          </div>

          <div>
            <div className="flex items-center justify-between">
              <label htmlFor="password" className="block text-sm/6 font-medium text-white">
                Password
              </label>
              <div className="text-sm">
                <a href="/passwordForgotten" className="font-semibold text-indigo-600 hover:text-indigo-500">
                  Forgot password?
                </a>
              </div>
            </div>
            <div className="mt-2">
              <input
                id="motDepasse"
                name="motDepasse"
                type="password"
                required
                autoComplete="current-password"
                className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                 onChange={(e)=>{
                  setPassword(e.target.value)
                }}
              />
            </div>
          </div>

          <div>
            <button
              type="submit"
              className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              Sign in
            </button>
          </div>
        </form>
        <div  id="error-msg">
          {message}
        </div>
      </div>
    </div>
  </>
  )
}
export default Login;
