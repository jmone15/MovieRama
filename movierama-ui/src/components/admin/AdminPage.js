import React, {useEffect, useState} from 'react'
import {Navigate} from 'react-router-dom'
import {Container} from 'semantic-ui-react'
import {useAuth} from '../context/AuthContext'
import AdminTab from './AdminTab'
import {movieApi} from '../misc/MovieApi'
import {handleLogError} from '../misc/Helpers'

function AdminPage() {
    const Auth = useAuth()
    const user = Auth.getUser()
    const isAdmin = user.data.rol[0] === 'ROLE_ADMIN'

    const [users, setUsers] = useState([])
    const [userUsernameSearch, setUserUsernameSearch] = useState('')
    const [isUsersLoading, setIsUsersLoading] = useState(false)

    useEffect(() => {
        handleGetUsers()
    }, [])

    const handleInputChange = (e, {name, value}) => {
        if (name === 'userUsernameSearch') {
            setUserUsernameSearch(value)
        }
    }

    const handleGetUsers = async () => {
        try {
            setIsUsersLoading(true)
            const response = await movieApi.getUsers(user)
            setUsers(response.data)
        } catch (error) {
            handleLogError(error)
        } finally {
            setIsUsersLoading(false)
        }
    }

    const handleDeleteUser = async (externalId) => {
        try {
            await movieApi.deleteUser(user, externalId)
            await handleGetUsers()
        } catch (error) {
            handleLogError(error)
        }
    }

    const handleSearchUser = async () => {
        try {
            const response = await movieApi.getUsers(user, userUsernameSearch)
            const data = response.data
            const users = Array.isArray(data) ? data : [data]
            setUsers(users)
        } catch (error) {
            handleLogError(error)
            setUsers([])
        }
    }

    if (!isAdmin) {
        return <Navigate to='/'/>
    }

    return (
        <Container>
            <AdminTab
                isUsersLoading={isUsersLoading}
                users={users}
                userUsernameSearch={userUsernameSearch}
                handleDeleteUser={handleDeleteUser}
                handleSearchUser={handleSearchUser}
                handleInputChange={handleInputChange}
            />
        </Container>
    )
}

export default AdminPage