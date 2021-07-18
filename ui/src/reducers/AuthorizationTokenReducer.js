import { LOGIN } from '../constants/ActionType';
import { initialState } from '../staticData/AuthorizationInitialToken';

const authorization = (state = initialState, action) => {
    const newState = {...state};

    switch(action.type) {
        case LOGIN : {
            return {
                ...newState,

                id: action.payload.id,
                username: action.payload.username,
                role: action.payload.role,
                token: action.payload.token
            };
        }
        default: {
            return newState;
        }
    }
}

export default authorization;