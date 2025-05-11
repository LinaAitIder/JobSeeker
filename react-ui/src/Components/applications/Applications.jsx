import CandidateMainHeader from "../utils/headers/CandidateMainHeader";
import ApplicationList from "./ApplicationList";

const USER_ID = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';

export default function Applications(){

    return(
        <>
            <CandidateMainHeader/>
            <ApplicationList candidateId={USER_ID}/>
        </>
    );
}