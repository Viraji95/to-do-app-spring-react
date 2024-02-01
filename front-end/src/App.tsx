import './App.css'
import {useUser, useUserDispatcher} from "./context/UserContext.tsx";
import {onAuthStateChanged} from 'firebase/auth';
import {SignIn} from "./signin/SignIn.tsx";
import {Loader} from "./loader/Loader.tsx";
import {useEffect, useState} from "react";
import {auth} from "./firebase.ts";
import {Header} from "./header/Header.tsx";

function App() {
    const [loader, setLoader] = useState(true);
    const user = useUser();
    const userDispatcher = useUserDispatcher();

    useEffect(() => {
        const unsubscribe = onAuthStateChanged(auth, (user) => {
            setLoader(false);
            if (user) {
                userDispatcher({type: 'sign-in', user});
            } else {
                userDispatcher({type: 'sign-out'});
            }
        });
        return () => unsubscribe();
    }, []);





    return (
    <>
        {loader ?
            <Loader/>
            :
            user ?
                (<>
                    <Header/>
                    <App/>

                </>)

                :
                <SignIn/>
        }
    </>
  )
}

export default App
