import RecruiterOffersManager from "./RecruiterOffersManager";
import RecruiterMainHeader from "../../utils/headers/RecruiterMainHeader";


const RECRUITER_ID=localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';

export default function RecruiterOffersPage(){
    console.log("This is the recruiter id :",RECRUITER_ID);
    return(
      <>
        <RecruiterMainHeader/>

          <RecruiterOffersManager recruiterId={RECRUITER_ID}/>
      </>
    );
}