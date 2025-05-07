import React, {useState} from 'react';
import AuthService from "../../services/AuthService";
import CandidateService from "../../services/CandidateService";

export default function AccountManager({connected, onConnectionChange}){
    const [loggedOut, setLoggedOut] = useState(false);
    function logout(){
        AuthService.logout();
        setLoggedOut(true);
        onConnectionChange(false);
        connected = !loggedOut;
    }
    async function deleteCandidatAccount(){
        let userId = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:''; //handle the error that will rise form backend when request with a userId=''
        try{
            const resp = await CandidateService.deleteAccountRequest(userId);
            console.log(resp.status);
            if(resp.status === 204){
                console.log("I passed the 204 condition status");
                AuthService.logout();
                setLoggedOut(true);
                onConnectionChange(false);
            }else{
                console.log("ERROR:ACCOUNT_DELETION_FAILED");
            }
        } catch(error){
            console.log(error);
        }
    }

    return (
        <div className="space-y-6">
            <h2 className="text-2xl font-semibold text-blue-800 mb-4">Manage Account</h2>
            <div className="flex space-x-4 ">
                <button className="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg" onClick={logout}>Logout</button>
                <button className="bg-red-600 hover:bg-red-700 text-white py-2 px-4 rounded-lg" onClick={deleteCandidatAccount}>Delete Account</button>
            </div>
        </div>
    )
}
