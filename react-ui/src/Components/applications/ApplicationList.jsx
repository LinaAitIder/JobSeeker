import { useEffect, useState } from "react";
import api from "../../api/axiosConfig";

export default function ApplicationList() {
    const candidateId = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';
    const [applications, setApplications] = useState([]);

    useEffect(() => {
        if (!candidateId) return;

        const fetchApplications = async () => {
            try {
                const res = await api.get(`/candidat/${candidateId}/candidature`);

                const mappedData = res.data.map(app => ({
                    applyDate: app.datePostulation,
                    recruiterMessage: app.messageRecruteur,
                    status: app.statut,
                    motivationLetterPath: app.lettreMotivationPath,
                }));

                setApplications(mappedData);
                if(res.status === 200 || 201){
                    console.log("successfuly Fetched");
                }
            } catch (err) {
                console.error("Error fetching applications:", err);
            }
        };

        fetchApplications();
    }, [candidateId]);



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
                            <p>
                                <span className="font-medium">Message to recruiter:</span>{" "}
                                {app.recruiterMessage || "None"}
                            </p>
                            <p className="mt-2">
                            </p>
                        </div>

                        <div className="flex justify-between items-center">
                            {app.motivationLetterPath && (
                                <>
                                    {/*Will Display more information in the next commits*/}
                                </>
                            )}
                        </div>
                    </div>
                ))
            )}
        </div>
    );
}
