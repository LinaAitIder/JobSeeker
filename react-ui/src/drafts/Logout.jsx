import {useNavigate} from "react-router-dom";

export const Logout= ()=>{
    const navigate = useNavigate();
    function da(){
        localStorage.removeItem('user');
        localStorage.removeItem('userIdentification');
        navigate('/login');
    }
    return(
       <div className="bg-white">
           <button onClick={da} className="bg-amber-400 text-white">Dissapear!!!</button>
       </div>
    );


}

