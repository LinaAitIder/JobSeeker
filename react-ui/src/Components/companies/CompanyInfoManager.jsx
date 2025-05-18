import React from "react";
import api from "../../api/axiosConfig";
import DataMapper from "../utils/DataMapper";
import companyService from "../services/CompanyService";
import CompanyService from "../services/CompanyService";

export default class CompanyInfoManager extends React.Component{
    constructor(props) {
        super(props);
        this.state={
            currStateCompany :{},
            logoUrl : '',
            recruiterId: props.recruiterId
        }
    }

    async componentDidMount() {


        //Loading Company
        this.fetchCompany();
        this.fetchCompanyLogo();


    }
    async fetchCompany(){
        try {
            const response = await CompanyService.fetchCompany(this.state.recruiterId);
            if(response.status === 200){
                console.log(response.data);
                const mappedCompanyData = DataMapper.mapCompanyToEnglish(response.data);
                console.log("english company data :", mappedCompanyData);
                this.setState({
                    currStateCompany : mappedCompanyData,
                })
                console.log(this.state.currStateCompany);
            }
        } catch(err){
            console.error(err);
        }
    }

     async fetchCompanyLogo(){
        try {
            const response = await CompanyService.getLogoCompanyRequest(this.state.currStateCompany.id);
            if(response.status === 200){
                const image = new Blob(response.data);
                this.setState({
                   logoUrl : URL.createObjectURL(image),
                })
                console.log(this.state.logoUrl);

            }
        } catch(err){
            console.error(err);
        }
    }



    render() {
        const {currStateCompany} =  this.state;
        const {logoUrl} = this.state;
        return (
            <>
                <div className="space-y-6" id="section1">
                    <h2 className="text-2xl font-semibold text-blue-800">Company's Information</h2>

                    <div className="m-10 flex flex-col items-center gap-2">
                        <div className="w-32 h-32">
                            <img
                                className="w-full h-full rounded-full object-cover border-4 border-blue-200"
                                src={logoUrl || 'https://www.pngmart.com/files/23/Profile-PNG-Photo.png'}
                                alt="Company Logo"
                            />
                        </div>
                        <p className="text-gray-600 text-sm">Company Logo</p>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-1">Company Name</label>
                        <p className="p-2 border rounded-md bg-gray-50 text-gray-800">{currStateCompany.name}</p>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-1">Description</label>
                        <p className="p-2 border rounded-md bg-gray-50 text-gray-800">{currStateCompany.description}</p>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-1">Location</label>
                        <p className="p-2 border rounded-md bg-gray-50 text-gray-800">{currStateCompany.location}</p>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-1">Size</label>
                        <p className="p-2 border rounded-md bg-gray-50 text-gray-800">{currStateCompany.size}</p>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-1">Domain</label>
                        <p className="p-2 border rounded-md bg-gray-50 text-gray-800">{currStateCompany.domain}</p>
                    </div>
                </div>
            </>

    )
    }
}
