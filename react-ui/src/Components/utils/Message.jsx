import React, {useEffect, useState} from "react";

export default function Message({ type, text }) {
    const baseClass = "text-black border p-4 m-4 text-center full-rounded";
    const colorClass = type === "error" ? "bg-red-400" : "bg-green-400";
    const [isVisible, setIsVisible] = useState(true);


    let seekandHideClass;
    if(type === null || text ===null || !type || !text){
        seekandHideClass ='hidden';
    }


    return (
        <div className={`${baseClass} ${colorClass} ${seekandHideClass} ${isVisible ? 'opacity-100' : 'hidden'}`}>
            {text}
        </div>
    );
}
