import api from "../../api/axiosConfig";

class OfferService{

    getOffers(){
        try {
            return api.get(`/offres`);
        }catch(err){

        }
    }
}
export default new OfferService();