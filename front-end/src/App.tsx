import './App.css'
import {useUser, useUserDispatcher} from "./context/UserContext.tsx";

function App() {

    const user = useUser();
    const userDispatcher = useUserDispatcher();

    if(user) {
        userDispatcher({type: 'sign-in'});
    }else {
        userDispatcher({type: 'sign-out'});
    }


  return (
    <>
      hdfhdhdh
    </>
  )
}

export default App
