import React, {useState} from 'react';
import AuthService from "../services/AuthService";
import CandidateService from "../services/CandidateService";
import {useNavigate} from "react-router-dom";
import RecruiterService from "../services/RecruiterService";

export default function AccountManager({connected, onConnectionChange}){
    const [loggedOut, setLoggedOut] = useState(false);
    const navigate = useNavigate();

    function logout(){
        AuthService.logout();
        setLoggedOut(true);
        onConnectionChange(false);
        connected = !loggedOut;
    }

    async function deleteAccount(){
        const user = JSON.parse(localStorage.getItem('user'));
        let userId = user.userId;
        if(user.role === 'CANDIDAT'){
            try{
                const resp = await CandidateService.deleteAccountRequest(userId);
                console.log(resp.status);
                if(resp.status === 204){
                    console.log("Candidate deleted");
                    AuthService.logout();
                    setLoggedOut(true);
                    onConnectionChange(false);
                }else{
                    console.log("ERROR:ACCOUNT_DELETION_FAILED");
                }
            } catch(error){
                console.log(error);
            }
        } else if(user.role === 'RECRUTEUR'){
            try{
                const resp = await RecruiterService.deleteAccountRequest(userId);
                console.log(resp.status);
                if(resp.status === 204){
                    console.log("Recruiter");
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


    }

    return (
        <div className="space-y-6">
            <h2 className="text-2xl font-semibold text-blue-800 mb-4">Manage Account</h2>
            <div className="flex space-x-4 ">
                <button className="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg" onClick={logout}>Logout</button>
                <button className="bg-red-600 hover:bg-red-700 text-white py-2 px-4 rounded-lg" onClick={deleteAccount}>Delete Account</button>
            </div>
        </div>
    )
}
