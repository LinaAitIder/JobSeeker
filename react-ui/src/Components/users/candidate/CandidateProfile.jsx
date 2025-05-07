import React from 'react';
import CandidateMainHeader from '../../utils/headers/CandidateMainHeader';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import api from '../../../api/axiosConfig'
import AuthService from "../../services/AuthService";
import {Navigate} from "react-router-dom";
import CandidateService from "../../services/CandidateService" ;
import {TrashIcon, PlusIcon} from "@heroicons/react/16/solid";
import CVUploader from "../../utils/FileManager/CVUploader";
import candidateService from "../../services/CandidateService";
import AccountManager from "./AccountManager";
import ApplicationList from "../../applications/ApplicationList";
import CertificateManager from "./CertificationManager";

const USER_ID = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';


class CandidateProfile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      originalCandidate:{},
      currStateCandidate : {},
      countries :[],
      selectedCVFile:null,
      connected :true,
      successfulMsg : false,
      cvUpStatus:false,
      cvUrl:'',
    };
    this.updateInfoData = this.updateInfoData.bind(this);
    this.transformToFrenchAPIFormat= this.transformToFrenchAPIFormat.bind(this);
    this.resetForm= this.resetForm.bind(this);
    this.hasUnsavedChanges=this.hasUnsavedChanges.bind(this);
    this.resetFormWithConfirmation=this.resetFormWithConfirmation.bind(this);
    this.uploadInitialCV=this.uploadInitialCV.bind(this);
    this.handleFileSelect=this.handleFileSelect.bind(this);
    this.updateCV = this.updateCV.bind(this);
    this.handleConnectionChange=this.handleConnectionChange.bind(this);
    this.getCvFullPath = this.getCvFullPath.bind(this);
  }

  //fetching Data
  componentDidMount() {
    console.log("Hi I am component did MOUnt! I am trying my best to work withthis loadded data")
    //Promise.all([fetchedCandidatData, fetchedRestCountries]);
    //let fetchedCandidatData = ne
    let userId = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:''; //handle the error that will rise form backend when request with a userId=''

    api.get(`/candidat/${userId}`)
      .then((response) => {

        const apiData = response.data;
        console.log(apiData);
        this.setState({
         currStateCandidate: {
           id :apiData.id,
           profilePicture: apiData.photoProfil,
           firstName: apiData.nom,
           lastName: apiData.prenom,
           email:apiData.email,
           phoneNumber:apiData.telephone,
           city : apiData.ville,
           country:apiData.pays,
           cvPath:apiData.cvPath,
           certifications:apiData.certifications
         },
          originalCandidate:  {
            id :apiData.id,
            profilePicture: apiData.photoProfil,
            firstName: apiData.nom,
            lastName: apiData.prenom,
            email:apiData.email,
            phoneNumber:apiData.telephone,
            city : apiData.ville,
            country:apiData.pays,
            cvPath:apiData.cvPath,
            certifications:apiData.certifications
          }
        })
        console.log("this is the current:",this.state.currStateCandidate);
      })
      .catch((error) => {
        console.error('Error fetching candidate profile:', error);
      });

    //Add this to utility
    api.get('https://restcountries.com/v3.1/all')
      .then((response) => {
        this.setState({ countries: response.data });
      })
      .catch((error) => {
        console.error('Error fetching countries:', error);
      });

    //Getting the Cv
    api.get(`candidat/${userId}/cv`, {
      responseType:'blob'
    }).then((response)=>{
      if(response.status === 200  ){
        const pdfBlob = new Blob([response.data], {type:'application/pdf'});
        const pdfUrl = URL.createObjectURL(pdfBlob);
        console.log(pdfUrl);
        this.setState({
          cvUrl : pdfUrl,
        });
      } else {
        window.alert("the response status:"+ response.status)
      }
    }).catch(error=>{
      window.alert(error);
    });



  }
  resetFormWithConfirmation = () => {
    if (this.hasUnsavedChanges()) {
      if (window.confirm("Discard changes?")) {
        this.resetForm();
      }
    } else {
      console.log("no changes");
      this.resetForm();
    }
  };
  hasUnsavedChanges = () => {
    return JSON.stringify(this.state.currStateCandidate) !==
        JSON.stringify(this.state.originalCandidate);
  };
  transformToFrenchAPIFormat(englishData) {
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
  resetForm = () => {
    window.history.back();
  };
  handleFileSelect(file){
      this.setState(
          {
            selectedCVFile: file,
          }
      )
  }
  async updateInfoData() {
    const userId = JSON.parse(localStorage.getItem('user'))?.userId;
    const data = this.transformToFrenchAPIFormat(this.state.currStateCandidate);
    try {
      const response = await CandidateService.updateCandidateRequest(data, userId);
      if (response.status === 200) {
        localStorage.setItem('candidat', JSON.stringify(response.data));
        this.setState({ successfulMsg: true });
      }
    } catch (error) {
      console.error(error);
    }
  }
  async uploadInitialCV(){
      let selectedCVFile = this.state.selectedCVFile;
      const InitialCvFile = new FormData();
      InitialCvFile.append('file', selectedCVFile);
      console.log(InitialCvFile.Type);
      let userId = JSON.parse(localStorage.getItem('user')).userId;
    try {
      const response = await candidateService.uploadCvRequest(InitialCvFile,userId);
      if(response.status === 200 || 204 ){
        this.setState ({

          cvUpStatus : true
        })
      } else {
        this.setState ({
          cvUpStatus : false
        })
      }
    } catch(err){
      console.error(err);
      throw new Error(err);
    }

  }
  async updateCV(){
    let selectedCVFile = this.state.selectedCVFile;
    const UpdatedCvFile = new FormData();
    UpdatedCvFile.append('file', selectedCVFile);
    let userId = JSON.parse(localStorage.getItem('user')).userId;
    try {
      const response = await candidateService.updateCvRequest(UpdatedCvFile,userId);
      if(response.status === 200 || 204 ){
        this.setState ({
          cvUpStatus : true
        })
      } else {
        this.setState ({
          cvUpStatus : false
        })
      }
    } catch(err){
      console.error(err);
      throw new Error(err);
    }
  }
  async getCvFullPath(){
    let userId = JSON.parse(localStorage.getItem('user')).userId;
    try {
      const response = await candidateService.getCvRequest(userId);
      if(response.status === 200 || 204 ){
        const pdfBlob = new Blob([response.data], {type:'application/pdf'});
        const pdfUrl = URL.createObjectURL(pdfBlob);
        this.setState((prevState)=>({
          cvUrl : pdfUrl,
        }));
        return this.state.cvUrl;

      } else {
        window.alert(response.status)
      }
    } catch(err){
      console.error(err);
      window.alert(err)

    }
    return this.state.cvUrl;
  }

  handleConnectionChange(connectedStatus){
    this.setState({
      connected:connectedStatus,
    })
  }




  render() {
    const { candidatInfo, countries } = this.state;
    if(!this.state.connected){
       return <Navigate to="/login"/>;
    }

    return (
      <>
        <CandidateMainHeader logout={this.logout}/>
        <div className="min-h-screen bg-gray-100 p-6">
          <Tabs className="flex flex-col md:flex-row bg-white rounded-lg shadow-lg overflow-hidden">
            <TabList className="w-full md:w-1/4 bg-blue-600 text-white p-5 space-y-6">
              <Tab className="cursor-pointer py-2 px-4 hover:bg-blue-700 rounded-lg">Personal Info</Tab>
              <Tab className="cursor-pointer py-2 px-4 hover:bg-blue-700 rounded-lg">Applications</Tab>
              <Tab className="cursor-pointer py-2 px-4 hover:bg-blue-700 rounded-lg">Account Settings</Tab>
            </TabList>

            <div className="w-full md:w-3/4 p-8 bg-gray-50">

              <TabPanel>
                <div className="space-y-6" id="section1">
                  <h2 className="text-2xl font-semibold text-blue-800">Personal Information</h2>

                  <div className=" rounded-full bg-bleu-800 w-50 h-50">
                    <label className="block text-gray-700 mb-2">Profile Picture</label>
                    <img className="rounded-full bg-blue-500 " src={this.state.currStateCandidate.profilePicture}/>
                    <input type="file" className="block w-full border p-2 rounded-md bg-amber-400 rounded-full" onChange={(e)=>{
                      this.setState(prevState=>({
                        currStateCandidate: {
                        ...prevState.currStateCandidate,
                          profilePicture:e.target.files[0]
                      }
                      }))
                    }}/>
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

                  {/* Here how do i manage t*/}
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
                        <option key={c.cca3} value={c.cca3} >{c.name.common}</option>
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

                  <hr/>

                  { this.state.successfulMsg?
                  <div className=" bg-green-100 border-b-emerald-400 my-3">
                    <span className="py-2 px-6 text-green-700">Successfully Saved!</span>
                  </div>
                  :
                      <div className=" bg-green-400 border-b-red-800 my-3">
                        <span className="py-2 px-6 text-red-700">Not Saved Proprely, please Retry!</span>
                      </div>}
                  <div className="mt-6">
                    <button type="button" className="bg-blue-600 rounded-xl px-5 py-3 text-white m-2 shadow" onClick={this.updateInfoData}>Save</button>
                    <button type="button"  className="bg-white rounded-xl px-5 py-3 text-black m-2 shadow" onClick={this.resetFormWithConfirmation}>Cancel</button>
                  </div>

                  {/* CV Upload */}
                  <div className="mt-10">
                    <h2 className="text-2xl font-semibold text-blue-800 mb-4">CV</h2>
                    <CVUploader onFileSelect={this.handleFileSelect}/>
                    <div className="mt-2">
                      {this.state.currStateCandidate.cvPath ? (
                          <div>
                            <a href={this.state.cvUrl}>Click to view the file</a>
                            <p className="text-green-600">CV Already Uploaded</p>
                            <button type="button" className="bg-blue-600 rounded-xl px-5 py-3 text-white m-2 shadow" onClick={this.updateCV}>Update CV</button>
                          </div>
                        ) : (
                          <div>
                        <p className="text-red-500">No CV Uploaded Yet</p>
                        <button type="button" className="bg-blue-600 rounded-xl px-5 py-3 text-white m-2 shadow" onClick={this.uploadInitialCV}>Upload CV</button>
                          </div>
                      )}
                    </div>
                    { this.state.cvUpStatus ? (
                        <div className=" bg-green-100 border-b-emerald-400 my-3">
                          <span className="py-2 px-6 text-green-700">Successfully Uploaded!</span>
                        </div>
                      ):(
                        <div className=" bg-red-100 border-b-red-400 my-3">
                          <span className="py-2 px-6 text-red-700">Not proprely uploaded!</span>
                        </div>
                      )
                    }

                  </div>

                <hr/>

                  <div className="mt-10">
                    <h2 className="text-2xl font-semibold text-blue-800 mb-4">Certifications</h2>
                    <CertificateManager candidateId={USER_ID} />
                  </div>
                <hr/>

                </div>
              </TabPanel>

              <TabPanel>
                  <ApplicationList candidatId={USER_ID}/>
              </TabPanel>

              <TabPanel>
                <AccountManager connected={this.state.connected} onConnectionChange={this.handleConnectionChange}/>
              </TabPanel>
            </div>
          </Tabs>
        </div>
      </>
    );
  }
}

export default CandidateProfile;
