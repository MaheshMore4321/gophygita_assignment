import React, { useState, useEffect } from 'react';
import * as _ from "../constants/ApiUrls";
import axios from 'axios';
import { Avatar, CssBaseline, Link, Paper, Box, Typography, Container, Button,
         Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import SupervisorAccountIcon from '@material-ui/icons/SupervisorAccount';
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

  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  const userId = localStorage.getItem('id');


  console.log(reducer.authorization);
  console.log(reducer.authorization.id);
  console.log(reducer.authorization.username);
  console.log(reducer.authorization.role);
  console.log(reducer.authorization.token);

  const callApiLoadData = () => {
    axios.get(_.AUTH_CHECK_URL, _.HEADER_DATA(token))
      .then(response =>{
        if(response.data.flag) {
          const obj = response.data.object;
          if(userId !== obj.userId && role !== obj.roles[0]){
            window.location.href = '/';
          }
          else{
            loadUserData();
          }
        }
        else{
          window.location.href = '/';
        }
      })
      .catch(error =>{ window.location.href = '/'; });
  }

  const [userList, setUserList] = useState([]);
  const loadUserData = () => {
    axios.get(_.ADMIN_PANEL, _.HEADER_DATA(token))
      .then(responsex =>{
        setUserList(responsex.data)
      })
      .catch(error =>{ window.location.href = '/'; });
  }

  const toggleUserStatus = (userId) => {
    axios.post(_.TOGGLE_ACTIVE_USER + userId, {}, _.HEADER_DATA(token))
      .then(response =>{ if(response.data.flag) { loadUserData(); } })
      .catch(error =>{ console.log(error) });
  }

  useEffect(callApiLoadData, []);

  return (
    <Container component="main" fullWidth>
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}><SupervisorAccountIcon/></Avatar>
        <Typography component="h1" variant="h5">Admin Panel</Typography>
        <Box mt={5}></Box>
        <TableContainer component={Paper}>
          <Table aria-label="simple table" style={{minWidth: 650}}>
            <TableHead>
              <TableRow>
                <TableCell>Sr. No</TableCell>
                <TableCell align="right">Name</TableCell>
                <TableCell align="right">Email</TableCell>
                <TableCell align="right">Created Date</TableCell>
                <TableCell align="right">Action(enable/disable users)</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {userList && Array.from(userList).map((row) => (
                <TableRow key={row.rowId}>
                  <TableCell component="th" scope="row">{row.rowId}</TableCell>
                  <TableCell align="right">{row.name}</TableCell>
                  <TableCell align="right">{row.username}</TableCell>
                  <TableCell align="right">{row.createdTs}</TableCell>
                  <TableCell align="right"><Button variant="contained" color={row.active ? "primary" : "secondary" } onClick={() => toggleUserStatus(row.userId)}>{row.active ? "Deactive User" : "Active User" } </Button></TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </div>
      <Box mt={5}>
        <Typography variant="body2" color="textSecondary" align="center">
          {'Copyright © '}<Link color="inherit" href="/">Ui</Link>{' '}{new Date().getFullYear()}{'.'}
        </Typography>
      </Box>
    </Container>
  );
}