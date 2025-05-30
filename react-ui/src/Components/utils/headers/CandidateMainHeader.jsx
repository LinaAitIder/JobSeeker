
import React, {useEffect, useState} from 'react';
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react';
import { Link, useNavigate } from 'react-router-dom';
import { MagnifyingGlassIcon, Bars3Icon, XMarkIcon } from "@heroicons/react/24/solid";
import AuthService from "../../../services/AuthService";
import CandidateService from "../../../services/CandidateService";
import offerService from "../../../services/OfferService";
import DataMapper from "../DataMapper";
import {BuildingOffice2Icon, RectangleGroupIcon, UserCircleIcon} from "@heroicons/react/16/solid";


const CandidateMainHeader = () => {
    const [profilePicture, setProfilePicture] = useState('https://www.pngmart.com/files/23/Profile-PNG-Photo.png');
    const [keyword, setKeyword] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [showResults, setShowResults] = useState(false);
    const userId =  localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';

    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const navigate = useNavigate();
    console.log("user id",userId)

    useEffect(() => {
        if (!userId) return;

        const fetchPP= async () =>{
            try {
                console.log(userId);
                const response =await CandidateService.getPProfileRequest(userId);
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

    function lightToggle(){

    }

    async function handleSearch(e){
        const searchTerm= e.target.value;
        setKeyword(searchTerm);
        console.log(keyword);

        if(searchTerm.length>2){
            try{
                const res = await offerService.searchOffer(searchTerm);
                const formattedOffers = res.data.map(offer => DataMapper.mapOfferToEnglish(offer));
                console.log("searched Offers :", formattedOffers)
                setSearchResults(formattedOffers || []);
                setShowResults(true);

            }catch(err) {
                console.log(err);
                setSearchResults([]);
                setShowResults(false);
            }
        } else {
            setSearchResults([]);
            setShowResults(false);
        }

    }

    function handleResultClick() {
        setShowResults(false);
        setKeyword('');
    }



    return (
        <header className="bg-blue-500 p-4 relative">
            <div className="flex items-center justify-between">
                <div className="flex items-center relative w-screen">
                    <Link to="/CandidateMainHome" className="text-white text-2xl flex flex-row items-center" style={{ fontFamily: 'poppins' }}>
                        JobSeeker
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
                                className="py-2 px-3 rounded-full bg-white text-black w-64"
                                onChange={handleSearch}
                                value={keyword}
                                placeholder="Search offers..."
                                onFocus={() => {
                                    if (searchResults.length > 0) {
                                        setShowResults(true);
                                    }
                                }}
                                onBlur={() => setTimeout(() => setShowResults(false), 200)}
                            />
                            <button className="absolute right-1 top-2 bg-white text-black p-1 rounded-full">
                                <MagnifyingGlassIcon className="w-4 h-4" />
                            </button>
                            <div className="relative ">
                            {showResults && (
                                <div className="absolute top-full right-10 p-2 mt-2 w-90 bg-white rounded-lg shadow-lg z-50 max-h-96 overflow-y-auto">
                                    {searchResults.length > 0 ? (
                                        searchResults.map((offer) => (
                                            <Link
                                                key={offer.id}
                                                to={`/offer/${offer.id}`}
                                                className="block p-4 hover:bg-gray-100 border-b border-gray-200 last:border-b-0"
                                                onClick={handleResultClick}
                                            >
                                                <h3 className="font-medium text-gray-900">{offer.title}</h3>
                                                <p className="text-sm text-gray-600 truncate">{offer.description}</p>
                                                <div className="flex justify-between mt-1">
                                                    <span className="text-xs text-gray-500">{offer.company}</span>
                                                    <span className="text-xs text-gray-500">{offer.location}</span>
                                                </div>
                                            </Link>
                                        ))
                                    ) : (
                                        <div className="p-4 text-gray-500">No results found for "{keyword}"</div>
                                    )}
                                </div>
                            )}
                            </div>
                        </div>

                    <Menu>
                        <MenuButton className="w-10 h-10 ">
                            <img src={profilePicture} className="rounded-full w-10 h-10 object-cover " alt="Profile"  />
                        </MenuButton>
                        <MenuItems anchor="bottom end" className="bg-white rounded-md shadow-lg p-2 mt-2 space-y-2">
                                <MenuItem>
                                <button onClick={goProfile} className="w-full text-left p-2 hover:bg-gray-200 rounded">Your Profile</button>
                            </MenuItem>
                            <MenuItem>
                                <button onClick={Applications} className="w-full text-left p-2 hover:bg-gray-200 rounded">
                                    Your Applications
                                </button>
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
                    <button onClick={Applications} className="w-full text-left p-2 hover:bg-gray-500 text-white rounded">
                        Applications
                    </button>
                    <Link to="/CompaniesList" className="w-full text-left p-2 hover:bg-gray-500 text-white rounded" onClick={() => setIsMenuOpen(false)}>
                        Companies
                    </Link>
                    <Link to="/OffersPage" className="w-full text-left p-2 hover:bg-gray-500  text-white rounded"  onClick={() => setIsMenuOpen(false)}>
                        Offers
                    </Link>

                    <div className="flex flex-col gap-2 mt-2">
                        <button onClick={goProfile} className="w-full text-left p-2 hover:bg-gray-500 text-white rounded">Your Profile</button>
                        <button onClick={logout} className="w-full text-left p-2 hover:bg-gray-500 text-white rounded">Sign Out</button>

                    </div>
                </div>
            )}
        </header>
    );
};

export default CandidateMainHeader;
