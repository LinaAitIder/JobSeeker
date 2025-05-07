// File: DataMapper.js

export default class DataMapper {
    static mapRecruiterToFrench(recruiter) {
        return {
            nom: recruiter.firstName,
            prenom: recruiter.lastName,
            entreprise: recruiter.company,
            email: recruiter.email,
            position: recruiter.position,
            telephone: recruiter.phone,
            motDePasse: recruiter.password
        };
    }

    static transformCandidateToEnglish(frenchData) {
        return {
            id: frenchData.id,
            profilePicture: frenchData.photoProfil,
            firstName: frenchData.nom,
            lastName: frenchData.prenom,
            email: frenchData.email,
            phoneNumber: frenchData.telephone,
            city: frenchData.ville,
            country: frenchData.pays,
            cvPath: frenchData.cvPath,
            certifications: frenchData.certifications
        }
    }
}