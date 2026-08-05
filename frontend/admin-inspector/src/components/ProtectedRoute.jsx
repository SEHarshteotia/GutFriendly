import { Navigate } from "react-router-dom";

export default function ProtectedRoute({

children,
role

}){

const currentRole = localStorage.getItem("role");

if(!currentRole){

return <Navigate to="/login"/>

}

if(role && currentRole!==role){

return <Navigate to="/login"/>

}

return children;

}