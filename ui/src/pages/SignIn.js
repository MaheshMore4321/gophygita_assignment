import React, { useState } from 'react';
import * as _ from "../constants/ApiUrls";
import axios from 'axios';
import { Avatar, Button, CssBaseline, TextField, Link, Grid, Box } from '@material-ui/core';
import { FormHelperText, Typography, Container } from '@material-ui/core';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import { makeStyles } from '@material-ui/core/styles';

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

export default function SignIn() {
  const classes = useStyles();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const [errorMessage, setErrorMessage] = useState('');
  const loadActionStartup = () => {
    let param = new URLSearchParams(window.location.search);
    let query = param.get('error');

    if(query !== null && query === 'true' && errorMessage === ''){
      setErrorMessage("username or password is incorrect.");
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault();

    setErrorMessage('');

    const loginRequest = {
      username: username,
      password: password
    };

    axios.post(_.LOGIN_USER,  loginRequest, _.HEADER_DATA)
      .then(response => {
          if(response.data.flag){

            localStorage.setItem("token", "Bearer " + response.data.token);
            localStorage.setItem("role", response.data.roles[0]);
            localStorage.setItem("id", response.data.id);
            localStorage.setItem("username", response.data.username);

            if(response.data.roles[0] === 'ROLE_ADMIN') {
              window.location.href = '/dashboard/admin';
            }
            else if(response.data.roles[0] === 'ROLE_USER') {
              window.location.href = '/dashboard/user/'+response.data.id;
            }
            else {
              setErrorMessage('role implementation not found, Kindly contact admin');
            }
          }
          else {
            setErrorMessage(response.data.message);
          }
      })
      .catch(error =>{
        setErrorMessage("username or password is incorrect.");
        console.log(error);
      });
  }

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}><LockOutlinedIcon /></Avatar>
        <Typography component="h1" variant="h5">Sign in</Typography>
        <form className={classes.form} onSubmit={handleSubmit}>
          <TextField name="username" label="Username" type="text" id="username" variant="outlined" fullWidth autoComplete="username" onChange={(event) => setUsername(event.target.value)} margin="normal" autoFocus required/>
          <TextField name="password" label="Password" type="password" id="password" variant="outlined" fullWidth autoComplete="password" onChange={(event) => setPassword(event.target.value)} margin="normal" autoFocus required/>
          {loadActionStartup()}
          <FormHelperText id="my-helper-text" style={{color:"red"}}>{errorMessage}</FormHelperText>
          <Button type="submit" fullWidth variant="contained" color="primary" className={classes.submit}
          >Sign In</Button>
          <Grid container>
            <Grid item xs>
              <Link href="/fetch" variant="body2">Fetch Username</Link>
            </Grid>
            <Grid item>
              <Link href="/registration" variant="body2">{"Don't have an account? Sign Up"}</Link>
            </Grid>
          </Grid>
        </form>
      </div>
      <Box mt={8}>
        <Typography variant="body2" color="textSecondary" align="center">
          {'Copyright © '}<Link color="inherit" href="/">Ui</Link>{' '}{new Date().getFullYear()}{'.'}
        </Typography>
      </Box>
    </Container>
  );
}