import { TextField } from '@material-ui/core';

export const isEmailValid = emailId => {
  let regEmail = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return regEmail.test(emailId);
}

export const getEnglishUserPanel = jsonData => {
  return <>
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Name" defaultValue={jsonData.name} />
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Username" defaultValue={jsonData.username} />
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Language" defaultValue={jsonData.language} />
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Mobileno" defaultValue={jsonData.mobileNo} />
        </>
}

export const getDutchUserPanel = jsonData => {
  return <>
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Name" defaultValue={jsonData.name} />
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Nutzername" defaultValue={jsonData.username} />
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Sprache" defaultValue={jsonData.language} />
            <TextField fullWidth autoFocus margin="dense" disabled id="standard-disabled" label="Handynummer" defaultValue={jsonData.mobileNo} />
        </>
}