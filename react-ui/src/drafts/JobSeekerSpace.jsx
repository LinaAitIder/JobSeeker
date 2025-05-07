import React from 'react';
import {RecruiterSignIn} from '../Components/users/recruiter/RecruiterSignIn'
import {RecruiterSignUp} from '../Components/users/recruiter/RecruiterSignUp'
import { Tab, TabGroup, TabList, TabPanel, TabPanels } from '@headlessui/react'
import HomeHeader from '../Components/utils/headers/HomeHeader';

const JobSeekerSpace = () => {


  return (
    <div >
    <HomeHeader/>
    <h3>Candidate Space</h3>
    <div className=" p-10">
    <TabGroup className="bg-gray-200 rounded ">
        <TabList>
            <Tab className='data-[selected]:border-t-2 px-4 mx-6 border-black border-x-3 '>SignIn</Tab>
            <Tab className='data-[selected]:border-t-2 px-4 mx-6 border-black border-x-3 '>SignUp</Tab>
        </TabList>    
        <TabPanels> 
          <TabPanel>
            <RecruiterSignIn />
          </TabPanel>
          <TabPanel>
            <RecruiterSignUp />
          </TabPanel>
        </TabPanels>
    </TabGroup>
    </div>
    </div>
  );
}

export default JobSeekerSpace;