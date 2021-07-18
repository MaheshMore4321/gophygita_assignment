import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import FetchPanel from './pages/Fetch';
import RootPanel from './pages/RootPanel';
import UserPanel from './pages/UserPanel';
import AdminPanel from './pages/AdminPanel';
import RedirectRoot from './pages/RedirectRoot';

import { BrowserRouter, Switch, Route } from 'react-router-dom';


function App() {
  return (
    <BrowserRouter>
        <Switch>
          <Route exact path='/' component={() => (<RootPanel/>)} />
          <Route exact path='/login' component={() => (<SignIn/>)} />
          <Route exact path='/fetch' component={() => (<FetchPanel/>)} />
          <Route exact path='/registration' component={() => (<SignUp/>)} />
          <Route exact path='/dashboard/admin' component={() => (<AdminPanel/>)} />
          <Route exact path='/dashboard/user/:userId' component={() => (<UserPanel/>)} />

          <Route component={() => (<RedirectRoot/>)} />
        </Switch>
    </BrowserRouter>
  );
}

export default App;
