
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
import VerificationCodeService from './Components/authentication/passwordManagement/VerificationCodeService';
import ProtectedRoute from './routes/ProtectedRoute';
import AuthRoute from './routes/AuthRoute';
import CandidateMainHome from "./Components/users/candidate/CandidateMainHome";
import OfferForm from "./Components/offers/OfferForm";
import RecruiterMainHeader from "./Components/utils/headers/RecruiterMainHeader";
import CandidateMainHeader from "./Components/utils/headers/CandidateMainHeader";
import RecruiterMainHome from "./Components/users/recruiter/RecruiterMainHome";
import OfferList from "./Components/offers/OfferList";
import OffersPage from "./Components/offers/OffersPage";
import ApplicationForm from "./Components/applications/ApplicationForm";
import ApplyButton from "./Components/applications/ApplyButton";
import ApplicationList from "./Components/applications/ApplicationList";
import Applications from "./Components/applications/Applications";


function App() {
  return (
    <BrowserRouter className="bg-black">
      <Routes>
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
            <Route path="/candidateProfile" element={<CandidateProfile/>}/>
            <Route path="/CandidateMainHome" element={<CandidateMainHome/>} />
            <Route path="/CompaniesList" element={<CompaniesList/>}/>
           <Route path="/OffersPage" element={<OffersPage/>}/>
            <Route path="/CandidateMainHeader" element={<CandidateMainHeader/>}/>
            <Route path="/ApplicationForm" element={<ApplicationForm/>}/>
            <Route path="/ApplyButton" element={<ApplyButton/>}/>
            <Route path="/ApplicationList" element={<ApplicationList/>}/>
            <Route path="/Applications" element={<Applications/>}/>


        </Route>

        {/* Proctected Routes - Only accessible for the recruiter */}
        <Route element={<ProtectedRoute allowedRole='RECRUTEUR'/>}>
            <Route path="/recruiterProfile" element={<RecruiterProfile/>}/>
            <Route path="/offerForm" element={<OfferForm/>}/>\
            <Route path="/RecruiterMainHome" element={<RecruiterMainHome/>}/>
            <Route path="/RecruiterMainHeader" element={<RecruiterMainHeader/>}/>

        </Route>




      </Routes>


    </BrowserRouter>
  );
}

export default App;
