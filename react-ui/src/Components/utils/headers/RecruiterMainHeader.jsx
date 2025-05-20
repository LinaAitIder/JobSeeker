
import React, {useEffect, useState} from 'react';
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react';
import { Link, useNavigate } from 'react-router-dom';
import { MagnifyingGlassIcon, Bars3Icon, XMarkIcon } from "@heroicons/react/24/solid";
import AuthService from "../../../services/AuthService";
import CandidateService from "../../../services/CandidateService";
import RecruiterService from "../../../services/RecruiterService";
import {PlusIcon} from "@heroicons/react/16/solid";

const USER_ID = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';

const CandidateMainHeader = () => {
    const [profilePicture, setProfilePicture] = useState('https://www.pngmart.com/files/23/Profile-PNG-Photo.png');
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const navigate = useNavigate();
    function navigateToOfferForm(){
        navigate('/offerForm');
    }
    console.log("user id",USER_ID)

    useEffect(() => {
        const fetchPP= async () =>{
            try {
                console.log(USER_ID);
                const response =await RecruiterService.getPProfileRequest(USER_ID);
                if(response.status === 200){
                    const fetchedImageUrl=URL.createObjectURL(response.data);
                    console.log("fetched Image in Main Header :",fetchedImageUrl);
                    setProfilePicture(fetchedImageUrl);
                }else {
                    console.warn("Unexpected response status:", response.status);
                }
            } catch(err){
                console.error(err);
            }

        };
        fetchPP();

    }, []);

    function logout() {
        AuthService.logout();
        navigate("/login");
    }
    function goProfile() {
        navigate("/recruiterProfile");
    }
    function Applications() {
        navigate("/JobApplications");
    }
    function postOffer(){
        navigate("/OfferForm");
    }


    return (
        <header className="bg-blue-500 p-4">
            <div className="flex items-center justify-between">
                <div className="flex items-center relative w-screen">
                    <Link to="/RecruiterMainHome" className="text-white text-2xl" style={{ fontFamily: 'poppins' }}>
                        Job Seeker
                    </Link>
                    <button className="text-white ml-4 lg:hidden absolute right-0" onClick={() => setIsMenuOpen(!isMenuOpen)}>
                        {isMenuOpen ? <XMarkIcon className="w-6 h-6" /> : <Bars3Icon className="w-6 h-6" />}
                    </button>
                </div>
                <nav className="hidden lg:flex items-center justify-between space-x-10">
                    <Link to="/CandidatesList" className="text-white text-sm" style={{ fontFamily: 'poppins' }}>Candidates</Link>
                    <Link to="/JobApplications" className="text-white text-sm" style={{ fontFamily: 'poppins' }}>Applications</Link>

                    <div className="p-1 relative group" >
                        <button  onClick={navigateToOfferForm} className="bg-blue-500 w-10 h-10 flex items-center justify-center text-white rounded-full hover:bg-blue-600">
                            <PlusIcon className="w-5 h-5" />
                        </button>
                        <div className="absolute left-12 top-1/2 -translate-x--2 px-2 py-1 bg-gray-700 text-white text-xs rounded opacity-0 group-hover:opacity-100 transition-opacity duration-200 whitespace-nowrap z-10">
                            Add Offer
                        </div>
                    </div>

                    <Menu>
                        <MenuButton className="w-10 h-10 ">
                            <img src={profilePicture} className="rounded-full w-10 h-10 object-cover " alt="Profile"  />
                        </MenuButton>
                        <MenuItems anchor="bottom end" className="bg-white rounded-md shadow-lg p-2 mt-2 space-y-2">
                            <MenuItem>
                                <button className="w-full text-left p-2 hover:bg-gray-200 rounded">Create Offer</button>
                            </MenuItem>
                            <MenuItem>
                                <button onClick={goProfile} className="w-full text-left p-2 hover:bg-gray-200 rounded">Your Profile</button>
                            </MenuItem>
                            <MenuItem>
                                <button onClick={Applications} className="w-full text-left p-2 hover:bg-gray-200 rounded">Job Applications</button>
                            </MenuItem>
                            <MenuItem>
                                <button onClick={logout} className="w-full text-left p-2 hover:bg-gray-200 rounded">Sign Out</button>
                            </MenuItem>
                            <MenuItem>
                                <button className="w-full text-left p-2 hover:bg-gray-200 rounded">About Us</button>
                            </MenuItem>
                        </MenuItems>
                    </Menu>
                </nav>
            </div>

            {isMenuOpen && (

                <div className="lg:hidden mt-4 space-y-2 flex flex-col">
                    <button onClick={postOffer} className="w-full text-white text-left p-2 hover:bg-gray-400 rounded">Post Offer</button>
                    <hr/>
                    <button onClick={Applications} className="w-full text-white text-left p-2 hover:bg-gray-400 rounded">Applications</button>
                    <Link to="/CandidatesList" className="w-full text-left p-2 text-white hover:bg-gray-400 rounded" onClick={() => setIsMenuOpen(false)}>Candidates</Link>
                    <hr/>

                    <Link to="/RecruiterOffersPage" className="w-full text-left p-2  text-white hover:bg-gray-400 rounded"  onClick={() => setIsMenuOpen(false)}>My Offers</Link>
                    <div className="flex flex-col gap-2 mt-2">
                        <button onClick={goProfile} className="w-full text-left p-2  text-white hover:bg-gray-400 rounded">Your Profile</button>
                        <button onClick={logout} className="w-full text-left p-2  text-white hover:bg-gray-400 rounded">Sign Out</button>
                        <hr/>
                        <button className="w-full text-left p-2 text-white hover:bg-gray-400 rounded">About Us</button>
                    </div>
                </div>
            )}
        </header>
    );
};

export default CandidateMainHeader;
