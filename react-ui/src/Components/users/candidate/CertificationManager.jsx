import React, { useEffect, useState } from 'react';
import { ArrowUpTrayIcon, TrashIcon } from '@heroicons/react/24/outline';
import axios from 'axios';
import api from "../../../api/axiosConfig";
import CandidateService from "../../../services/CandidateService";
import Message from "../../utils/Message";
import CVUploader from "../../utils/FileManager/CVUploader";

const CertificateManager = ({ candidateId }) => {
    const [certificates, setCertificates] = useState([]);
    const [fileUrls, setFileUrls] = useState({}); // ADD THIS
    const [certName, setCertName] = useState('');
    const [file, setFile] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [isDeleted, setIsDeleted] = useState(false);
    const [message, setMessage] = useState({
        type : '',
        text: ''
    });


    useEffect(() => {
        if(!candidateId) return;
        fetchCertificates();
    }, [candidateId]);

    const fetchCertificates = async () => {
        try {
            const res = await api.get(`candidat/${candidateId}/certifications`);
            console.log("fetch data : " ,res.status);
            const certs = res.data;
            setCertificates(certs);

            const urls = {};
            await Promise.all(
                certs.map(async (cert) => {
                    try {
                        const fileRes = await api.get(`candidat/${candidateId}/certifications/${cert.id}/file`, {
                            responseType: 'blob',
                        });
                        const blobUrl = URL.createObjectURL(fileRes.data);
                        urls[cert.id] = blobUrl;
                    } catch (err) {
                        console.error(`Error fetching file for cert ${cert.id}:`, err);
                    }
                })
            );

            setFileUrls(urls);
        } catch (err) {
            console.error('Error fetching certificates:', err);
        }
    };


    const handleAdd = async () => {
        if (!certName || !file) {
            setMessage({type:"error", text:"Missing fields, please upload the file or add the name of the certificate!"});
            return;
        }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("nom", certName);

        try {
            setIsLoading(true);
            const res = await CandidateService.addCertificateRequest(formData, candidateId);
            setCertName('');
            setFile(null);
            await fetchCertificates();
        } catch (err) {
            console.error('Erreur:', err.response?.data || err.message);
        } finally {
            setIsLoading(false);
        }
    };

    const handleDelete = async (certificationId) => {
        try{
            const response =  await CandidateService.deleteCertificateRequest(candidateId, certificationId);
            if(response.status === 200 ){
                setCertName('');
                setFile(null);
                setIsDeleted(true);
                await fetchCertificates();
                setMessage({type:"success", text:"Deleted successfully"});

            }
        }catch(error){
            console.log("error", error)
            setMessage({type:"error", text:"a problem occured while trying to delete the certificate!"});
            setIsDeleted(false);
        }
    }


    return (
        <div className="mt-10">
            <h2 className="text-2xl font-semibold text-blue-800 mb-4">Certifications</h2>
        <div className="space-y-4">
                <div className="flex items-center gap-2 w-full">
                    <input
                        type="text"
                        placeholder="Certificate name"
                        value={certName}
                        onChange={(e) => setCertName(e.target.value)}
                        className="border rounded-xl px-3 py-2 w-1/3"
                    />

                    <label className="flex items-center cursor-pointer text-blue-600 hover:text-blue-800">
                        <ArrowUpTrayIcon className="w-5 h-5" />
                        <input
                            type="file"
                            className="hidden"
                            onChange={(e) => setFile(e.target.files[0])}
                        />
                    </label>

                    <button
                        onClick={handleAdd}
                        className="bg-blue-600 text-white rounded-xl px-4 py-2 hover:bg-blue-700 transition disabled:opacity-50"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Uploading...' : 'Add'}
                    </button>
                </div>

                {/* Certificate List */}
                <ul className="space-y-2">
                    {certificates.length === 0 ? (
                        <div className="text-gray-500">No certifications yet.</div>
                    ) : (
                        certificates.map((cert) => (
                            <li
                                key={cert.id}
                                className="flex justify-between items-center p-3 bg-gray-100 rounded-xl"
                            >
                                <a
                                    href={fileUrls[cert.id]}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="text-blue-700 hover:underline"
                                >
                                    {cert.nom}
                                </a>
                                <button
                                    className="text-red-600 hover:text-red-800"
                                    onClick={()=>handleDelete(cert.id)}
                                >
                                    <TrashIcon className="w-5 h-5" />
                                </button>
                            </li>
                        ))
                    )}
                </ul>
                {file &&(
                    <p>File Name : {file.name}</p>
                )}
                <Message type={message.type} text={message.text} timeout={10000} />

            </div>
        </div>
    );
};

export default CertificateManager;
