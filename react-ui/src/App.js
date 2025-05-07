
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './Components/authentication/Home';
import CandidateSignUp from './Components/users/candidate/CandidateSignUp';
import RecuiterSignUp from './Components/users/recruiter/RecruiterSignUp';
import React from 'react';
import {Login} from './Components/authentication/Login';
import CandidateProfile from './Components/users/candidate/CandidateProfile';
import EmailVerification from './Components/authentication/passwordManagement/EmailVerification';
import RecruiterProfile from './Components/users/recruiter/RecruiterProfile';
import NewPassword from './Components/authentication/passwordManagement/NewPassword';
import {CompaniesList} from './Components/companies/CompaniesList';
import OffresList from './Components/offers/OffresList';
import VerificationCodeService from './Components/authentication/passwordManagement/VerificationCodeService';
import ProtectedRoute from './routes/ProtectedRoute';
import AuthRoute from './routes/AuthRoute';
import CandidateMainHome from "./Components/users/candidate/CandidateMainHome";
import {Logout} from "./drafts/Logout"
import MainHome from "./Components/users/candidate/MainHome";

function App() {
  return (
    <BrowserRouter className="bg-black">
      <Routes>
          <Route path="logout" element={<Logout />} />
        {/*Auth Routes  */}
         <Route element={<AuthRoute/>}>
          <Route path="/" element={<Home />} />
          <Route path="/candidatesignup" element={<CandidateSignUp />} />
          <Route path="/recruitersignup" element={<RecuiterSignUp />} />
          <Route path="/login" element={<Login />} />
          <Route path="/passwordForgotten" element={<EmailVerification/>}/>
          <Route path="/newPasswordPage" element={<NewPassword/>}/>
          <Route path="/VerificationCodeService" element={<VerificationCodeService/>}/>
         </Route>

        {/* Protected Routes - Only accessible when logged in  */}

        <Route element={<ProtectedRoute allowedRole='CANDIDAT'/>}>
            <Route path="/MainHome" element={<MainHome />} />

            <Route path="/candidateProfile" element={<CandidateProfile/>}/>
            <Route path="/CandidateMainHome" element={<CandidateMainHome/>} />
            <Route path="/CompaniesList" element={<CompaniesList/>}/>
          <Route path="/OffresList" element={<OffresList/>}/>
        </Route>

        {/* Proctected Routes - Only accessible for the recruiter */}
        <Route element={<ProtectedRoute allowedRole='RECRUTEUR'/>}>
            <Route path="/recruiterProfile" element={<RecruiterProfile/>}/>
        </Route>




      </Routes>


    </BrowserRouter>
  );
}

export default App;
