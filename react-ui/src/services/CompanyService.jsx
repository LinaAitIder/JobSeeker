import axios from "axios";
import api from "../api/axiosConfig";

class CompanyService {
    //Extracting CompaniesList Info
     fetchCompanies(){
        try{
            return  api.get(`/entreprises`);
        }catch(error){
            console.log(error);
            return error;
        }
    }

    getLogoCompanyRequest(companyId){
         try {
             return api.get(`entreprises/${companyId}/logo`,{
                 responseType:"blob",
             });
         } catch(err){
             console.log(err);
         }
    }

    fetchCompany(companyId){
        try{
            return  api.get(`entreprises/${companyId}`);
        }catch(error){
            console.log(error);
        }
    }

    fetchCompanyOffers(companyId) {
         try{
             return api.get(`offres/entreprise/${companyId}`);
         }catch(err){
             console.log(err);

         }
    }
}

export default new CompanyService();