// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import {getAuth} from 'firebase/auth'
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyCEkrjxKCf6iv0uFeojkhRALS8bYHkPw2o",
    authDomain: "to-do-app-5c975.firebaseapp.com",
    projectId: "to-do-app-5c975",
    storageBucket: "to-do-app-5c975.appspot.com",
    messagingSenderId: "65746528808",
    appId: "1:65746528808:web:647fc6d1d9a6106352a31d"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

const auth = getAuth(app);

export {auth, app};