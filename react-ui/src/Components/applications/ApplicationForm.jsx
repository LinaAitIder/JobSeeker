import CandidateMainHeader from "../utils/headers/CandidateMainHeader";
import Message from "../utils/Message";
import { useLocation } from "react-router-dom";
import {useState} from "react";
import ApplicationService from "../../services/ApplicationService";

export default function ApplicationForm(){
    const location= useLocation();
    const offerId = location.state.offreId;
    const [motivationLetter, setMotivationLetter] = useState(null);
    const [recruiterMessage, setRecruiterMessage] = useState('');
    const [message, setMessage] = useState({})
    async function handleSubmit(e){
        e.preventDefault();
        console.log(motivationLetter.name);
        console.log(recruiterMessage);
        const candidatId = JSON.parse(localStorage.getItem('user'))?.userId;
        const applicationData = new FormData();
        applicationData.append('candidatId', candidatId);
        applicationData.append('offreId', offerId);
        applicationData.append('lettreMotivation', motivationLetter);
        applicationData.append('messageRecruteur', recruiterMessage);
        for (let [key, value] of applicationData.entries()) {
            console.log(`${key}:`, value);
        }
        try {
            const response = await ApplicationService.applyToOfferRequest(applicationData)
            setMessage({type:"success", text:"Your application was sent successfuly!"})

        } catch(error){
            if(error.response.status === 409) {
                setMessage({type:"error", text:"You've already applied to this offer!"})
            } else if(error.response.status === 400) {
                setMessage({type:"error", text:"Only pdf Files are accepted!"})
            } else if (error.response.status === 500) {
                setMessage({ type: "error", text: "A problem occurred on the server. Please try again later!" });
            }else {
                setMessage({type:"error", text:"A problem Occured, please try again later!"})
            }
        }
    };

    function handleChange(e){
        setRecruiterMessage(e.target.value);
    };

    function handleFileChange(e){
        const file = e.target.files[0];
        if(file){
            console.log("file detected!");
            setMotivationLetter(file);
        } else{
            console.log("file not detected!");
        }

    }


    return(
        <>
            <CandidateMainHeader />

            <div className="flex flex-col justify-center place-items-center mt-10">
                <span className="text-2xl text-white m-6" style={{fontFamily:'poppins'}}>Application Form </span>
            <form
                className="max-w-2xl mx-auto p-6 bg-white rounded-2xl shadow-md grid grid-cols-1 gap-4"

                onSubmit={handleSubmit}>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Motivation Letter (PDF)</label>
                    <input
                        type="file"
                        name="lettreMotivation"
                        accept="application/pdf"
                        onChange={handleFileChange}
                        required
                        className="mt-1 block w-full border border-gray-300 rounded p-2"
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Message to Recruiter</label>
                    <textarea
                        name="messageRecruteur"
                        rows="4"
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full border border-gray-300 rounded p-2"
                    ></textarea>
                </div>

                <div className="flex justify-end">
                    <button
                        type="submit"
                        className="bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700"
                    >
                        Submit Application
                    </button>
                </div>

            </form>
                <Message type={message.type} text={message.text} />
            </div>

        </>
    );
}