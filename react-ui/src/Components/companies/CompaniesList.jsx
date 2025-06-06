import React from 'react';
import CandidateMainHeader from '../utils/headers/CandidateMainHeader';
import CompanyService from "../../services/CompanyService";
import DataMapper from "../utils/DataMapper";

export class CompaniesList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            companies: [],
            logoUrl : [],
            error: null
        };
        this.formatCompanyData = this.formatCompanyData.bind(this);
        this.handleDetailsRedirection = this.handleDetailsRedirection.bind(this);
    }

    async componentDidMount() {
        try {
            const response = await CompanyService.fetchCompanies();
            if (response.status === 200) {
                const formattedCompanies = this.formatCompanyData(response.data);
                //debugging command- review fetched companies data
                console.log(response.data);
                console.log(formattedCompanies);
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
        return apiData.map(company => (
                DataMapper.mapCompanyToEnglish(company)
        ));

    }

    handleDetailsRedirection(companyId){
        window.location.href = `/CompanyDetails/${companyId}`;
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
            <div className="h-full bg-gray-50">
                <CandidateMainHeader />
                <div className="text-center mt-10  text-2xl font-semibold">All Organizations</div>

                <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 p-6 mt-4 ">
                    {companies.length > 0 ? (
                        companies.map((company, index) => (
                            <div
                                key={index}
                                className="bg-white dark:bg-gray-800 shadow-md rounded-2xl overflow-hidden hover:scale-105 transition-transform duration-200"
                            >

                                <div className="p-4 space-y-2">
                                    <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                                        {company.name || "Unknown Company"}
                                    </h3>
                                    <p className="text-sm text-gray-600 dark:text-gray-300">
                                        {company.description
                                            ? (company.description.length > 100
                                                ? company.description.slice(0, 100) + "..."
                                                : company.description)
                                            : <span className="text-gray-400 italic">No description</span>}
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
                                        <span>{company.domain || "Unspecified domain"}</span>
                                    </div>
                                    <div className=" h-full mt-auto pt-3 flex flex-col  ">
                                        <div className="flex-grow "></div>

                                        <button
                                            onClick={()=>this.handleDetailsRedirection(company.id)}
                                            className="inline-block px-4 py-2  rounded bg-blue-500 text-white text-sm rounded hover:bg-blue-560"
                                        >
                                            View More Information
                                        </button>
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
