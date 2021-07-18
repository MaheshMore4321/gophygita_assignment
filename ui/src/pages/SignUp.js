import React, { useState } from 'react';
import * as _ from "../constants/ApiUrls";
import axios from 'axios';
import { Avatar, Button, CssBaseline, TextField, MenuItem, Link, Grid, Box, FormHelperText, 
         Typography, Container } from '@material-ui/core';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import { makeStyles } from '@material-ui/core/styles';
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
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default function SignUp() {
  const classes = useStyles();

  const [name, setName] = useState('');

  const [username, setUsername] = useState('');
  const [userNameFlag, setUserNameFlag] = useState(false);
  const [userNameMessage, setUserNameMessage] = useState('');
  const userNameChangeHandler = (userName) => {

    const flag = methd.isEmailValid(userName);
    if(!flag) {
      setUserNameFlag(false);
      setUserNameMessage('Invalid EmailId');
      return ;
    }

    const UserRequest = {
      username: userName
    };
    axios.get(_.USER_IS_EXIST,  { params: UserRequest }, _.HEADER_DATA)
      .then(response =>{
        const flag = response.data.flag;
        setUserNameFlag(!flag);
        setUserNameMessage(!flag ? '' : response.data.message);
        setUsername(userName);
      })
      .catch(error =>{ console.log(error); });
  }

  const [password, setPassword] = useState('');
  const [passwordMatchedFlag, setPasswordMatchedFlag] = useState(true);
  const [passwordMatchedMessage, setPasswordMatchedMessage] = useState('');
  const passwordChangeHandler = (password1) => {
      if(password1.length < 8 && password.length < 8){
        setPasswordMatchedMessage('password length must greater than 8');
      }
      else {
        if (password === password1){
          setPasswordMatchedFlag(false);
          setPasswordMatchedMessage('');
          setPassword(password);
        }
        else {
          setPasswordMatchedFlag(true);
          setPasswordMatchedMessage('password mismatch');
        }
      }
  }

  const [language, setLanguage] = useState('EN');
  const languages = [
    {
      label: 'English',
      value: 'EN',
    },
    {
      label: 'German',
      value: 'DE',
    }
  ];

  const [mobileNo, setMobileNo] = useState('');
  const mobilenoChangeHandler = (mobileno) => {
    if(mobileno.length <= 10){
      setMobileNo(mobileno);
    }
  }

  const [signUpStatus, setSignUpStatus] = useState(true);
  const [signUpMessage, setSignUpMessage] = useState('');
  const handleSubmit = (e) => {
    e.preventDefault();

    setSignUpMessage('');

    const registrationRequest = {
      name: name,
      username: username,
      password: password,
      language: language,
      mobileNo: mobileNo,
    };

    axios.post(_.REGISTER_USER,  registrationRequest, _.HEADER_DATA)
      .then(response =>{
        setSignUpStatus(response.data.flag);
        if(response.data.flag){
          setSignUpMessage("Registration Successful, Confirmation Message Sent On Email");
        }
        else {
          setSignUpMessage("Something went wrong, Kindly try after some time");
        }

      })
      .catch(error =>{
        setSignUpStatus(false);
        setSignUpMessage("Something went wrong, Kindly try after some time");

      });
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}><LockOutlinedIcon /></Avatar>
        <Typography component="h1" variant="h5">Sign up</Typography>
        <FormHelperText fullWidth focused style={{color: signUpStatus?'blue':'red'}}>{signUpMessage}</FormHelperText>
        <form className={classes.form} onSubmit={handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField name="name" label="Name" type="text" id="name" variant="outlined" fullWidth  autoComplete="name" onChange={(event) => setName(event.target.value)} autoFocus required/>
            </Grid>
            <Grid item xs={12}>
              <TextField name="username" label="Username" type="email" id="username" variant="outlined" fullWidth autoComplete="username" onChange={(event) => userNameChangeHandler(event.target.value)} error={!userNameFlag} helperText={userNameMessage} autoFocus required/>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField name="password" label="Password" type="password" id="password" variant="outlined" fullWidth autoComplete="password" onChange={(event) => setPassword(event.target.value)} error={passwordMatchedFlag} helperText={passwordMatchedMessage} autoFocus required/>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField name="password1" label="Password1" type="password" id="password1" variant="outlined" fullWidth autoComplete="password1" onChange={(event) => passwordChangeHandler(event.target.value)}  error={passwordMatchedFlag} autoFocus required/>
            </Grid>
            <Grid item xs={12} sm={4}>
              <TextField name="language" id="language" label="Language" value={language} onChange={(event) => setLanguage(event.target.value)} select variant="outlined" fullWidth autoFocus required>
                {languages.map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={8}>
              <TextField name="mobileNo" label="Mobile Number" type="number" id="mobileNo" variant="outlined" fullWidth autoComplete="mobileNo" value={mobileNo} onChange={(event) => mobilenoChangeHandler(event.target.value)}  autoFocus/>
            </Grid>
          </Grid>
          <Button type="submit" fullWidth variant="contained" color="primary" className={classes.submit} disabled={passwordMatchedFlag || !userNameFlag}> Sign Up </Button>
          <Grid container>
            <Grid item xs>
              <Link href={'/fetch'} variant="body2">Fetch Username</Link>
            </Grid>
            <Grid item>
            <Link href={'/login'} variant="body2"> Already have an account ? Sign in </Link>
            </Grid>
          </Grid>
        </form>
      </div>
      <Box mt={5}>
        <Typography variant="body2" color="textSecondary" align="center">
          {'Copyright © '}<Link color="inherit" href="/">Ui</Link>{' '}{new Date().getFullYear()}{'.'}
        </Typography>
      </Box>
    </Container>
  );
}