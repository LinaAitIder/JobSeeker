import React, {useEffect, useState} from 'react'
import axios from 'axios';
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import {Link, useNavigate} from 'react-router-dom';
import {MagnifyingGlassIcon} from "@heroicons/react/16/solid";


const RecruiterMainHeader = () => {
    const [profilePicture, setProfilePicture] = React.useState('https://www.pngmart.com/files/23/Profile-PNG-Photo.png');
    const Navigate = useNavigate();
    const [keyword, setKeyword]= useState('');


    function logout(){
        localStorage.removeItem("user");
        Navigate("/login");
    }
    function goProfile(){
        Navigate("/recruiterProfile");
    }

    return (
        <header className="p-2 bg-blue-500">
            <div className="flex flex-row items-center justify-between">
                <Link to="/RecruiterMainHome" className="text-white text-2xl pl-4 hover:cursor-pointer"
                      style={{fontFamily:'poppins'}}>
                    Job Seeker
                </Link>
                <Link to="/CompaniesList" className='text-white text-sm p-0 m-0' style={{fontFamily:'poppins'}}>JobSeekers</Link>
                <Link to="/OffresList"  className='text-white text-sm  p-0 m-0' style={{fontFamily:'poppins'}}>Applications</Link>



                <div className='flex flex-row justify-between items-center relative'>
                    <div className="ml-2">
                        <Menu>
                            <MenuButton className=" px-3 py-3 rounded  ">
                                <img src={profilePicture} className="rounded-full w-8 h-8" alt="Profile" />
                            </MenuButton>
                            <MenuItems anchor="bottom end" className=" bg-gray-100 rounded-md shadow-lg p-1 ring-opacity-5 focus:outline-none">
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" onClick={goProfile} >
                                        Your profile
                                    </button>
                                </MenuItem>
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" >
                                       Your Job Offers
                                    </button>
                                </MenuItem>
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" onClick={logout} >
                                        Sign Out
                                    </button>
                                </MenuItem>
                                <hr/>
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" >
                                        About Us
                                    </button>
                                </MenuItem>
                            </MenuItems>
                        </Menu>
                    </div>
                </div>
            </div>
        </header>
    )
}

export default RecruiterMainHeader
