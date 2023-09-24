import React from 'react'
import {Link} from 'react-router-dom'
import {Container, Menu} from 'semantic-ui-react'
import {useAuth} from '../context/AuthContext'
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

function Navbar() {
    const {getUser, userIsAuthenticated, userLogout} = useAuth()

    const logout = () => {
        userLogout()
    }

    const enterMenuStyle = () => {
        return userIsAuthenticated() ? {"display": "none"} : {"display": "block"}
    }

    const logoutMenuStyle = () => {
        return userIsAuthenticated() ? {"display": "block"} : {"display": "none"}
    }

    const adminPageStyle = () => {
        const user = getUser()
        return user && user.data.rol[0] === 'ROLE_ADMIN' ? {"display": "block"} : {"display": "none"}
    }

    const userPageStyle = () => {
        const user = getUser()
        return user && user.data.rol[0] === 'ROLE_USER' ? {"display": "block"} : {"display": "none"}
    }

    const getUserName = () => {
        const user = getUser()
        return user ? user.data.name : ''
    }

    const getUserId = () => {
        const user = getUser()
        return user ? user.data.externalId : ''
    }

    return (
        <Menu inverted color='blue' stackable size='massive' style={{borderRadius: 0}}>
            <Container>
                <Menu.Item header>MovieRama</Menu.Item>
                <Menu.Item as={Link} exact='true' to="/">Home</Menu.Item>
                <Menu.Item as={Link} to="/adminpage" style={adminPageStyle()}>User Management</Menu.Item>
                <Menu.Item as={Link} to="/moviepage" style={userPageStyle()}>Movies Management</Menu.Item>
                <Menu.Item as={Link} to={`/userprofile/${getUserId()}`} style={userPageStyle()}>User profile</Menu.Item>
                <Menu.Menu position='right'>
                    <Menu.Item as={Link} to="/login" style={enterMenuStyle()}>Login</Menu.Item>
                    <Menu.Item as={Link} to="/signup" style={enterMenuStyle()}>Sign Up</Menu.Item>
                    <Menu.Item header style={logoutMenuStyle()}>{`Hi ${getUserName()}`}</Menu.Item>
                    <Menu.Item as={Link} to="/" style={logoutMenuStyle()} onClick={logout}>Logout</Menu.Item>
                </Menu.Menu>
            </Container>
            <ToastContainer
                position="top-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
                theme="dark"/>
        </Menu>
    )
}

export default Navbar
