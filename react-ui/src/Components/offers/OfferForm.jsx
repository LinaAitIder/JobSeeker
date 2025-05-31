import React, {useState} from "react";
import RecruiterService from "../../services/RecruiterService";
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
    const [message, setMessage] = useState({
        type:'',
        text:''
    });
    const [errors, setErrors] = useState({});


    function handleChange(e){
        const {name, value} = e.target;

        if (name === 'salaryMin' || name === 'salaryMax') {
            const numValue = parseFloat(value);
            if (value !== '' && (isNaN(numValue) || numValue < 0)) {
                setErrors({...errors, [name]: 'Salary must be a positive number'});
                return;
            } else {
                const newErrors = {...errors};
                delete newErrors[name];
                setErrors(newErrors);
            }
        }

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    }

    async function handleSubmit(e){
        e.preventDefault();
        if (!validateForm()) {
            window.alert("Please correct the form errors");
            setMessage({type:"error", text:"Please correct the form errors"});

            return

        }
        let formmatedFormData = DataMapper.mapOfferToFrench(formData)
        console.log("recruiterId", recruiterId);
        try{
            const response = await RecruiterService.addOfferRequest(formmatedFormData, recruiterId);
                setMessage({type:"success", text:"Offer successfully Posted!"});
        }catch(err){
            window.alert("A problem Occured, please retry Again!")
            setMessage({type:"error", text:"A problem Occured, please retry again!"});

        }
    }

    function validateForm() {
        const newErrors = {};

        if (formData.salaryMin && formData.salaryMax &&
            parseFloat(formData.salaryMin) > parseFloat(formData.salaryMax)) {
            newErrors.salaryMin = 'Minimum salary cannot be greater than maximum';
            newErrors.salaryMax = 'Maximum salary cannot be less than minimum';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }

    return (
        <>
            <RecruiterMainHeader/>
            <div className="flex justify-center items-start  min-h-screen bg-gray-50  pt-0">
            <form onSubmit={handleSubmit} className="max-w-2xl mx-auto p-6 bg-white rounded-2xl shadow-md grid grid-cols-1 gap-4 mt-10">
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

                <div className="flex flex-row gap-4 ">
                    <input
                        type="date"
                        name="publicationDate"
                        value={formData.publicationDate}
                        onChange={handleChange}
                        className="py-2  px-3 border rounded-xl"
                        required
                    />

                    <input
                        type="date"
                        name="expirationDate"
                        value={formData.expirationDate}
                        onChange={handleChange}
                        className="py-2 px-4 border rounded-xl"
                        required
                    />
                </div>


                <textarea
                    name="description"
                    placeholder="Description"
                    value={formData.description}
                    onChange={handleChange}
                    className="p-2 border rounded-xl"
                    rows={2}
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

                <div className="flex flex-row gap-4 ">

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

                <div className="grid grid-cols-2 gap-4">
                    <input
                        type="number"
                        name="salaryMin"
                        placeholder="Minimum Salary"
                        value={formData.salaryMin}
                        onChange={handleChange}
                        className="p-2 border rounded-xl"
                    />
                    {errors.salaryMin && (
                        <p className="mt-1 text-sm text-red-600">{errors.salaryMin}</p>
                    )}

                    <input
                        type="number"
                        name="salaryMax"
                        placeholder="Maximum Salary"
                        value={formData.salaryMax}
                        onChange={handleChange}
                        className="p-2 border rounded-xl"
                    />
                    <div className="block">
                    {errors.salaryMax && (
                        <p className="mt-1 text-sm text-red-600">{errors.salaryMax}</p>
                    )}
                    </div>
                </div>

                <button
                    type="submit"
                    className="mt-4 bg-blue-600 text-white px-4 py-2 rounded-xl hover:bg-blue-700"
                >
                    Submit
                </button>
                {message && (
                    <Message type={message.type} text={message.text} />
                )}
            </form>

            </div>



        </>
    );
}