import React, { useState, useEffect } from 'react';
import * as _ from "../constants/ApiUrls";
import axios from 'axios';
import { Avatar, CssBaseline, Link, Grid, Box, Typography, Container } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import * as methd from '../utility/Utility';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import { useParams } from 'react-router-dom';
import { useSelector } from "react-redux";

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

export default function UserPanel() {
  const classes = useStyles();

  const reducer = useSelector(state => state);

  const { userId } = useParams();

  const [jsonData, setJsonData] = useState({
    userId: '',
    name: '',
    username: '',
    language: '',
    mobileNo: '',
    active: '',
    createdTs: '',
  });

  const callApiLoadData = () => {
    const token = localStorage.getItem('token');
    const id = localStorage.getItem('id');

    console.log(reducer.authorization);
    console.log(reducer.authorization.id);
    console.log(reducer.authorization.username);
    console.log(reducer.authorization.role);
    console.log(reducer.authorization.token);

    if(userId !== id){
      window.location.href = '/';
    }

    axios.get(_.USER_PANEL + userId, _.HEADER_DATA(token))
      .then(response =>{
        const obj = response.data;
        setJsonData({
          userId: obj.userId,
          name: obj.name,
          username: obj.username,
          language: obj.language,
          mobileNo: obj.mobileNo,
          active: obj.active,
          createdTs: obj.createdTs
        });
      })
      .catch(error =>{ window.location.href = '/'; });
  }

  useEffect(callApiLoadData, []);

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}><AccountCircleIcon /></Avatar>
        <Typography component="h1" variant="h5">User Panel</Typography>
        <Grid container spacing={2}>
          { jsonData.language === "EN" ? methd.getEnglishUserPanel(jsonData) :
            jsonData.language === 'DE' ? methd.getDutchUserPanel(jsonData) : "NO DATA"}
        </Grid>
      </div>
      <Box mt={5}>
        <Typography variant="body2" color="textSecondary" align="center">
          {'Copyright © '}<Link color="inherit" href="/">Ui</Link>{' '}{new Date().getFullYear()}{'.'}
        </Typography>
      </Box>
    </Container>
  );
}