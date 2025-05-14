import React from 'react';
import CandidateMainHeader from '../../utils/headers/CandidateMainHeader';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import {Navigate} from "react-router-dom";
import AccountManager from "../AccountManager";
import ApplicationList from "../../applications/ApplicationList";
import CertificateManager from "./CertificationManager";
import CandidateInfoManager from "./CandidateInfoManager";
import CVManager from "./CVManager";

const USER_ID = localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).userId:'';


class CandidateProfile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      connected :true,
      successfulMsg : false,
    };

    this.handleConnectionChange=this.handleConnectionChange.bind(this);
  }

  handleConnectionChange(connectedStatus){
    this.setState({
      connected:connectedStatus,
    })
  }

  render() {
    if(!this.state.connected){
       return <Navigate to="/login"/>;
    }


    return (
      <>
        <CandidateMainHeader />
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

                 <CandidateInfoManager candidateId={USER_ID}/>

                  <hr/>
                  <CVManager candidateId={USER_ID} />
                  <hr/>
                  <CertificateManager candidateId={USER_ID} />
                </div>
              </TabPanel>

              <TabPanel>
                  <ApplicationList candidateId={USER_ID}/>
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
