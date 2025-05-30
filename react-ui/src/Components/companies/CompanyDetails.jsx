import {useLocation, useParams} from "react-router-dom";
import CandidateMainHeader from "../utils/headers/CandidateMainHeader";
import RecruiterCard from "../users/recruiter/RecruiterCard";
import CompanyInfoManager from "./CompanyInfoManager";
import CompanyService from "../../services/CompanyService";
import {useState} from "react";
import CompanyCard from "./CompanyCard";
import OfferList from "../offers/OfferList";
import DataMapper from "../utils/DataMapper";


export default function CompanyDetails(){
    const { companyId } = useParams();
    const [company, setCompany] = useState({});
    const [companyOffers, setCompanyOffers] = useState([]);

    console.log('companyId :',companyId);

    useState(()=>{

        getCompanyInfo();
        getCompanyOffers();
    }, [])

    //Get Company Informations
    async function getCompanyInfo(){
        try {
            const response = await CompanyService.fetchCompany(companyId);
            console.log("company Info received :", response.data);
            const formmatedCompany = DataMapper.mapCompanyToEnglish(response.data);
            setCompany(formmatedCompany);
            console.log("Company Data", company);
        } catch (err){
            console.log(err);
        }
    }

    // Get Company Offers
    async function getCompanyOffers(){
        try{
            const response = await CompanyService.fetchCompanyOffers(companyId);
            console.log("offers received :", response.data);
            const offers = response.data;
            setCompanyOffers(offers);
        } catch(err) {
            console.log(err)
        }

    }


    return(
        <div className="bg-gray-100 h-full ">
            <CandidateMainHeader/>
            <CompanyCard company={company}/>
            <OfferList offers={companyOffers} isCompanyOffers={true}/>

        </div>
    );



}