import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import HomeHeader from '../../utils/headers/HomeHeader';


export const CandidateSignUp = () => {
  const [nom, setNom] = useState('');
  const [prenom, setPrenom]= useState('');
  const [email, setEmail]=useState('');
  const[motdepasse, setPassword] = useState('');
  const [telephone, setPhone] = useState('');
  const [message,setMessage] = useState(''); // HERE THE MESSAGE TO KNOW ABOUT THE STATE OF THE REGISTRATION
  const navigate = useNavigate();


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
        console.error("Full error:", {
          status: error.response?.status,
          data: error.response?.data,
          request: error.config?.data
        });
      }


    }catch(error){
      console.error('Registration error:', error);
      document.getElementById("error-msg").className="text-black bg-red-400 border p-4 m-4 text-center";
      setMessage('Registration failed');
    }
  }

  return (
      <>
        <HomeHeader/>
        <div className="h-full bg-black">
          <form data-testid="signUpForm" className='flex justify-center align-center p-12 flex-col h-full sm:mx-auto sm:w-full s sm:max-w-sm'>
            {message}
            <h1 className=" text-2xl/9 font-bold tracking-tight text-white mb-4 text-center font-serif">Candidate's space</h1>

            <h1 className=" text-xl font-bold tracking-tight text-white mb-4 text-center font-serif">Sign Up</h1>
            <div className="flex flex-col  ">
              <label htmlFor="nom" className="text-white" >First Name</label>
              <div>
                <input
                    id="first name"
                    name="first name"
                    type="text"
                    required
                    className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                    onChange={(e)=>setNom(e.target.value)}
                />
              </div>
            </div>
            <div>
              <label htmlFor="prenom" className="text-white">Last Name</label>
              <div>
                <input
                    id="last name"
                    name="last name"
                    type="text"
                    required
                    autoComplete="last name"
                    className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                    onChange={(e)=>setPrenom(e.target.value)}
                />
              </div>
            </div>
            <div>

              <label htmlFor="email" className="text-white">Email</label>
              <div>
                <input
                    id="email"
                    name="email"
                    type="text"
                    required
                    autoComplete="email"
                    className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                    onChange={(e)=>setEmail(e.target.value)}
                />
              </div>
            </div>
            <div>
              <label htmlFor="motdepasse" className="text-white">motdepasse</label>
              <div>
                <input
                    id="motdepasse"
                    name="motdepasse"
                    type="password"
                    required
                    autoComplete="motdepasse"
                    className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                    onChange={(e)=>setPassword(e.target.value)}
                />
              </div>
              <div>
              </div>
              <div>
                <label htmlFor="telephone" className="text-white">phone</label>
                <div>
                  <input
                      id="telephone"
                      name="telephone"
                      type="text"
                      required
                      autoComplete="telephone"
                      className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                      onChange={(e)=>setPhone(e.target.value)}
                  />
                </div>
              </div>
            </div>
            <button className="p-3 mt-5 text-white bg-blue-500 rounded" type="button" onClick={handleRegister}>Submit</button>
          </form>
          <div id="error-msg">{message}</div>
        </div>
      </>
  )
}

export default CandidateSignUp;

