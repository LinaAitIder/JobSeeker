import {useState} from "react";
import RecruiterService from "../services/RecruiterService";
import DataMapper from "../utils/DataMapper";
import Message from '../utils/Message'
import RecruiterMainHeader from "../utils/headers/RecruiterMainHeader";

export default function OfferForm(){
    console.log((JSON.parse(localStorage.getItem('user')).userId));
    const recruiterId =(JSON.parse(localStorage.getItem('user')).userId);
    const [formData, setFormData]= useState({
        title : '',
        publicationDate : '',
        expirationDate : '',
        description:'',
        country : '',
        city : '',
        salaryMin: '',
        salaryMax : '',
        contractType : '',
    });
    const [message, setMessage] = useState('');

    function handleChange(e){
        const {name, value} = e.target; // this returns the name of the input tag and its value
        setFormData((prev)=>({
            ...prev,
            [name] :value
        }))
    }

    async function handleSubmit(){
        let formmatedFormData = DataMapper.mapOfferToFrench(formData)
        try{
            const response = await RecruiterService.addOfferRequest(formmatedFormData, recruiterId);
            if (response.status===200){
                setMessage("Offer successfully Posted!")
                console.log("done successfully")
            }
        }catch(err){
            window.alert("A problem Occured, please retry Again!")
            setMessage("A problem Occured, please retry Again!")

        }
    }

    return (
        <>
            <RecruiterMainHeader/>
            <div className="flex justify-center items-center min-h-screen bg-gray-50">
            <form onSubmit={handleSubmit} className="max-w-2xl mx-auto p-6 bg-white rounded-2xl shadow-md grid grid-cols-1 gap-4">
                <h2 className="text-2xl font-semibold">Post a Job Offer</h2>

                <input
                    type="text"
                    name="title"
                    placeholder="Title"
                    value={formData.title}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                    required
                />

                <input
                    type="date"
                    name="publicationDate"
                    value={formData.publicationDate}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                    required
                />

                <input
                    type="date"
                    name="expirationDate"
                    value={formData.expirationDate}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                    required
                />

                <textarea
                    name="description"
                    placeholder="Description"
                    value={formData.description}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                    rows={4}
                    required
                />

                <input
                    type="text"
                    name="domain"
                    placeholder="Domain"
                    value={formData.domain}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                />

                <input
                    type="text"
                    name="country"
                    placeholder="Country"
                    value={formData.country}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                />

                <input
                    type="text"
                    name="city"
                    placeholder="City"
                    value={formData.city}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                />

                <div className="grid grid-cols-2 gap-4">
                    <input
                        type="number"
                        name="salaryMin"
                        placeholder="Minimum Salary"
                        value={formData.salaryMin}
                        onChange={handleChange}
                        className="p-2 border rounded-xl"
                    />

                    <input
                        type="number"
                        name="salaryMax"
                        placeholder="Maximum Salary"
                        value={formData.salaryMax}
                        onChange={handleChange}
                        className="p-2 border rounded-xl"
                    />
                </div>

                <select
                    name="contractType"
                    value={formData.contractType}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                >
                    <option value="">Select Contract Type</option>
                    <option value="CDI">CDI</option>
                    <option value="CDD">CDD</option>
                    <option value="Internship">Internship</option>
                    <option value="Freelance">Freelance</option>
                </select>

                <button
                    type="submit"
                    className="mt-4 bg-blue-600 text-white px-4 py-2 rounded-xl hover:bg-blue-700"
                >
                    Submit
                </button>
            </form>
            </div>
            <Message type="success" text={message}/>


        </>
    );
}