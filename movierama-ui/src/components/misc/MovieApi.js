import axios from 'axios'
import {config} from '../../Constants'
import {parseJwt} from './Helpers'

export const movieApi = {
    authenticate,
    signup,
    getUsers,
    deleteUser,
    getMovies,
    deleteMovie,
    addMovie,
    addVote
}

function authenticate(username, password) {
    return instance.post('/auth/login', {username, password}, {
        headers: {'Content-type': 'application/json'}
    })
}

function signup(user) {
    return instance.post('/auth/signup', user, {
        headers: {'Content-type': 'application/json'}
    })
}

function getUsers(user, id) {
    const url = id ? `/api/v1/users/${id}` : '/api/v1/users'
    return instance.get(url, {
        headers: {'Authorization': bearerAuth(user)}
    })
}

function deleteUser(user, id) {
    return instance.delete(`/api/v1/users/${id}`, {
        headers: {'Authorization': bearerAuth(user)}
    })
}

function getMovies(userId, term, direction) {
    term = term ? term : "PUBLICATION_DATE"
    direction = direction ? direction : "DESC"
    const url = userId ? `/api/v1/movies?userId=${userId}&term=${term}&direction=${direction}` : `/api/v1/movies?term=${term}&direction=${direction}`
    return instance.get(url)
}

function deleteMovie(user, id) {
    return instance.delete(`/api/v1/movies/${id}`, {
        headers: {'Authorization': bearerAuth(user)}
    })
}

function addMovie(user, movie) {
    return instance.post('/api/v1/movies', movie, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': bearerAuth(user)
        }
    })
}

function addVote(user, vote) {
    return instance.put('/api/v1/votes', vote, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': bearerAuth(user)
        }
    })
}

// -- Axios

const instance = axios.create({
    baseURL: config.url.API_BASE_URL
})

instance.interceptors.request.use(function (config) {
    // If token is expired, redirect user to login
    if (config.headers.Authorization) {
        const token = config.headers.Authorization.split(' ')[1]
        const data = parseJwt(token)
        if (Date.now() > data.exp * 1000) {
            window.location.href = "/login"
        }
    }
    return config
}, function (error) {
    return Promise.reject(error)
})

// -- Helper functions

function bearerAuth(user) {
    return `Bearer ${user.token}`
}