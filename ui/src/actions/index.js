import * as _ from '../constants/ActionType';

export const tokenChange = token => ({
    type: _.TOKEN_CHANGE,
    payload: {
        token
    }
})
