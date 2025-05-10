//Here we need to manage user services

import api from "../../api/axiosConfig";

class CandidateService{

    getCandidateProfileRequest(candidatId){
        try {
            return api.get(`/candidat/${candidatId}`);
        } catch(error){
            console.error(error);
        }
    }

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
         try{
             return  api.put(`/candidat/${candidatId}/profile`, updatedCandidate , {
                 headers: {
                     'Content-Type': 'multipart/form-data'
                 }
             });
         }catch(err){
             console.log(err);
             return err;
         }

    }

    getPProfileRequest(candidatId){
        try{
            return  api.get(`/candidat/${candidatId}/photo`, {responseType: 'blob'});
        }catch(err){
            console.log(err);
            return err;
        }
    }

    updatePProfileRequest(candidatId, imageFile){
        try{
            return  api.post(`/candidat/${candidatId}/photo`,imageFile);
        }catch(err){
            console.log(err);
            return err;
        }
    }

    uploadCvRequest(formData, candidatId){
        try{
            return api.post(`candidat/${candidatId}/cv/initial`, formData)
        }catch(err){
            return err;
        }

    }

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

    addCertificateRequest(CertificateData, candidateId){
        try{
            return api.post(`candidat/${candidateId}/certifications`, CertificateData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
        }catch(err){
            return err;
        }
    }

    deleteCertificateRequest(candidatId, certificationId){
        try{
            return api.delete(`candidat/${candidatId}/certifications/${certificationId}`)
        }catch(err){
            return err;
        }
    }





}

export default new CandidateService();