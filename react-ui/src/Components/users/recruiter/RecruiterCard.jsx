import React from 'react';

const RecruiterCard = ({ recruiter }) => {
    return (
        <div className="flex items-center gap-4">
            <img
                src={`http://localhost:8080/api/photos/${recruiter.profilePhotoPath}`}
                alt="Recruiter Profile"
                className="w-12 h-12 rounded-full object-cover"
            />
            <div>
                <p className="font-medium text-gray-800">{recruiter.firstName} {recruiter.lastName}</p>
                <p className="text-sm text-gray-600">{recruiter.position}</p>
                <p className="text-xs text-gray-500">{recruiter.email}</p>
            </div>
        </div>
    );
};

export default RecruiterCard;
