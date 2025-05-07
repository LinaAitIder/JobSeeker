import { useEffect, useState } from 'react';
import { ArrowUpTrayIcon, TrashIcon } from '@heroicons/react/24/outline';
import axios from 'axios';
import api from "../../../api/axiosConfig";

const CertificateManager = ({ candidateId }) => {
    const [certificates, setCertificates] = useState([]);
    const [fileUrls, setFileUrls] = useState({}); // ADD THIS
    const [certName, setCertName] = useState('');
    const [file, setFile] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
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
            console.log("Champs manquants");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("nom", certName); //* Changé de "name" à "nom"*

        try {
            setIsLoading(true);
            const res = await api.post(`candidat/${candidateId}/certifications`,
            formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }
        );
            setCertName('');
            setFile(null);
            await fetchCertificates();
        } catch (err) {
            console.error('Erreur:', err.response?.data || err.message);
        } finally {
            setIsLoading(false);
        }
    };
    const handleDelete = async (certificateId) => {
        try {
            await api.delete(`/${candidateId}/certifications/${certificateId}`);
            setCertificates(prev => prev.filter(cert => cert.id !== certificateId));

            // Remove from fileUrls state too
            setFileUrls(prev => {
                const updated = { ...prev };
                delete updated[certificateId];
                return updated;
            });
        } catch (err) {
            console.error('Error deleting certificate:', err);
        }
    };

    return (
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
                                onClick={() => handleDelete(cert.id)}
                                className="text-red-600 hover:text-red-800"
                            >
                                <TrashIcon className="w-5 h-5" />
                            </button>
                        </li>
                    ))
                )}
            </ul>
        </div>
    );
};

export default CertificateManager;
