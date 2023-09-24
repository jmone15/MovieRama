import React, {useEffect, useState} from 'react'
import {Navigate, useParams} from 'react-router-dom'
import {Container, Card, Image, Icon} from 'semantic-ui-react'
import MovieList from './MovieList'
import {useAuth} from '../context/AuthContext'
import {movieApi} from '../misc/MovieApi'
import {handleLogError} from '../misc/Helpers'

function UserPage() {
    const { id } = useParams()
    const Auth = useAuth()
    const user = Auth.getUser()
    const isAuthenticated = Auth.userIsAuthenticated()
    const isUser = user.data.rol[0] === 'ROLE_USER'
    const [profile, setUserProfile] = useState([])
    const [movies, setMovies] = useState([])
    const [isMoviesLoading, setIsMoviesLoading] = useState(false)

    useEffect(() => {
        handleGetMovies()
        handleGetUsers()
    }, [])

    const handleGetMovies = async () => {
        setIsMoviesLoading(true)
        try {
            const response = await movieApi.getMovies(id, "PUBLICATION_DATE", "DESC")
            setMovies(response.data)
        } catch (error) {
            handleLogError(error)
        } finally {
            setIsMoviesLoading(false)
        }
    }

    const handleGetUsers = async () => {
        try {
            const response = await movieApi.getUsers(user, id)
            setUserProfile(response.data)
        } catch (error) {
            handleLogError(error)
        }
    }

    const handleVoting = async (movieId, givenVote) => {
        try {
            const vote = {
                movie: movieId,
                like: givenVote
            }
            await movieApi.addVote(user, vote)
            await handleGetMovies()
        } catch (error) {
            handleLogError(error)
        }
    }

    if (!isUser) {
        return <Navigate to='/'/>
    }

    return (
        <Container>
            <Card>
                <Image src='https://react.semantic-ui.com/images/avatar/large/daniel.jpg' wrapped ui={true}/>
                <Card.Content>
                    <Card.Header>{profile.name}</Card.Header>
                    <Card.Meta>
                        <span className='email'>{profile.email}</span>
                    </Card.Meta>
                </Card.Content>
                <Card.Content extra>
                    <a>
                        <Icon name='video camera'/>
                        {movies.length} Movies Published
                    </a>
                </Card.Content>
            </Card>
            <MovieList
                isMoviesLoading={isMoviesLoading}
                movies={movies}
                isAuthenticated={isAuthenticated}
                handleVoting={handleVoting}
            />
        </Container>
    )
}

export default UserPage