import api from "../../api/axiosConfig";

class RecruiterService{

    deleteAccountRequest(recruiterId){
        console.log("localstorage"+localStorage.getItem('user'));
        console.log("userId"+recruiterId);
        try{
            return api.delete(`/candidat/${recruiterId}`);

        } catch(error){
            console.log(error);
            return {
                error : 'failed to delete account',
            }
        }

    }

    updateRecruiterRequest(updatedCandidate, recruiterId){
        //let updatedData = { updatedCandidate : upa}
        try{
            return  api.put(`/candidat/${recruiterId}/profile`, updatedCandidate);
        }catch(err){
            console.log(err);
            return err;
        }

    }

    uploadCompanyLogoRequest(formData, recruiterId){
        try{
            return api.post(`candidat/${recruiterId}/cv/initial`, formData)
        }catch(err){
            return err;
        }

    }

    updateCompanyLogoRequest(formData, recruiterId){
        try{
            return api.patch(`candidat/${recruiterId}/cv`, formData)
        }catch(err){
            return err;
        }

    }

    addCertificateRequest(CertificateFile, recruiterId , name){
        try{
            return api.post(`candidat/${recruiterId}/certifications`, CertificateFile, name);
        }catch(err){
            return err;
        }
    }

    deleteCertificateRequest(Certificate, recruiterId, certificationId){
        try{
            return api.delete(`candidat/${recruiterId}/certifications/${certificationId}`)
        }catch(err){
            return err;
        }
    }


}

export default new CandidateService();