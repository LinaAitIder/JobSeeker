import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../../services/AuthService';
import HomeHeader from '../../utils/headers/HomeHeader';
import Message from "../../utils/Message";


export const CandidateSignUp = () => {
  const [nom, setNom] = useState('');
  const [prenom, setPrenom]= useState('');
  const [email, setEmail]=useState('');
  const[motdepasse, setPassword] = useState('');
  const [telephone, setPhone] = useState('');
  const [message,setMessage] = useState(''); // HERE THE MESSAGE TO KNOW ABOUT THE STATE OF THE REGISTRATION
  const navigate = useNavigate();
  const [error, setError] = useState(false);


  const handleRegister = async(e)=>{
    //console.log('debugbefore');
    e.preventDefault();
    //console.log('debugafter');
    try{
      //console.log('debug');
      const user ={
        nom:nom.trim(),
        prenom: prenom.trim(),
        email: email.trim(),
        motDePasse:motdepasse.trim(),
        telephone:telephone.trim()};
      console.log('Sending :',user);
      try{
        const response = await AuthService.registerCandidat(user);
        setMessage(response.data);
        if(response.status === 201){
          //redirect to login
          navigate('/login')
        } else if(response.status===409){
          document.getElementById("error-msg").className="text-black bg-red-400 border p-4 m-4 text-center";
          setMessage('Email already exists');
        } else {
          document.getElementById("error-msg").className="text-black bg-red-400 border p-4 m-4 text-center";
          setMessage('Server error');
        }
      }catch(error){
        setError(true);
        console.error("Full error:", {
          status: error.response?.status,
          data: error.response?.data,
          request: error.config?.data
        });
        setMessage("An error occured! please verify your informations. ")
      }


    }catch(error){
      setError(true);
      console.error('Registration error:', error);
      document.getElementById("error-msg").className="text-black bg-red-400 border p-4 m-4 text-center";
      setMessage('Registration failed');
    }
  }

  return (
      <>
        <HomeHeader/>
        <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-white to-indigo-200 flex items-center justify-center py-16 px-4 pt-0 sm:px-6 lg:px-8">
          <div className="bg-white shadow-2xl rounded-2xl  w-full max-w-md">
          <form data-testid="signUpForm" className='flex justify-center align-center p-12 flex-col h-full sm:mx-auto sm:w-full s sm:max-w-sm pb-6'>
            <h1 className=" text-2xl/9 font-bold tracking-tight text-gray-900 mb-1 text-center font-serif">Candidate's space</h1>

            <h1 className=" text-sm tracking-tight text-gray-900 mb-4 text-center font-serif">Sign Up</h1>
            <div className="mt-2">
              <label htmlFor="nom" className="block text-sm font-medium text-gray-700 p-1" >First Name</label>
                <input
                    id="first name"
                    name="first name"
                    type="text"
                    required
                    className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    onChange={(e)=>setNom(e.target.value)}
                />
            </div>
            <div className="mt-2">
              <label htmlFor="prenom" className="block text-sm font-medium text-gray-700 p-1">Last Name</label>
                <input
                    id="last name"
                    name="last name"
                    type="text"
                    required
                    autoComplete="last name"
                    className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    onChange={(e)=>setPrenom(e.target.value)}
                />
            </div>
            <div className="mt-2">
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 p-1">Email</label>
                <input
                    id="email"
                    name="email"
                    type="text"
                    required
                    autoComplete="email"
                    className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    onChange={(e)=>setEmail(e.target.value)}
                />
            </div>
            <div className="mt-2">
              <label htmlFor="motdepasse" className="block text-sm font-medium text-gray-700 p-1">Password</label>
                <input
                    id="motdepasse"
                    name="motdepasse"
                    type="password"
                    required
                    autoComplete="motdepasse"
                    className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                    onChange={(e)=>setPassword(e.target.value)}
                />
              <div>
              </div>
              <div className="mt-2">
                <label htmlFor="telephone" className="block text-sm font-medium text-gray-700 p-1">Phone</label>
                  <input
                      id="telephone"
                      name="telephone"
                      type="text"
                      required
                      autoComplete="telephone"
                      className="block w-full rounded-md border border-gray-200 bg-white p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                      onChange={(e)=>setPhone(e.target.value)}
                  />
              </div>
            </div>
            <button className="w-full flex justify-center py-2 px-4 my-8 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500" type="button" onClick={handleRegister}>Submit</button>
          </form>
            <div className="flex justify-center place-content-center ">
              {error && <Message type="error" text={message}/>}
            </div>
        </div>


        </div>
      </>
  )
}

export default CandidateSignUp;

