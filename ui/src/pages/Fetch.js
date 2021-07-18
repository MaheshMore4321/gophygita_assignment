import React, { useState } from 'react';
import * as _ from "../constants/ApiUrls";
import axios from 'axios';
import { Typography, Container, Grid, Dialog } from '@material-ui/core';
import { Avatar, Button, CssBaseline, Link, Box, FormHelperText, TextField, DialogTitle,
         DialogActions, DialogContent, DialogContentText, IconButton } from '@material-ui/core';
import PageviewIcon from '@material-ui/icons/Pageview';
import { makeStyles } from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import * as methd from './../utility/Utility';

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default function Fetch() {
  const classes = useStyles();

  const [username, setUsername] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [json, setJson] = useState(false);
  const [jsonData, setJsonData] = useState({
    userId: '',
    name: '',
    username: '',
    language: '',
    mobileNo: '',
    active: '',
    createdTs: '',
  });

  const handleSubmit = (e) => {
    e.preventDefault();

    setJson(false);
    setErrorMessage('');

    const UserRequest = {
      username: username
    };

    axios.get(_.FETCH_CHECK_USER,   { params: UserRequest }, _.HEADER_DATA)
      .then(response => { response.data.flag ? setOpenModal(true) : setErrorMessage(response.data.message); })
      .catch(error =>{ console.log(error); });
  }

  const [passcode, setPasscode] = useState('');

  const handlePasscodeSubmit = () => {

    setJson(false);
    setJsonData({
      userId: '',
      name: '',
      username: '',
      language: '',
      mobileNo: '',
      active: '',
      createdTs: '',
    });
    setErrorMessage('');

    const UserRequest = {
      username: username,
      passcode: passcode
    };

    axios.get(_.FETCH_PASSCODE_VALIDATE_USER,   { params: UserRequest }, _.HEADER_DATA)
      .then(response => {
        setJson(response.data.flag);
        if(response.data.flag) {
          const obj = response.data.object;
          setJsonData({
            userId: obj.userId,
            name: obj.name,
            username: obj.username,
            language: obj.language,
            mobileNo: obj.mobileNo,
            active: obj.active,
            createdTs: obj.createdTs
          });
        }
        else{
          setErrorMessage(response.data.message)
        }
      })
      .catch(error =>{ console.log(error); });
  }

  const [openModal, setOpenModal] = useState(false);
  const toggleOpenModal = () => {
    setOpenModal(!openModal);
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}><PageviewIcon /></Avatar>
        <Typography component="h1" variant="h5">Find Username</Typography>
        <form className={classes.form} onSubmit={handleSubmit}>
          <TextField name="username" label="Username" type="text" id="username" variant="outlined" fullWidth autoComplete="username" onChange={(event) => setUsername(event.target.value)} margin="normal" autoFocus required/>
          <FormHelperText id="my-helper-text" style={{color:"red"}}>{errorMessage}</FormHelperText>
          <Button type="submit" fullWidth variant="contained" color="primary" className={classes.submit}
          >Fetch</Button>
          <Grid container>
            <Grid item xs>
              <Link href={'/login'} variant="body2">Sign in</Link>
            </Grid>
            <Grid item>
              <Link href="/registration" variant="body2">Sign Up</Link>
            </Grid>
          </Grid>
        </form>
      </div>
      <Box mt={8}>
        <Typography variant="body2" color="textSecondary" align="center">
          {'Copyright © '}<Link color="inherit" href="/">Ui</Link>{' '}{new Date().getFullYear()}{'.'}
        </Typography>
      </Box>

      <>
        <Dialog open={openModal} onClose={() => toggleOpenModal()} aria-labelledby="form-dialog-title" maxWidth="xl"
        fullWidth={true} disableBackdropClick={true} disableEscapeKeyDown={true}>
          <DialogTitle id="form-dialog-title">
            <Box display="flex" alignItems="center">
              <Box flexGrow={1} >User {json ? " Panel" : " Verification"}</Box>
              <Box><IconButton onClick={() => toggleOpenModal()}><CloseIcon /></IconButton></Box>
            </Box>
          </DialogTitle>
          <DialogContent>
          {json ?
             <>
              <DialogContentText style={{paddingTop:"10px"}}>
                  Kindly find below user details.
              </DialogContentText>
              <Grid container spacing={2}>
                { jsonData.language === "EN" ? methd.getEnglishUserPanel(jsonData) :
                  jsonData.language === 'DE' ? methd.getDutchUserPanel(jsonData) : "NO DATA"}
              </Grid>
             </>
          :
            <>
              <FormHelperText id="my-helper-text" style={{color:"red"}}>{errorMessage}</FormHelperText>
              <DialogContentText style={{paddingTop:"10px"}}>
                  Kindly check your email & enter token to view user details.
              </DialogContentText>
              <TextField fullWidth autoFocus margin="dense" id="subject" label="Token" type="text" value={passcode}
              onChange={(event) => setPasscode(event.target.value)} style={{paddingBottom:"10px"}}/>
              <Button onClick={() => handlePasscodeSubmit()} variant="contained" color="primary" > Submit</Button>
            </>
          }
          </DialogContent>
          <DialogActions></DialogActions>
        </Dialog>
      </>
    </Container>
  );
}