import React, { useEffect } from 'react';
import * as _ from "../constants/ApiUrls";
import axios from 'axios';

export default function RootPanel() {

  const callApiLoadData = () => {
    const token = localStorage.getItem('token');

    axios.get(_.AUTH_CHECK_URL, _.HEADER_DATA(token))
      .then(response =>{
        if(response.data.flag) {
          const obj = response.data.object;
          if(obj.roles[0] === 'ROLE_ADMIN') {
            window.location.href = '/dashboard/admin';
          }
          else if(obj.roles[0] === 'ROLE_USER') {
            window.location.href = '/dashboard/user/'+obj.userId;
          }
        }
        else {
          window.location.href = '/login';
        }
      })
      .catch(error =>{ window.location.href = '/login'; });
  }

  useEffect(callApiLoadData, []);

  return (<></>);
}