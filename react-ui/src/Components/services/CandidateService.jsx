//Here we need to manage user services

import {useNavigate} from "react-router-dom";
import axios from "axios";
import api from "../../api/axiosConfig";
import {editableInputTypes} from "@testing-library/user-event/dist/utils";
const API_BASE_URL = process.env.REACT_APP_API_URL;

class CandidateService{

    deleteAccountRequest(candidatId){
         console.log("localstorage"+localStorage.getItem('user'));
        console.log("userId"+candidatId);
        try{
           return api.delete(`/candidat/${candidatId}`);

        } catch(error){
            console.log(error);
            return {
              error : 'failed to delete account',
            }
        }

    }

    updateCandidateRequest(updatedCandidate, candidatId){
        //let updatedData = { updatedCandidate : upa}
         try{
             return  api.put(`/candidat/${candidatId}/profile`, updatedCandidate);
         }catch(err){
             console.log(err);
             return err;
         }

    }

    //this one should receive the file multipartFile
    uploadCvRequest(formData, candidatId){
        try{
            return api.post(`candidat/${candidatId}/cv/initial`, formData)
        }catch(err){
            return err;
        }

    }

    //This one should receive also the multipartFile
    updateCvRequest(formData, candidatId){
        try{
            return api.patch(`candidat/${candidatId}/cv`, formData)
        }catch(err){
            return err;
        }

    }

    getCvRequest(candidatId){
        try{
            return api.get(`candidat/${candidatId}/cv`, {
                responseType:'blob'
            });
        }catch(err){
            return err;
        }

    }
    //here i need to pass the certificate file and the name of the file
    //I need to send the file "file" and the other param as nom
    addCertificateRequest(CertificateFile, candidatId , name){
        try{
            return api.post(`candidat/${candidatId}/certifications`, CertificateFile, name);
        }catch(err){
            return err;
        }
    }

    deleteCertificateRequest(Certificate, candidatId, certificationId){
        try{
            return api.delete(`candidat/${candidatId}/certifications/${certificationId}`)
        }catch(err){
            return err;
        }
    }


}

export default new CandidateService();