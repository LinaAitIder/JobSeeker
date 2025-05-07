import React from "react";

export default function Message({ type, text }) {
    const baseClass = "text-black border p-4 m-4 text-center rounded";
    const colorClass = type === "error" ? "bg-red-400" : "bg-green-400";

    return (
        <div className={`${baseClass} ${colorClass}`}>
            {text}
        </div>
    );
}
