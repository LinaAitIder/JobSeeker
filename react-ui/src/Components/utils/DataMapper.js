
export default class DataMapper {
    static mapRecruiterToFrench(recruiter) {
        return {
            nom: recruiter.firstName,
            prenom: recruiter.lastName,
            entrepriseNom: recruiter.company,
            email: recruiter.email,
            photoProfilPath: recruiter. photoProfilPath,
            position: recruiter.position,
            telephone: recruiter.phone,
        };
    }

    static mapRecruiterToEnglish(recruiter) {
        return {
            firstName: recruiter.nom,
            lastName: recruiter.prenom,
            company: recruiter.entrepriseNom,
            email: recruiter.email,
            position: recruiter.position,
            phone: recruiter.telephone,
            photoProfilPath: recruiter. photoProfilPath

        };
    }


    static mapOfferToFrench(offer) {
        return {
            id : offer.id,
            titre: offer.title,
            datePublication: offer.publicationDate,
            dateExpiration: offer.expirationDate,
            description: offer.description,
            domaine: offer.domain,
            pays: offer.country,
            ville: offer.city,
            salaireMin: offer.salaryMin,
            salaireMax: offer.salaryMax,
            typeContrat: offer.contractType
        };
    }

    static mapOfferToEnglish(offer) {
        return {
            id : offer.id,
            title: offer.titre,
            publicationDate: offer.datePublication,
            expirationDate: offer.dateExpiration,
            description: offer.description,
            domain: offer.domaine,
            country: offer.pays,
            city: offer.ville,
            salaryMin: offer.salaireMin,
            salaryMax: offer.salaireMax,
            contractType: offer.typeContrat
        };
    }

    static mapCandidateInfoToEnglish(frenchData) {
        return {
            id: frenchData.id,
            profilePicture: frenchData.photoProfil,
            firstName: frenchData.nom,
            lastName: frenchData.prenom,
            email: frenchData.email,
            phoneNumber: frenchData.telephone,
            city: frenchData.ville,
            country: frenchData.pays
        }
    }

    static mapCandidateToEnglish(frenchData) {
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
    static mapCandidateToFrench(englishData) {
        return {
            id: englishData.id,
            photoProfil: englishData.profilePicture,
            nom: englishData.firstName,
            prenom: englishData.lastName,
            email: englishData.email,
            telephone: englishData.phoneNumber,
            ville: englishData.city,
            pays: englishData.country,
            cvPath: englishData.cvPath,
            certifications: englishData.certifications
        };
    }


    static mapApplicationToEnglish(frenshApplication){
        return {
            applyDate: frenshApplication.datePostulation,
            recruiterMessage: frenshApplication.messageRecruteur,
            status: frenshApplication.statut,
            motivationLetterPath: frenshApplication.lettreMotivationPath,
        };
    }

    static mapCompanyToEnglish(frenshCompanyData){
        return{
            id:frenshCompanyData.id,
            name: frenshCompanyData.nom,
            description:frenshCompanyData.description,
            location: frenshCompanyData.location,
            size: frenshCompanyData.taille,
            domain: frenshCompanyData.domaine,
            logoPath: frenshCompanyData.logoUrl
        }
    }

}