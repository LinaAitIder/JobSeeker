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
            mappedData.sort((a, b) => new Date(b.applyDate) - new Date(a.applyDate));

            setApplications(mappedData);
            console.log(mappedData)

        }catch(error){
            console.error("error while fethching data :", error);
        }
    }

    const changeStatusApplication = async(applicationId, newStatus)=>{
        console.log("The application id :", applicationId);
        console.log("the new status ", newStatus);
        try {
            const res = await ApplicationService.updateApplicationStatusRequest(applicationId, newStatus);
            setApplications(prevApps =>
                prevApps.map(app =>
                    app.id === applicationId ? { ...app, status: newStatus } : app
                )
            );

        } catch(error){
          console.log(error.response.data.message || error.message || "Unknown error occured")

        }

    }

    return(
        <div  className="flex flex-col min-h-screen">
            <RecruiterMainHeader />
            {applications.length === 0 ? (
                <div className="bg-gray-50 h-screen w-full  flex items-center justify-center">
                <span className="text-gray-500 text-center">No applications for your offer yet.</span>
                </div>
            ) : (
                <div className="bg-white h-full p-10 ">
                    <div className="m-10 ml-0 ">
                        <span className="font-semibold text-2xl p-2 mb-2 pb-4">Offer Applications :</span>

                        <hr className="m-4" />
                    </div>
                    { applications.map((app, index)=>{
                return (
                        <div key={index} className="bg-white  mb-4 shadow-md rounded-xl p-6 border border-gray-200">

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
                                    className="text-blue-500 text-sm underline font-semibold"
                                >
                                    Click to view motivation letter
                                </button>
                            </div>
                            <div className="flex flex-row ">
                                <div className="rounded-lg bg-gradient-to-br from-emerald-50 to-green-50 border border-emerald-200 hover:border-emerald-300 shadow-sm m-2 transition-all duration-200">
                                    <button
                                        onClick={() => changeStatusApplication(app.id, updatedStatus.ACCEPTED)}
                                        className="w-full px-4 py-2.5 text-emerald-700 hover:text-white hover:bg-emerald-600 transition-all duration-200 flex items-center justify-center gap-2 font-medium text-sm"
                                    >
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                                            <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                                        </svg>
                                        Accept Application
                                    </button>
                                </div>
                                <div className="rounded-lg bg-white border border-gray-200 hover:border-red-300 m-2 transition-all duration-200 overflow-hidden group ">
                                    <button onClick={()=> changeStatusApplication(app.id, updatedStatus.REJECTED)} className="w-full px-4 py-2 text-gray-700 hover:text-white group-hover:bg-red-500 transition-all duration-200 flex items-center justify-center gap-2 font-medium text-sm" >
                                     <span className="relative">
                                         <span className="absolute inset-0 scale-0 group-hover:scale-100 bg-red-500 rounded-full transition-transform duration-200"></span>
                                          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 relative z-10" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                                          </svg>
                                     </span>
                                        <span className="relative z-10">Reject</span>
                                    </button>
                                </div>
                            </div>


                        </div> )
                    })}
                    </div>
            )})

        </div>
    );
}