import CandidateMainHeader from "../utils/headers/CandidateMainHeader";
import ApplicationList from "./ApplicationList";

const USER_ID = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';

export default function Applications(){

    return(
        <div className="bg-white h-full">
            <CandidateMainHeader/>
            <span className="text-black text-2xl text-center font-semibold inline-flex place-content-center w-full m-6 mb-0" >Your Applications</span>
            <ApplicationList candidateId={USER_ID}/>
        </div>
    );
}