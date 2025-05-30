import React from "react";
import Message from "../../utils/Message";
import api from "../../../api/axiosConfig";
import DataMapper from "../../utils/DataMapper";
import CandidateService from "../../../services/CandidateService";
import RecruiterService from "../../../services/RecruiterService";



export default class RecruiterInfoManager extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            currStateRecruiter : {},
            connected :true,
            imgUrl :'',
            recruiterId : props.recruiterId,
            successfulMsg : null,
            isUpdated : false,
        };
        this.updateInfoData = this.updateInfoData.bind(this);
        this.handleImageChange = this.handleImageChange.bind(this);

    }

    componentDidMount(){
        if(!this.state.recruiterId) return;
        console.log("RecruiterInfoForm ...")
        console.log(this.state.recruiterId);
        //Loading Candidat Information
        api.get(`/recruteur/${this.state.recruiterId}`)
            .then((response) => {
                console.log(response.data);
                const apiData = response.data;
                console.log("This is the parted Data :", apiData);

                const apiDataT = DataMapper.mapRecruiterToEnglish(apiData);
                console.log("fetchedFormattedData :",apiDataT);

                this.setState({
                    currStateRecruiter: {
                        ...apiDataT
                    },
                })
                console.log("this is the current:",this.state.currStateRecruiter);
            })
            .catch((error) => {
                console.error('Error fetching candidate profile:', error);
            });

        //Loading ProfilePicture
        RecruiterService.getPProfileRequest(this.state.recruiterId).then((response)=>{
            const fetchedImageUrl=URL.createObjectURL(response.data);
            console.log("fetched Image :",fetchedImageUrl);
            if(response.status === 200){
                this.setState({
                    imgUrl : fetchedImageUrl
                });
            }
        }).catch(error=>{
            console.log(error);
            if(error.response.status === 404){
                console.log("no initial profile photo");
            }
        })


    }

    async updateInfoData() {
        const recruiterData = DataMapper.mapRecruiterToFrench(this.state.currStateRecruiter);
        console.log("this is the recruiter data " , recruiterData);
        const formData = new FormData();
        this.setState({
            isUpdated : true,
        })
        console.log(recruiterData);
        Object.keys(recruiterData).forEach(key => {
            formData.append(key, recruiterData[key]);
        })
        console.log("this is the image of the recruiter before adding to db :", this.state.currStateRecruiter.profilePicture);

        if (this.state.currStateRecruiter.profilePicture instanceof File) {
            formData.append('photoProfil', this.state.currStateRecruiter.profilePicture);
        }


        try {
            const response = await RecruiterService.updateRecruiterRequest(formData,this.state.recruiterId);
            const ppPath = response.data.nom; //check for errors
            console.log("this is the image path :",ppPath);
            if (response.status === 200) {
                this.setState({
                    successfulMsg: true ,
                    imgUrl : ppPath,
                    isUpdated : true
                });
                console.log("this is the image path :",ppPath);
                setTimeout(()=>{
                    window.location.reload();

                }, 1000)

            }
        } catch (error) {
            console.error(error);
        }
    }

    handleImageChange(e){
        const file = e.target.files[0];
        if (file) {
            const imageUrl = URL.createObjectURL(file);

            this.setState(prevState => ({
                currStateRecruiter: {
                    ...prevState.currStateRecruiter,
                    profilePicture: file,
                },
                imgUrl : imageUrl,
            }));
            console.log("this is the image : ", imageUrl);

        }
    }

    render(){
        return(
            <>
                <div className="space-y-6" id="section1">
                    <h2 className="text-2xl font-semibold text-blue-800">Personal Information</h2>
                    <div className="m-10 flex flex-col items-center gap-2">
                        <div className="relative group w-32 h-32">
                            <img
                                className="w-full h-full rounded-full object-cover border-4 border-blue-200 group-hover:border-blue-400 transition-all duration-300 cursor-pointer"
                                src={this.state.imgUrl || 'https://www.pngmart.com/files/23/Profile-PNG-Photo.png'}
                                alt="Profile"
                                onClick={() => document.getElementById('profile-upload').click()}
                            />

                            <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-30 rounded-full opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    className="h-8 w-8 text-white"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    stroke="currentColor"
                                >
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                            </div>

                            <input
                                id="profile-upload"
                                type="file"
                                accept="image/*"
                                className="hidden"
                                onChange={this.handleImageChange}
                            />
                        </div>

                        <label
                            htmlFor="profile-upload"
                            className="text-gray-600 text-sm cursor-pointer hover:text-blue-600 transition-colors"
                        >
                            Click to upload photo
                        </label>

                        <p className="text-xs text-gray-400">
                            JPG or PNG, max 2MB
                        </p>
                    </div>
                    <div>
                        <label className="block text-gray-700 mb-2">First Name</label>
                        <input type="text" value={this.state.currStateRecruiter.firstName || ''} className="block w-full border p-2 rounded-md" onChange={(e)=>{this.setState(prevState=>({
                            currStateRecruiter: {
                                ...prevState.currStateRecruiter,
                                firstName: e.target.value
                            }
                        }))}}/>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-2">Last Name</label>
                        <input type="text" className="block w-full border p-2 rounded-md text-black" value={this.state.currStateRecruiter.lastName || ''}  onChange={(e)=>{this.setState(prevState=>({
                            currStateRecruiter: {
                                ...prevState.currStateRecruiter,
                                lastName: e.target.value
                            }
                        }))}}/>
                    </div>


                    <div>
                        <label className="block text-gray-700 mb-2">Postition</label>
                        <input type="text" className="block w-full border p-2 rounded-md" value={this.state.currStateRecruiter.position || ''} onChange={(e)=>{
                            this.setState(prevState=>({
                                currStateRecruiter: {
                                    ...prevState.currStateRecruiter,
                                    position: e.target.value
                                }
                            }))}}/>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-2">CompanyName</label>
                        <input type="text" className="block w-full border p-2 rounded-md" value={this.state.currStateRecruiter.company || ''} onChange={(e)=>{
                            this.setState(prevState=>({
                                currStateRecruiter: {
                                    ...prevState.currStateRecruiter,
                                    company: e.target.value
                                }
                            }))}}/>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-2">Phone Number</label>
                        <input type="text" className="block w-full border p-2 rounded-md" value={this.state.currStateRecruiter.phone|| ''} onChange={(e)=>{
                            this.setState(prevState=>({
                                currStateRecruiter: {
                                    ...prevState.currStateRecruiter,
                                    phone: e.target.value
                                }
                            }))}}/>
                    </div>

                    <div className="mt-6">
                        <button type="button" className="bg-blue-600 rounded-xl px-5 py-3 text-white m-2 shadow" onClick={this.updateInfoData}>Save</button>
                    </div>

                    { this.state.isUpdated && (
                        this.state.successfulMsg ? (
                            <Message type="success" text="Uploaded Successfully!" />
                        ):(
                            <Message type="error" text="A problem occurred! Please try again." />
                        )
                    )

                    }
                </div>
            </>

        )
    }
}

