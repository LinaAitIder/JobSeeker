import api from "../api/axiosConfig";

class ApplicationService{

    applyToOfferRequest(applicationData){
        try{
            return api.post(`/candidature`, applicationData);
        } catch(err){
            console.log(err);
        }
    }

    getApplicationsByOfferRequest(offerId){
        try{
            return api.get(`candidature/offre/${offerId}`);
        } catch(err){
             throw new Error(err);
        }
    }

    updateApplicationStatusRequest(applicationId, newStatut){
        try{
            return api.patch(`candidature/${applicationId}/statut`, null, {
                params: {
                    newStatut: newStatut
                }});
        } catch(err){
             throw new Error(err);
        }
    }

    deleteApplicationRequest(applicationId){
        try{
            return api.delete(`candidature/${applicationId}`);
        } catch(err){
             throw new Error(err);
        }
    }

    getMotivationLetterRequest(applicationId){
        try{
            return api.get(`candidature/${applicationId}/lettre-motivation`, {
                responseType:'blob'
            });
        } catch(err){
            throw new Error(err);
        }
    }



}

export default new ApplicationService();