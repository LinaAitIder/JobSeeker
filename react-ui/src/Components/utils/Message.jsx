import React, {useEffect, useState} from "react";

export default function Message({ type, text, timeout= 10000 }) {
    const baseClass = "text-black border p-4 m-4 text-center full-rounded";
    const colorClass = type === "error" ? "bg-red-400" : "bg-green-400";
    const [isVisible, setIsVisible] = useState(true);

    let seekandHideClass;
    if(type === null || text ===null || !type || !text){
        seekandHideClass ='hidden';
    }

    useEffect(() => {
        if (timeout > 0) {
            const timer = setTimeout(() => {
                setIsVisible(false);
            }, timeout);

            return () => clearTimeout(timer);
        }
    }, [timeout]);

    return (
        <div className={`${baseClass} ${colorClass} ${seekandHideClass} ${isVisible ? 'opacity-100' : 'hidden'}`}>
            {text}
        </div>
    );
}
