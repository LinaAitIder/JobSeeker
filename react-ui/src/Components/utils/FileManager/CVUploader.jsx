import React, {useEffect, useState} from 'react'
import {TrashIcon} from "@heroicons/react/16/solid";




export default function CVUploader({onFileSelect}){
    const [file, setFile] = useState(null);
    const handleFileUpload = ((e)=>{
        if(e.target.files){
            const firstFile = e.target.files[0];
            setFile(e.target.files[0]);
            if(onFileSelect){
                onFileSelect(firstFile);
            }

        }else {
            console.log("there is not file")

        }
    })
    return(
        <div>
            <input type="file" className="block w-full border p-2 rounded-md " onChange={handleFileUpload} />
            {file && (
            <div className="flex-col flex p-2 shadow-lg align-center ">
                <span className="p-2 text-black">File Name : {file.name}</span>
                <span className="p-2">File Size : {((file.size)/1024).toFixed(2)} KB</span>
                <span className="p-2 text-black">File Type : {file.type}</span>

            </div>
            )}


        </div>
    );

}