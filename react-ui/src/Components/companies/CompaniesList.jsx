import React from 'react';
import CandidateMainHeader from '../utils/headers/CandidateMainHeader';
import CompanyService from "../services/CompanyService";

// Class Component
export class CompaniesList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            companies: [],
            error: null
        };
        this.formatCompanyData = this.formatCompanyData.bind(this);
    }

    async componentDidMount() {
        try {
            const response = await CompanyService.fetchCompanies();
            if (response.status === 200) {
                const formattedCompanies = this.formatCompanyData(response.data);
                this.setState({ companies: formattedCompanies });
            } else {
                throw new Error(`API returned status ${response.status}`);
            }
        } catch (error) {
            console.error('Fetch error', error);
            this.setState({
                error: 'Failed to load companies. Please try again later.'
            });
        }
    }

    formatCompanyData(apiData) {
        return apiData.map(company => ({
            id: company.id,
            nom: company.nom,
            description: company.description,
            location: company.location,
            taille: company.taille,
            domaine: company.domaine,
            logo_path: company.logoPath
        }));
    }

    render() {
        const { companies, error } = this.state;

        if (error) {
            return (
                <div>
                    <CandidateMainHeader />
                    <div className="p-6 text-center bg-red-100 text-red-800 rounded shadow-sm mt-10">
                        {error}
                    </div>
                </div>
            );
        }

        return (
            <div className="h-full bg-black">
                <CandidateMainHeader />
                <div className="text-center mt-10 text-2xl font-semibold">All Organizations</div>

                <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 p-6 mt-6  bg-black">
                    {companies.length > 0 ? (
                        companies.map((company, index) => (
                            <div
                                key={index}
                                className="bg-white dark:bg-gray-800 shadow-md rounded-2xl overflow-hidden hover:scale-105 transition-transform duration-200"
                            >
                                <div className="w-full h-48 bg-gray-100 flex items-center justify-center">
                                    <img
                                        src={company.logo_path || ""}
                                        alt="Company Logo"
                                        className="max-h-full max-w-full object-contain"
                                        onError={(e) => e.target.src = "/placeholder-logo.png"}
                                    />
                                </div>
                                <div className="p-4 space-y-2">
                                    <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                                        {company.nom}
                                    </h3>
                                    <p className="text-sm text-gray-600 dark:text-gray-300">
                                        {company.description?.length > 100
                                            ? company.description.slice(0, 100) + "..."
                                            : company.description}
                                    </p>
                                    <div className="flex items-center gap-2 text-gray-500 dark:text-gray-400 text-sm">
                                        <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none"
                                             viewBox="0 0 24 24" stroke="currentColor">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                                  d="M17.657 16.657L13.414 12.414m0 0L9.172 8.172a4 4 0 115.656 5.656z"/>
                                        </svg>
                                        <span>{company.location || "Unknown location"}</span>
                                    </div>
                                    <div className="flex items-center gap-2 text-gray-500 dark:text-gray-400 text-sm">
                                        <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none"
                                             viewBox="0 0 24 24" stroke="currentColor">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                                  d="M12 14l9-5-9-5-9 5 9 5zm0 0v6"/>
                                        </svg>
                                        <span>{company.domaine || "Unspecified domain"}</span>
                                    </div>
                                    <div className="mt-3">
                                        <a
                                            href="#"
                                            className="inline-block px-4 py-2 bg-blue-600 text-white text-sm rounded hover:bg-blue-700"
                                        >
                                            View Profile
                                        </a>
                                    </div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className="text-center text-gray-500 col-span-full">No organizations available yet.</div>
                    )}
                </div>
            </div>
        );
    }
}
