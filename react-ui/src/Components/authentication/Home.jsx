import React from 'react';
import HomeHeader from '../utils/headers/HomeHeader';
const Home=()=>{
  return (
      <div className="bg-black h-screen ">
        <HomeHeader/>
        <div className="flex flex-row justify-between h-1/2 w-full bg-black">
          <div class="flex flex-col bg-black w-3/6 p-3">
            <span className="text-white text-4xl text-center pt-20 pb-5 font-serif">JobSeeker </span>
            <div className="pl-10 m-0">
              <p className="text-white italic">Searching for a Job/Internship in morocco?</p>
              <p className="text-white italic" >Or searching for a Candidat/Profile?</p>
              <p className="text-white italic">We are here to help you</p>
            </div>
            <div className="pt-0 pb-0 px-4 m-10">
              <img src="./assets/searchCandidat.png" className="rounded-full p"/>
            </div>
          </div>
          <div className="flex flex-col  w-3/6  m-6 p-8 bg-white justify-start rounded-full ">
            <img src="./assets/hire.png" className=" rounded-full pt-8"/>
            <br/>
            <br/>
            <br/>
            <img src="./assets/searchJob.png" className="rounded-full pt-8"/>
          </div>
        </div>
      </div>
  );
}

export default Home;