import React, {Suspense} from 'react';
import CandidateMainHeader from '../../utils/headers/CandidateMainHeader';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import {Navigate} from "react-router-dom";
import AccountManager from "../AccountManager";
import ApplicationList from "../../applications/ApplicationList";
import CertificateManager from "./CertificationManager";
import CandidateInfoManager from "./CandidateInfoManager";
import CVManager from "./CVManager";
import CertificationManager from "./CertificationManager";



const LazyInfoManager = React.lazy(() => import('./CandidateInfoManager'));
const LazyApplicationsManager = React.lazy(() => import('../../applications/ApplicationList'));
const LazyCertificateManager = React.lazy(() => import('./CertificationManager'));
const LazyCvManager = React.lazy(() => import('./CVManager'));

class CandidateProfile extends React.Component {

  constructor(props) {
    super(props)
      const user = localStorage.getItem('user');
      this.state = {
          connected :true,
          successfulMsg : false,
          userId: user ? JSON.parse(user).userId : ''
    };

    this.handleConnectionChange=this.handleConnectionChange.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
        const user = localStorage.getItem('user');
        const newUserId = user ? JSON.parse(user).userId : '';
        if (newUserId !== prevState.userId) {
            this.setState({ userId: newUserId });
        }
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
    const { userId } = this.state;


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
                    <Suspense fallback={<div>Loading...</div>}>
                        <LazyInfoManager candidateId={userId}/>
                    </Suspense>
                  <hr/>
                    <CVManager candidateId={userId}/>

                  <hr/>
                        <CertificationManager candidateId={userId} />
                </div>
              </TabPanel>

              <TabPanel>
                  <Suspense fallback={<div>Loading...</div>}>
                    <LazyApplicationsManager candidateId={userId}/>
                  </Suspense>
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
