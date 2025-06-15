import React, {useState} from 'react';
import { useNavigate, Link } from 'react-router-dom';
import AuthService from '../../../services/AuthService';
import HomeHeader from '../../utils/headers/HomeHeader';
import DataMapper from "../../utils/DataMapper";
import Message from "../../utils/Message";

export const RecruiterSignUp = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName]= useState('');
  const [email, setEmail]=useState('');
  const[password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [message,setMessage] = useState(''); // HERE THE MESSAGE TO KNOW ABOUT THE STATE OF THE REGISTRATION
  const [company, setCompany] = useState('');
  const navigate = useNavigate();
  const [position, setPosition] = useState('');
  const [error, setError] = useState(false);

  const handleRegister = async(e)=>{
    e.preventDefault();
    const recruiterData = DataMapper.mapRecruiterToFrench({firstName, lastName, company, email, position, phone ,password});
    console.log(recruiterData)
    try{
      const response = await AuthService.registerRecruiter(recruiterData);
      setMessage(response.data);
        navigate('/login')
    }catch(error){
      setError(true);

      const status = error?.response?.status;
      if(status === 409){
        setError(true);
        setMessage(  'This Email Already Exists');
      } else {
        setError(true);
        if(password.lenght<6) {
          setMessage(error.response.data.message);
        } else {
          setMessage("An error occured! Please review your informations.")
        }

      }

    }
  }

  return (
    <>
    <HomeHeader/>
    <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-white to-indigo-200 flex items-center justify-center py-16 px-4 pt-0 sm:px-6 lg:px-8 ">
      <div className="bg-white shadow-2xl rounded-2xl p-6 m-6  w-full max-w-md">
      <form className='flex justify-center place-content-center p-2 flex-col sm:mx-auto sm:w-full s sm:max-w-sm pb-1'>
      <h1 className=" text-center text-2xl font-bold  mb-1  text-gray-900 font-serif">Organisation's Space</h1>
      <h1 className=" text-center text-sm  text-gray-900 pb-4 font-serif">Sign Up</h1>
      <div className="">
        <label htmlFor="firstName" className="block text-sm font-medium text-gray-700 p-1" >First Name</label>
          <input
            id="first name"
            name="first name"
            type="text"
            required
            className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
            onChange={(e)=>setFirstName(e.target.value)}
          />
      </div>
      <div className="mt-2">
        <label htmlFor="lastName" className="block text-sm font-medium text-gray-700 p-1">Last Name</label>
        <div>
          <input
            id="last name"
            name="last name"
            type="text"
            required
            autoComplete="last name"
            className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
            onChange={(e)=>setLastName(e.target.value)}
          />
        </div>
      </div >
        <div className="mt-2">
          <label htmlFor="phone" className="block text-sm font-medium text-gray-700 p-1">Phone</label>
          <div>
            <input
                id="phone"
                name="phone"
                type="text"
                required
                autoComplete="phone"
                className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                onChange={(e)=>setPhone(e.target.value)}
            />
          </div>
        </div>
        <div className="mt-2">
          <label htmlFor="company" className="block text-sm font-medium text-gray-700 p-1" >Company</label>
          <div>
            <input
                id="company"
                name="company"
                type="text"
                required
                className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
                onChange={(e)=>setCompany(e.target.value)}
            />
          </div>
        </div>
      <div className="mt-2">
        <label htmlFor="position" className="block text-sm font-medium text-gray-700 p-1">Position</label>
        <div>
          <input
            id="position"
            name="position"
            type="text"
            required
            autoComplete="position"
            className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
            onChange={(e)=>setPosition(e.target.value)}
          />
        </div>
      </div>
        <div className="mt-2">
          <label htmlFor="email" className="block text-sm font-medium text-gray-700 p-1">Email</label>
          <div>
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
        </div>
      <div className="mt-2">
        <label htmlFor="password" className="block text-sm font-medium text-gray-700 p-1">Password</label>
        <div>
          <input
            id="password"
            name="password"
            type="password"
            required
            autoComplete="password"
            className="block w-full rounded-md border border-gray-200 bg-white  p-1 pr-4 py-2 text-base text-gray-900 placeholder:text-gray-500 focus:border-indigo-500 focus:ring-2 focus:border-none focus:outline-none focus:ring-indigo-400 sm:text-sm shadow-md"
            onChange={(e)=>setPassword(e.target.value)}
          />
        </div>
        <div>
      </div>
      </div>
      <button className="w-full flex justify-center py-2 px-4 mt-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500" type="submit" onClick={handleRegister}>Submit</button>
        <div className="flex justify-center place-content-center ">
          {error && <Message type="error" text={message}/>}
        </div></form>
      </div>
    </div>

    </>
  )
}

export default RecruiterSignUp;

