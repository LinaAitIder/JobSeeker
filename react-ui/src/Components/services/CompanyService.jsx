import axios from "axios";
import api from "../../api/axiosConfig";

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
             return api.get(`/api/entreprises/${companyId}/logo`,{
                 responseType:"blob",
             });
         } catch(err){
             console.log(err);
         }
    }

    fetchCompany(companyId){
        try{
            return  api.get(`/entreprises`);
        }catch(error){
            console.log(error);
            return error;
        }
    }A


    //Search Companies
}

export default new CompanyService();