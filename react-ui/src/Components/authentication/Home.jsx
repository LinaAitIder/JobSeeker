import React from 'react';
import HomeHeader from '../utils/headers/HomeHeader';
import {useNavigate} from "react-router-dom";
const Home=()=>{
  const navigate = useNavigate();
  function redirectLogin(){
    navigate('/login');
  }
  return (
      <div className= "bg-gradient-to-br from-indigo-100 via-white to-indigo-200 min-h-screen">
        <HomeHeader/>
          <div className="flex flex-col   p-3 md:flex-row place-content-around lg:pr-0 lg:m-20 lg:mt-0">

            <div className="order-1 md:order-2 py-1 px-16 m-10 lg:w-1/2 md:w-1/2 lg:flex-end lg:flex lg:mr-0 lg:pl-40 lg:pr-0 lg:py-0">
              <img src="./assets/searchCandidat.png" className="rounded-full w-screen lg:relative  "/>
            </div>

            <div className="order-2 md:order-1 sm:flex-col sm:flex sm:p-3 sm:m-3 sm:opacity-100 lg:place-content-center lg:mt-0">
              <span className="text-black text-4xl text-center pt-10 pb-5 font-serif sm:text-center sm:pt-10 block ">JobSeeker </span>
              <div className=" m-0 flex flex-col place-content-center align-center px-10 ">
                  <p className="text-black  text-center ">Searching for a Job or an Internship ? </p>
                  <p className="text-black text-center " >Or searching for a Candidate ?   </p>
                  <p className="text-black text-center ">We are here to help you   </p>
              </div>
                <div className="flex justify-center mt-6">
                    <button
                        className="rounded-full bg-blue-500 px-10 py-3 mb-3 text-white font-semibold"
                        onClick={redirectLogin}
                    >
                        Add your CV
                    </button>
                </div>
            </div>

          </div>
        <hr/>

        </div>
  );
}

export default Home;