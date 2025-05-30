import React, {useEffect, useState} from "react";
import RecruiterMainHeader from "../../utils/headers/RecruiterMainHeader";
import CandidateService from "../../../services/CandidateService";
import DataMapper from "../../utils/DataMapper";
import Message from "../../utils/Message";


export default function CandidatesList(){
    const [error, setError] = useState('');
    const [candidates, setCandidates] = useState([]);
    const [isloading, setLoading] = useState(false);
    const [imgUrl, setImgUrl] = useState();

        useEffect(()=>{

            fetchCandidates();

        }, []);

        async function fetchCandidates(){
            try{
                const response = await CandidateService.getCandidates();
                console.log(response.data);
                const frenshCandidates = response.data;
                const mappedCandidates =  frenshCandidates.map(frenshCandidate => {
                   return DataMapper.mapCandidateInfoToEnglish(frenshCandidate)
                });
                setCandidates(mappedCandidates);
            } catch(error){
                console.log(error);
                setError("A problem Occured please refresh the page!")
            }
        }
        async function fetchCandidatPProfile(candidateId) {
            await CandidateService.getPProfileRequest(candidateId).then((response)=>{
                const fetchedImageUrl=URL.createObjectURL(response.data);
                console.log("fetched Image :",fetchedImageUrl);
                if(response.status === 200){
                    setImgUrl(fetchedImageUrl);
                }
            }).catch((err)=>{
                console.log(err);
            })
        }

    return (
        <>
        <RecruiterMainHeader/>
            <div className="p-6">
                {error && <Message type="error" text={error}/>}
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                    {candidates.map((candidate) => (
                        <div
                            key={candidate.id}
                            className="bg-white rounded-2xl shadow-md p-4 flex flex-col items-center text-center"
                        >
                            {candidate.profilePicture ? (
                                <img
                                    src={imgUrl}
                                    alt={`${candidate.firstName} ${candidate.lastName}`}
                                    className="w-32 h-32 object-cover rounded-full mb-4"
                                />
                            ) : (
                                    <img
                                        src= "https://www.pngmart.com/files/23/Profile-PNG-Photo.png"
                                        className="w-32 h-32 object-cover rounded-full mb-4"
                                    />
                            )}

                            <h3 className="text-lg font-semibold mb-1">
                                {candidate.firstName} {candidate.lastName}
                            </h3>
                            <p className="text-gray-500 text-sm">{candidate.email}</p>
                            {candidate.phone && <p className="text-gray-500 text-sm">{candidate.phone}</p>}
                            {candidate.position && <p className="text-gray-600 text-sm mt-1">{candidate.position}</p>}
                        </div>
                    ))}
                </div>
            </div>


        </>
    );



}