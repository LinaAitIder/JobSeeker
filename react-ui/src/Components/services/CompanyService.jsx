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

    //Search Companies
}

export default new CompanyService();