import React from "react";
import Message from "../../utils/Message";
import api from "../../../api/axiosConfig";
import DataMapper from "../../utils/DataMapper";
import CandidateService from "../../../services/CandidateService";



export default class CandidateInfoManager extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            currStateCandidate : {},
            countries :[],
            connected :true,
            imgUrl :'',
            candidateId : props.candidateId,
            successfulMsg : null,
            isUpdated : false,
        };
        this.updateInfoData = this.updateInfoData.bind(this);
        this.handleImageChange = this.handleImageChange.bind(this);
        this.fetchCandidatPProfile = this.fetchCandidatPProfile.bind(this);
        this.fetchCandidateData = this.fetchCandidateData.bind(this);
        this.fetchCountries = this.fetchCountries.bind(this);

    }

    componentDidMount(){
        if (!this.state.candidateId) return;
        console.log("CandidatInfoForm ...")
        console.log(this.state.candidateId);
        //Loading Candidat Information
        this.fetchCandidateData().then(r => console.log("candidate Data Fetched"));
        //Loading Countries
        this.fetchCountries().then(r=> console.log("Countries Fetched"));
        //Loading ProfilePicture
        this.fetchCandidatPProfile().then(r => console.log("Candidate Profile Picture Candidates"));
    }

    async fetchCountries(){
        await api.get('https://restcountries.com/v3.1/all?fields=name,flags')
            .then((response) => {
                this.setState({ countries: response.data });
            })
            .catch((error) => {
                console.error('Error fetching countries:', error);
            });

    }
    async fetchCandidateData(){
        await api.get(`/candidat/${this.state.candidateId}`)
            .then((response) => {
                console.log(response.data);
                const apiData = response.data;
                console.log("This is the parted Data :", apiData);

                const apiDataT = DataMapper.mapCandidateToEnglish(apiData);
                console.log("fetchedFormattedData :",apiDataT);

                this.setState({
                    currStateCandidate: {
                        ...apiDataT
                    },
                })
                console.log("this is the current:",this.state.currStateCandidate);
            })
            .catch((error) => {
                console.error('Error fetching candidate profile:', error);
            });
    }
    async fetchCandidatPProfile() {
        await CandidateService.getPProfileRequest(this.state.candidateId).then((response)=>{
            const fetchedImageUrl=URL.createObjectURL(response.data);
            console.log("fetched Image :",fetchedImageUrl);
            if(response.status === 200){
                this.setState({
                    imgUrl : fetchedImageUrl
                });
            }

        }).catch((err)=>{
            console.log(err);
        })
    }

    async updateInfoData() {
        const candidateData = DataMapper.mapCandidateToFrench(this.state.currStateCandidate);
        const formData = new FormData();
        this.setState({
            isUpdated : true,
        })
        console.log(candidateData);
        Object.keys(candidateData).forEach(key => {
            formData.append(key, candidateData[key]);
        })
        if (this.state.currStateCandidate.profilePicture instanceof File) {
            formData.append('photoProfil', this.state.currStateCandidate.profilePicture);
        }
        try {
            const response = await CandidateService.updateCandidateRequest(candidateData,this.state.candidateId);
            const ppPath = response.data.photoProfilPath ;
            console.log(ppPath);
            if (response.status === 200) {
                this.setState({
                    successfulMsg: true ,
                    imgUrl : ppPath,
                    isUpdated : true
                });
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
                currStateCandidate: {
                    ...prevState.currStateCandidate,
                    profilePicture: file,
                },
                imgUrl : imageUrl ,
            }));
        }
    }

    render(){
        const {countries } = this.state;
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
                        <input type="text" value={this.state.currStateCandidate.firstName || ''} className="block w-full border p-2 rounded-md" onChange={(e)=>{this.setState(prevState=>({
                            currStateCandidate: {
                                ...prevState.currStateCandidate,
                                firstName: e.target.value
                            }
                        }))}}/>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-2">Last Name</label>
                        <input type="text" className="block w-full border p-2 rounded-md text-black" value={this.state.currStateCandidate.lastName || ''}  onChange={(e)=>{this.setState(prevState=>({
                            currStateCandidate: {
                                ...prevState.currStateCandidate,
                                lastName: e.target.value
                            }
                        }))}}/>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-2">Country</label>
                        <select className="block w-full border p-2 rounded-md" value={this.state.currStateCandidate.country || ''} onChange={(e)=>{
                            this.setState(prevState=>({
                                currStateCandidate: {
                                    ...prevState.currStateCandidate,
                                    country: e.target.value
                                }
                            }));
                        }}>
                            <option >{this.state.currStateCandidate.country}</option>
                            {countries.map((c) => (
                                <option key={c.cca3} value={c.cca3} > {c.name.common}</option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-2">City</label>
                        <input type="text" className="block w-full border p-2 rounded-md" value={this.state.currStateCandidate.city || ''} onChange={(e)=>{
                            this.setState(prevState=>({
                                currStateCandidate: {
                                    ...prevState.currStateCandidate,
                                    city:e.target.value
                                }
                            }))
                        }}/>
                    </div>

                    <div>
                        <label className="block text-gray-700 mb-2">Phone Number</label>
                        <input type="text" className="block w-full border p-2 rounded-md" value={this.state.currStateCandidate.phoneNumber || ''} onChange={(e)=>{
                            this.setState(prevState=>({
                                currStateCandidate: {
                                    ...prevState.currStateCandidate,
                                    phoneNumber: e.target.value
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

