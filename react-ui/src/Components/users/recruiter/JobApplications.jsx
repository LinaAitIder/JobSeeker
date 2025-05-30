import {useEffect, useState} from "react";
import CandidateService from "../../../services/CandidateService";
import DataMapper from "../../utils/DataMapper";
import RecruiterService from "../../../services/RecruiterService";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import RecruiterMainHeader from "../../utils/headers/RecruiterMainHeader";
import ApplicationService from "../../../services/ApplicationService";


export default function JobApplications(){
    const [applicationsReceived, setApplicationsReceived] = useState([]);
    const recruiterId = JSON.parse(localStorage.getItem("user")).userId;
    console.log(recruiterId);
    useEffect(()=>{

        fetchAllApplicationsToRecruiter();

    }, []);

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
    async function  fetchAllApplicationsToRecruiter(){
        try{
            const response = await RecruiterService.fetchAllApplicationsToRecruiterRequest(recruiterId);
            console.log(response.data);
            const frenshApplications = response.data;
            const mappedApplications =  frenshApplications.map(frenshApplication => {
                return DataMapper.mapApplicationToEnglish(frenshApplication)
            });
            console.log(mappedApplications);
            setApplicationsReceived(mappedApplications);
        } catch(error){
            console.log(error);
        }
    }

    const fomattingDateUntilNow = (applicationDate)=>{
        const startDate = dayjs(applicationDate);
        dayjs.extend(relativeTime)
        return startDate.fromNow();
    }


    return(
        <>
            <RecruiterMainHeader/>
            <div className="p-6 space-y-4">
                {applicationsReceived.length === 0 ? (
                    <p className="text-gray-500">No applications found.</p>
                ) : (
                    applicationsReceived.map((app, index) => (
                        <div key={index} className="bg-white shadow-md rounded-xl p-6 border border-gray-200">
                            <div className="flex items-center justify-between mb-2">
                                <h2 className="text-lg font-semibold text-gray-800">Application</h2>
                            </div>

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
                        </div>
                    ))
                )}
            </div>
        </>
    );


}