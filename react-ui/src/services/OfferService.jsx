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

    //searchOffers
    searchOffer(keyword){

    }


    //deleteOffers
    deleteOffer(offerId){

    }


}
export default new OfferService();