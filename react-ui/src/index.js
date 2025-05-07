import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

import reportWebVitals from './reportWebVitals';
import {GoogleOAuthProvider} from '@react-oauth/google';

const CLIENT_ID = process.env.REACT_APP_CLIENT_ID;
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <GoogleOAuthProvider clientId={CLIENT_ID}>
    <App />
    </GoogleOAuthProvider>
  </React.StrictMode>
 
);

reportWebVitals();
