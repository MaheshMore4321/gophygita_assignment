import { combineReducers } from 'redux';
import authorization from './AuthorizationTokenReducer';

export default combineReducers({
    authorization,
})