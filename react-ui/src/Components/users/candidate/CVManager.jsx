import CVUploader from "../../utils/FileManager/CVUploader";
import Message from "../../utils/Message";
import React from "react";
import api from "../../../api/axiosConfig";
import CandidateService from "../../services/CandidateService";


 class CVManager extends React.Component{
      constructor(props) {
          super(props);
          this.state = {
              candidateId : props.candidateId,
              selectedCVFile:null,
              cvUpStatus:false,
              successfulMsg:'',
              cvUrl:'',
              cvIsUpdated : false,

          };
          this.uploadInitialCV=this.uploadInitialCV.bind(this);
          this.handleFileSelect=this.handleFileSelect.bind(this);
          this.updateCV = this.updateCV.bind(this);
          this.getCvFullPath = this.getCvFullPath.bind(this);


      }
      componentDidMount() {
         api.get(`candidat/${this.state.candidateId}/cv`, {
             responseType:'blob'
         }).then((response)=>{
             if(response.status === 200  ){
                 const pdfBlob = new Blob([response.data], {type:'application/pdf'});
                 const pdfUrl = URL.createObjectURL(pdfBlob);
                 console.log(pdfUrl);
                 this.setState({
                     cvUrl : pdfUrl,
                 });
             }
         }).catch(error=>{

             console.error(error);
         });

     }
      handleFileSelect(file){
        this.setState(
            {
                selectedCVFile: file,
            }
        )
    }
      async uploadInitialCV(){
        let selectedCVFile = this.state.selectedCVFile;
        const InitialCvFile = new FormData();
        InitialCvFile.append('file', selectedCVFile);
        console.log(InitialCvFile.Type);
        try {
            const response = await CandidateService.uploadCvRequest(InitialCvFile,this.state.candidateId);
            if(response.status === 200 || 204 ){
                this.setState ({

                    cvUpStatus : true,
                    cvIsUpdated : true

                })
            } else {
                this.setState ({
                    cvUpStatus : false,
                    cvIsUpdated : true

                })
            }
        } catch(err){
            this.setState ({
                cvIsUpdated : true
            })
            console.error(err);
            throw new Error(err);

        }
    }
      async updateCV(){
        let selectedCVFile = this.state.selectedCVFile;
        const UpdatedCvFile = new FormData();
        UpdatedCvFile.append('file', selectedCVFile);
        try {
            const response = await CandidateService.updateCvRequest(UpdatedCvFile,this.state.candidateId);
            if(response.status === 200 || 204 ){
                this.setState ({
                    cvUpStatus : true,
                    cvIsUpdated : true
                })
            } else {
                this.setState ({
                    cvUpStatus : false,
                    cvIsUpdated : true
                })
            }
        } catch(err){
            this.setState ({
                cvIsUpdated : true
            })
            console.error(err);
            throw new Error(err);
        }
    }
      async getCvFullPath(){
        try {
            const response = await CandidateService.getCvRequest(this.state.candidateId);
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
      render(){
        return(
            <div className="mt-10">
                <h2 className="text-2xl font-semibold text-blue-800 mb-4">CV</h2>
                <CVUploader onFileSelect={this.handleFileSelect}/>
                <div className="mt-2">
                    {this.state.cvUrl ? (
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
                {this.state.cvIsUpdated && (
                   this.state.successfulMsg ? (
                       <Message type="success" text="Successfully Saved!" timeout={10000}/>

                   ):(
                       <Message type="error" text="Not Saved Proprely, please Retry!" timeout={10000}/>

                   )
                )}

            </div>
        );

    }



}
export default CVManager;