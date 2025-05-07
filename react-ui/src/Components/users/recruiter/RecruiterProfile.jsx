import React from 'react'
import RecruiterMainHeader from "../../utils/headers/RecruiterMainHeader";
import {Link} from "react-router-dom";
import {MagnifyingGlassIcon} from "@heroicons/react/16/solid";
import {Menu, MenuButton, MenuItem, MenuItems} from "@headlessui/react";
const RecruiterProfile = () => {
  return (
    <div>
    <RecruiterMainHeader/>
        <header className="p-2 bg-blue-500">
            <div className="flex flex-row items-center justify-between">
                <Link to="/MainHome" className="text-white text-2xl pl-4 hover:cursor-pointer"
                      style={{fontFamily:'poppins'}}>
                    Job Seeker
                </Link>
                <Link to="/CompaniesList" className='text-white text-sm p-0 m-0' style={{fontFamily:'poppins'}}>Candidates</Link>
                <Link to="/OffresList"  className='text-white text-sm  p-0 m-0' style={{fontFamily:'poppins'}}>Candidate Applications</Link>



                <div className='flex flex-row justify-between items-center relative'>
                    <input className="py-3 px-3 rounded-full bg-white text-black " onChange={e=>setKeyword(e.target.value)}/>
                    <button className="bg-white text-black border-0 px-2 py-2 text-sm font-serif rounded-full absolute right-20">
                        <MagnifyingGlassIcon className="text-black size-3 "  />
                    </button>
                    <div className="ml-2">
                        <Menu>
                            <MenuButton className=" px-3 py-3 rounded  ">
                                <img src={profilePicture} className="rounded-full w-8 h-8" alt="Profile" />
                            </MenuButton>
                            <MenuItems anchor="bottom end" className=" bg-gray-100 rounded-md shadow-lg p-1 ring-opacity-5 focus:outline-none">
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" >
                                        Change CV
                                    </button>
                                </MenuItem>
                                <hr/>
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" onClick={goProfile} >
                                        Your profile
                                    </button>
                                </MenuItem>
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" >
                                        Saved Job Offers
                                    </button>
                                </MenuItem>
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" >
                                        Your applications
                                    </button>
                                </MenuItem>
                                <MenuItem className="pr-7 mt-2">
                                    <button className="group flex w-full items-center gap-2 data-[focus]:bg-gray-400 p-2 rounded-sm text-black font-serif" onClick={logout} >
                                        Sign Out
                                    </button>
                                </MenuItem>
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
    </div>
  )
}

export default RecruiterProfile
