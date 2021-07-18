import * as _ from '../constants/ActionType';

export const login = (id, username, role, token) => ({
    type: _.LOGIN,
    payload: {
        id,
        username,
        role,
        token
    }
})
