import React from 'react';
import HomeHeader from '../utils/headers/HomeHeader';
const Home=()=>{
  return (
      <div className="bg-white h-screen ">
        <HomeHeader/>
          <div className="flex flex-col bg-white p-3 sm:flex-row place-content-between">
            <div className="sm:flex-col sm:flex sm:p-3 sm:m-3 sm:opacity-100">
              <span className="text-black text-4xl text-center pt-10 pb-5 font-serif sm:text-center sm:pt-10 block ">JobSeeker </span>
              <div className=" m-0 flex flex-col place-content-center align-center">
                <p className="text-black  text-center ">Searching for a Job or an Internship in Morocco?</p>
                <p className="text-black text-center " >Or searching for a Candidat?</p>
                <p className="text-black text-center ">We are here to help you</p>
              </div>
            </div>
            <div className="py-1 px-16 m-10 lg:w-1/2 ">
              <img src="./assets/searchCandidat.png" className="rounded-full w-screen"/>
            </div>
          </div>
        <hr/>

        </div>
  );
}

export default Home;