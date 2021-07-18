import { TOKEN_CHANGE } from '../constants/ActionType';
import { initialState } from '../staticData/AuthorizationInitialToken';

const authorization = (state = initialState, action) => {
    const newState = {...state};

    switch(action.type) {
        case TOKEN_CHANGE : {
            return {...newState, token: action.payload.token};
        }
        default: {
            return newState;
        }
    }
}

export default authorization;