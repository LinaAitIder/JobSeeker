import { useEffect, useState } from "react";
import axios from "axios"; // Use axios instead of the default api import
import api from "../../api/axiosConfig";

export default function ApplicationList({ candidateId }) {
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
            } catch (err) {
                console.error("Error fetching applications:", err);
            }
        };

        fetchApplications();
    }, [candidateId]);

    const getStatusStyle = (status) => {
        switch (status) {
            case "Acceptée":
                return "bg-green-200 text-green-800";
            case "Refusée":
                return "bg-red-200 text-red-800";
            case "En attente":
                return "bg-yellow-200 text-yellow-800";
            default:
                return "bg-gray-200 text-gray-800";
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const options = { year: 'numeric', month: 'long', day: 'numeric' };
        return new Date(dateString).toLocaleDateString(undefined, options);
    };

    const fetchMotivationLetter = async (url) => {
        try {
            const response = await axios.get(url, { responseType: 'arraybuffer' });
            const blob = new Blob([response.data], { type: 'application/pdf' });
            return URL.createObjectURL(blob);
        } catch (error) {
            console.error('Error fetching motivation letter PDF:', error);
        }
    };

    return (
        <div className="p-6 space-y-4">
            {applications.length === 0 ? (
                <p className="text-gray-500">No applications found.</p>
            ) : (
                applications.map((app, index) => (
                    <div key={index} className="bg-white shadow-md rounded-xl p-6 border border-gray-200">
                        <div className="flex items-center justify-between mb-2">
                            <h2 className="text-lg font-semibold text-gray-800">Application</h2>
                            <span className="text-sm text-gray-500">{formatDate(app.applyDate)}</span>
                        </div>

                        <div className="text-sm text-gray-700 mb-4">
                            <p>
                                <span className="font-medium">Message to recruiter:</span>{" "}
                                {app.recruiterMessage || "None"}
                            </p>
                            <p className="mt-2">
                                <span
                                    className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${getStatusStyle(app.status)}`}
                                >
                                    {app.status}
                                </span>
                            </p>
                        </div>

                        <div className="flex justify-between items-center">
                            {app.motivationLetterPath && (
                                <>
                                    <a
                                        href="#"
                                        onClick={async (e) => {
                                            e.preventDefault();
                                            const motivationLetterUrl = await fetchMotivationLetter(app.motivationLetterPath);
                                            window.open(motivationLetterUrl, "_blank");
                                        }}
                                        className="text-sm text-blue-600 underline"
                                    >
                                        View motivation letter
                                    </a>
                                </>
                            )}
                        </div>
                    </div>
                ))
            )}
        </div>
    );
}
