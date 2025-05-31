import { useLocation } from "react-router-dom";
import RecruiterMainHeader from "../../utils/headers/RecruiterMainHeader";

export default function CandidateProfilePage() {
    const location = useLocation();
    const { candidateData } = location.state || {};

    if (!candidateData) {
        return <div>No candidate data available</div>;
    }

    return (
        <>
            <RecruiterMainHeader />
            <div className="p-6">
                <h1 className="text-2xl font-bold mb-6">Candidate Profile</h1>

                <div className="bg-white shadow-md rounded-xl p-6">
                    <div className="flex items-center space-x-4 mb-6">
                        {candidateData.photoProfilPath && (
                            <img
                                src={candidateData.photoProfilPath}
                                alt="Profile"
                                className="w-20 h-20 rounded-full object-cover"
                            />
                        )}
                        <div>
                            <h2 className="text-xl font-semibold">
                                {candidateData.prenom} {candidateData.nom}
                            </h2>
                            <p className="text-gray-600">{candidateData.email}</p>
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div className="space-y-4">
                            <h3 className="font-medium text-lg">Personal Information</h3>
                            <div className="space-y-2">
                                <p><span className="font-medium">Phone:</span> {candidateData.telephone}</p>
                                <p><span className="font-medium">Location:</span> {candidateData.ville}, {candidateData.pays}</p>
                            </div>
                        </div>

                        <div className="space-y-4">
                            <h3 className="font-medium text-lg">Professional Information</h3>
                            {candidateData.cvPath && (
                                <a
                                    href={candidateData.cvPath}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="text-blue-500 underline"
                                >
                                    View CV
                                </a>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}