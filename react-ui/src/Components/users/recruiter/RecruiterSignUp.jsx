import React, {useState} from 'react';
import { useNavigate, Link } from 'react-router-dom';
import AuthService from '../../services/AuthService';
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
    try{
      const response = await AuthService.registerRecruiter(recruiterData);
      setMessage(response.data);
      if(response.status === 200 || 201){
        navigate('/login')
      }
    }catch(error){
      setError(true);
      if(error.response.status === 409){
        setError(true);
        setMessage('This Email Already Exists');
      } else {
        setError(true);
        setMessage(error.response.data.message);
      }

    }
  }

  return (
    <>
    <HomeHeader/>
    <div className="h-full bg-black p-0 pt-3 ">
      <form className='flex justify-center place-content-center p-8 flex-col sm:mx-auto sm:w-full s sm:max-w-sm pb-1'>
      {message}
      <h1 className=" text-2xl/9 font-bold tracking-tight text-white mb-4 text-center font-serif">Organisation's Space</h1>
      <h1 className=" text-xl font-bold tracking-tight text-white mb-4 text-center font-serif">Sign Up</h1>
      <div>
        <label htmlFor="firstName" className="text-white" >First Name</label>
        <div>
          <input
            id="first name"
            name="first name"
            type="text"
            required
            className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            onChange={(e)=>setFirstName(e.target.value)}
          />
        </div>
      </div>
      <div>
        <label htmlFor="lastName" className="text-white">Last Name</label>
        <div>
          <input
            id="last name"
            name="last name"
            type="text"
            required
            autoComplete="last name"
            className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            onChange={(e)=>setLastName(e.target.value)}
          />
        </div>
      </div>
      <div>
        <label htmlFor="company" className="text-white" >Company</label>
        <div>
          <input
            id="company"
            name="company"
            type="text"
            required
            className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            onChange={(e)=>setCompany(e.target.value)}
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
        <label htmlFor="position" className="text-white">Position</label>
        <div>
          <input
            id="position"
            name="position"
            type="text"
            required
            autoComplete="position"
            className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            onChange={(e)=>setPosition(e.target.value)}
          />
        </div>
      </div>       
      <div>
        <label htmlFor="password" className="text-white">Password</label>
        <div>
          <input
            id="password"
            name="password"
            type="password"
            required
            autoComplete="password"
            className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            onChange={(e)=>setPassword(e.target.value)}
          />
        </div>
        <div>
      </div>
      <div>
        <label htmlFor="phone" className="text-white">Phone</label>
        <div>
          <input
            id="phone"
            name="phone"
            type="text"
            required
            autoComplete="phone"
            className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            onChange={(e)=>setPhone(e.target.value)}
          />
        </div>
      </div>
      </div>
      <button className="p-3 mt-5 text-white bg-blue-500 rounded" type="submit" onClick={handleRegister}>Submit</button>

      </form>
      <div className="flex justify-center place-content-center ">
        {error && <Message type="error" text={message}/>}
      </div>


    </div>

    </>
  )
}

export default RecruiterSignUp;

