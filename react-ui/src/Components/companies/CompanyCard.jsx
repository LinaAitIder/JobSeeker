import React from "react";

export default function CompanyCard({ company }) {
    return (
        <div className="flex flex-col justify-center m-6 ">
            <div className="w-full max-w-screen-xl mx-auto my-10 px-4 py-6 bg-white shadow-xl rounded-2xl border border-gray-200">
            <div className="mb-8 text-center">
                <h2 className="text-3xl font-bold text-gray-800">{company.name || 'Unknown Company'} </h2>
                <p className="text-sm text-gray-500 mt-1">An overview of the company details</p>
            </div>

            <div className="space-y-6">
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-sm text-gray-500 mb-1">Company Name</label>
                        <div className="bg-gray-50 p-3 rounded-md text-gray-900 shadow-sm">{company.name}</div>
                    </div>

                    <div>
                        <label className="block text-sm text-gray-500 mb-1">Location</label>
                        <div className="bg-gray-50 p-3 rounded-md text-gray-900 shadow-sm">{company.location}</div>
                    </div>

                    <div>
                        <label className="block text-sm text-gray-500 mb-1">Size</label>
                        <div className="bg-gray-50 p-3 rounded-md text-gray-900 shadow-sm">{company.size}</div>
                    </div>

                    <div>
                        <label className="block text-sm text-gray-500 mb-1">Domain</label>
                        <div className="bg-gray-50 p-3 rounded-md text-gray-900 shadow-sm">{company.domain}</div>
                    </div>
                </div>

                <div>
                    <label className="block text-sm text-gray-500 mb-1">Description</label>
                    <div className="bg-gray-50 p-4 rounded-md text-gray-800 leading-relaxed shadow-sm">
                        {company.description}
                    </div>
                </div>
            </div>
        </div>
        </div>
            );
}
