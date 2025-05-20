import {useEffect, useState} from "react";
import ApplicationService from "../../services/ApplicationService";
import RecruiterMainHeader from "../utils/headers/RecruiterMainHeader";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import DataMapper from "../utils/DataMapper";
import {useLocation} from "react-router-dom";

export default function OfferApplications(){
    //Status enum
    const  updatedStatus= {
        PENDING: "EN_ATTENTE",
        ACCEPTED: "ACCEPTEE",
        REJECTED: "REFUSEE",
    }

    const location = useLocation()
    const { offerId } = location.state || {};
    console.log("this is the offer id :", offerId);
    const [applications, setApplications] = useState([]);
    //fetching all the appliction of an offer
    useEffect( () => {
         fetchApplicationOffer();

    }, [])

    const fomattingDateUntilNow = (applicationDate)=>{
        const startDate = dayjs(applicationDate);
        dayjs.extend(relativeTime)
        return startDate.fromNow();
    }

    const fetchMotivationLetter = async(applicationId) =>{
        try{
            const response = await ApplicationService.getMotivationLetterRequest(applicationId);
            if(response.status === 200){
                const motivationLetterFile = new Blob([response.data], { type: 'application/pdf' });
                const motivationLetterUrl = URL.createObjectURL(motivationLetterFile);
                console.log(motivationLetterUrl);
                window.open(motivationLetterUrl);
            } else {
                console.warn("Unexpected response status:", response.status);
            }
        } catch(err){
            console.error(err);
        }
    }

    async function fetchApplicationOffer() {
        try {
            const response = await ApplicationService.getApplicationsByOfferRequest(offerId)
            const mappedData = response.data.map(app => (
                DataMapper.mapApplicationToEnglish(app)
            ));
            setApplications(mappedData);


        }catch(error){
            console.error("error while fethching data :", error);
        }
    }

    const changeStatusApplication = async(applicationId, newStatus)=>{
        console.log("The application id :", applicationId);
        console.log("the new status ", newStatus);
        try {
            const res = await ApplicationService.updateApplicationStatusRequest(applicationId, newStatus);

        } catch(error){
          console.log(error.response.data.message || error.message || "Unknown error occured")

        }

    }

    return(
        <>
            <RecruiterMainHeader/>
            {/*Display the card Offer*/}
            {applications.length === 0 ? (
                <p>No applications for your offer yet.</p>
            ) : (

                applications.map((app, index)=>{
                return (
                    <>
                        <div key={index} className="bg-white shadow-md rounded-xl p-6 border border-gray-200">

                            <div className="text-sm text-gray-700 mb-4">

                                <div>
                                    <span className="font-medium">Message to recruiter : </span>
                                    <span className="text-wrap">{app.recruiterMessage || "None"}</span>
                                </div>
                                <p className="mt-2">
                                    <span className="font-small font-medium">Applied : </span>
                                    {
                                        fomattingDateUntilNow(app.applyDate)
                                    }
                                </p>
                                <p className="mt-2">
                                    <span className="font-small font-medium">Status :</span> {app.status}
                                </p>
                            </div>

                            <div className="flex justify-between items-center">
                                <button
                                    onClick={() => fetchMotivationLetter(app.id)}
                                    className="text-blue-500 text-sm underline"
                                >
                                    Click to view motivation letter
                                </button>
                            </div>
                            <button onClick={()=> changeStatusApplication(app.id, updatedStatus.ACCEPTED)}>Accept Application</button>
                            <br/>
                            <button onClick={()=> changeStatusApplication(app.id, updatedStatus.REJECTED)}>Reject Application</button>

                        </div>

                    </>
                );
                    }))

                })

        </>
    );
}