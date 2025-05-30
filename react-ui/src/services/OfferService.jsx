import api from "../api/axiosConfig";


class OfferService{


    getOffers(){
        try {
            return api.get(`/offres`);
        }catch(err){

        }
    }

    //Still Not used
    getOffersByCompany(){

    }

    async checkCV(candidateId) {
        try {
            const response = await api.get(`/offres/has-cv/${candidateId}`);
            return response.data;
        } catch (error) {
            if (error.response?.status === 401) {
                throw new Error("Please login to continue");
            }
            if (error.response?.status === 404) {
                return false; // Treat as no CV exists
            }
            console.error("CV check failed:", error);
            throw new Error("Failed to check CV status");
        }
    }

    async getRecommendedOffers(candidateId) {
        try {
            const response = await api.get(`/offres/recommended/${candidateId}`);
            return response.data || [];
        } catch (error) {
            if (error.response?.status === 401) {
                throw new Error("Session expired. Please login again");
            }
            if (error.response?.status === 404) {
                return [];
            }
            if (error.response?.status === 412) {
                throw new Error("Please upload a CV first");
            }
            console.error("Recommendation error:", error);
            throw new Error(error.response?.data?.message || "Unable to load recommendations");
        }
    }

    //searchOffers
    searchOffer(keyword){

    }


    //deleteOffers
    deleteOffer(offerId){

    }


}
export default new OfferService();