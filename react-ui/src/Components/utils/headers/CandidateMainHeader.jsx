
import React, {useEffect, useState} from 'react';
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react';
import { Link, useNavigate } from 'react-router-dom';
import { MagnifyingGlassIcon, Bars3Icon, XMarkIcon } from "@heroicons/react/24/solid";
import AuthService from "../../services/AuthService";
import CandidateService from "../../services/CandidateService";

const USER_ID = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';

const CandidateMainHeader = () => {
    const [profilePicture, setProfilePicture] = useState('https://www.pngmart.com/files/23/Profile-PNG-Photo.png');
    const [keyword, setKeyword] = useState('');
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const navigate = useNavigate();
    console.log("user id",USER_ID)

    useEffect(() => {
        const fetchPP= async () =>{
            try {
                console.log(USER_ID);
                const response =await CandidateService.getPProfileRequest(USER_ID);
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
        navigate("/candidateProfile");
    }
    function Applications() {
        navigate("/Applications");
    }



    return (
        <header className="bg-blue-500 p-4">
            <div className="flex items-center justify-between">
                <div className="flex items-center relative w-screen">
                    <Link to="/MainHome" className="text-white text-2xl" style={{ fontFamily: 'poppins' }}>
                        Job Seeker
                    </Link>
                    <button className="text-white ml-4 lg:hidden absolute right-0" onClick={() => setIsMenuOpen(!isMenuOpen)}>
                        {isMenuOpen ? <XMarkIcon className="w-6 h-6" /> : <Bars3Icon className="w-6 h-6" />}
                    </button>
                </div>
                <nav className="hidden lg:flex items-center justify-between space-x-10">
                    <Link to="/CompaniesList" className="text-white text-sm" style={{ fontFamily: 'poppins' }}>Companies</Link>
                    <Link to="/OffersPage" className="text-white text-sm" style={{ fontFamily: 'poppins' }}>Offers</Link>

                    <div className="relative">
                        <input
                            className="py-2 px-3 rounded-full bg-white text-black"
                            onChange={e => setKeyword(e.target.value)}
                            placeholder="Search..."
                        />
                        <button className="absolute right-1 top-2 bg-white text-black p-1 rounded-full">
                            <MagnifyingGlassIcon className="w-4 h-4" />
                        </button>
                    </div>

                    <Menu>
                        <MenuButton className="w-10 h-10 ">
                            <img src={profilePicture} className="rounded-full w-10 h-10 object-cover " alt="Profile"  />
                        </MenuButton>
                        <MenuItems anchor="bottom end" className="bg-white rounded-md shadow-lg p-2 mt-2 space-y-2">
                                <MenuItem>
                                    <button className="w-full text-left p-2 hover:bg-gray-200 rounded">Change CV</button>
                                </MenuItem>
                                <MenuItem>
                                <button onClick={goProfile} className="w-full text-left p-2 hover:bg-gray-200 rounded">Your Profile</button>
                            </MenuItem>
                            <MenuItem>
                                <button onClick={Applications} className="w-full text-left p-2 hover:bg-gray-200 rounded">Your Applications</button>
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
                    <button onClick={Applications} className="w-full text-left p-2 hover:bg-gray-200 rounded">Applications</button>
                    <Link to="/CompaniesList" className="w-full text-left p-2 hover:bg-gray-200 rounded" onClick={() => setIsMenuOpen(false)}>Companies</Link>
                    <Link to="/OffersPage" className="w-full text-left p-2 hover:bg-gray-200 rounded"  onClick={() => setIsMenuOpen(false)}>Offers</Link>

                    <div className="flex flex-col gap-2 mt-2">
                        <button onClick={goProfile} className="w-full text-left p-2 hover:bg-gray-200 rounded">Your Profile</button>
                        <button onClick={logout} className="w-full text-left p-2 hover:bg-gray-200 rounded">Sign Out</button>

                        <button className="w-full text-left p-2 hover:bg-gray-200 rounded">About Us</button>
                    </div>
                </div>
            )}
        </header>
    );
};

export default CandidateMainHeader;
