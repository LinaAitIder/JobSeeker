import React from 'react';

const PersonalInfoForm = ({ candidate, countries, onChange, onFileSelect }) => {
    return (
        <div className="space-y-6">
            <h2 className="text-2xl font-semibold text-blue-800">Personal Information</h2>

            <div className="rounded-full bg-bleu-800 w-50 h-50">
                <label className="block text-gray-700 mb-2">Profile Picture</label>
                <img className="rounded-full bg-blue-500" src={candidate.profilePicture} />
                <input
                    type="file"
                    className="block w-full border p-2 rounded-md bg-amber-400 rounded-full"
                    onChange={e => onFileSelect(e.target.files[0])}
                />
            </div>

            {['firstName', 'lastName', 'city', 'phoneNumber'].map(field => (
                <div key={field}>
                    <label className="block text-gray-700 mb-2">{field.replace(/([A-Z])/g, ' $1').toUpperCase()}</label>
                    <input
                        type="text"
                        className="block w-full border p-2 rounded-md"
                        value={candidate[field] || ''}
                        onChange={e => onChange(field, e.target.value)}
                    />
                </div>
            ))}

            <div>
                <label className="block text-gray-700 mb-2">Country</label>
                <select
                    className="block w-full border p-2 rounded-md"
                    value={candidate.country || ''}
                    onChange={e => onChange('country', e.target.value)}
                >
                    <option>{candidate.country}</option>
                    {countries.map(c => (
                        <option key={c.cca3} value={c.cca3}>
                            {c.name.common}
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
};

export default PersonalInfoForm;
