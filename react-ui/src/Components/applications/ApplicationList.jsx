import { useEffect, useState } from "react";
import CandidateService from "../services/CandidateService";
import DataMapper from "../utils/DataMapper";
import ApplicationService from "../services/ApplicationService";
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';

export default function ApplicationList({candidateId}) {
    const [applications, setApplications] = useState([]);
    const [motivationLetterPath, setMotivationLetterPath] = useState('');

    const fomattingDateUntilNow = (applicationDate)=>{
        const startDate = dayjs(applicationDate);
        dayjs.extend(relativeTime)
        return startDate.fromNow();
    }


    useEffect(() => {
        if (!candidateId) return;

        const fetchApplications = async () => {
            try {
                const res = await CandidateService.getCandidateCertificatesRequest(candidateId);

                const mappedData = res.data.map(app => (
                        DataMapper.mapApplicationToEnglish(app)
                ));
                console.log(mappedData);
                setApplications(mappedData);
                if(res.status === 200 || 201){
                    console.log("successfuly Fetched");

                }
            } catch (err) {
                console.error("Error fetching applications:", err);
            }
        };



        //fetchMotivationLetter();
        fetchApplications();
    }, [candidateId]);

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


    return (
        <div className="p-6 space-y-4">
            {applications.length === 0 ? (
                <p className="text-gray-500">No applications found.</p>
            ) : (
                applications.map((app, index) => (
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
    );
}
