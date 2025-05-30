import React from 'react'
import RecruiterMainHeader from "../../utils/headers/RecruiterMainHeader";
import {Tab, TabList, TabPanel, Tabs} from "react-tabs";
import AccountManager from "../AccountManager";
import RecruiterInfoManager from "./RecruiterInfoManager";
import CompanyInfoManager from "../../companies/CompanyInfoManager";
import {Navigate} from "react-router-dom";
import RecruiterOffersManager from "./RecruiterOffersManager";


class RecruiterProfile extends React.Component {
    constructor(props) {
        const user = localStorage.getItem('user');
        super(props);
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
            <div>
                <RecruiterMainHeader/>
                <div className="min-h-screen bg-gray-100 p-6">
                    <Tabs className="flex flex-col md:flex-row bg-white rounded-lg shadow-lg overflow-hidden">
                        <TabList className="w-full md:w-1/4 bg-blue-600 text-white p-5 space-y-6">
                            <Tab className="cursor-pointer py-2 px-4 hover:bg-blue-700 rounded-lg">Personal Info</Tab>
                            <Tab className="cursor-pointer py-2 px-4 hover:bg-blue-700 rounded-lg">Company's Info</Tab>
                            <Tab className="cursor-pointer py-2 px-4 hover:bg-blue-700 rounded-lg">Job Applications</Tab>
                            <Tab className="cursor-pointer py-2 px-4 hover:bg-blue-700 rounded-lg">Account Settings</Tab>
                        </TabList>
                        <div className="w-full md:w-3/4 p-8 bg-gray-50">
                            <TabPanel>
                                <div className="space-y-6" id="section1">
                                    <RecruiterInfoManager recruiterId={userId}/>
                                    <hr/>
                                </div>
                            </TabPanel>
                            <TabPanel>
                                <CompanyInfoManager recruiterId={userId} />
                            </TabPanel>
                            <TabPanel>
                                <RecruiterOffersManager recruiterId={userId}/>
                            </TabPanel>
                            <TabPanel>
                                <AccountManager connected={this.state.connected} onConnectionChange={this.handleConnectionChange}/>
                            </TabPanel>
                        </div>
                    </Tabs>
                </div>
            </div>
        )
    }


}

export default RecruiterProfile
