import api from "../../api/axiosConfig";

class RecruiterService{

    deleteAccountRequest(recruiterId){
        console.log("localstorage"+localStorage.getItem('user'));
        console.log("userId"+recruiterId);
        try{
            return api.delete(`/recruteur/${recruiterId}`); // need to make sure of this api

        } catch(error){
            console.log(error);
            return {
                error : 'failed to delete account',
            }
        }

    }

    getRecruiterProfileRequest(recruiterId){
        try{
            return  api.get(`/recruteur/${recruiterId}`);
        }catch(err){
            console.log(err);
            return err;
        }
    }

    updateRecruiterRequest(updatedRecruiter, recruiterId){
        try{
            return  api.put(`/recruteur/${recruiterId}`, updatedRecruiter);
        }catch(err){
            console.log(err);
            return err;
        }

    }

    updatePhotoProfileRequest(formData, recruiterId){
        try{
            return api.post(`recruteur/${recruiterId}/photo`, formData)
        }catch(err){
            return err;
        }

    }

    addOfferRequest(offerData, recruiterId){
        try{
            return api.post(`recruteur/${recruiterId}/offre`, offerData);
        }catch(err){
            return err;
        }
    }

    getOffersRequest(recruiterId){
        try{
            return api.get(`recruteur/${recruiterId}/offre`);
        }catch(err){
            return err;
        }
    }

   
    getApplicationsOfferRequest(offerId, status){
        try{
            return api.get(`recruteur/offres/${offerId}/candidatures`);
        }catch(err){
            return err;
        }
    }

    deleteOfferRequest(Certificate, recruiterId, certificationId){
        try{
            return api.delete(`candidat/${recruiterId}/certifications/${certificationId}`)
        }catch(err){
            return err;
        }
    }


}

export default new RecruiterService();