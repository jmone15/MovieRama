import React, {useEffect, useState} from 'react'
import {Navigate} from 'react-router-dom'
import {Container} from 'semantic-ui-react'
import {useAuth} from '../context/AuthContext'
import {movieApi} from '../misc/MovieApi'
import {handleLogError} from '../misc/Helpers'
import MovieTab from "./MovieTab";

function MoviePage() {
    const Auth = useAuth()
    const user = Auth.getUser()
    const isUser = user.data.rol[0] === 'ROLE_USER'

    const [movies, setMovies] = useState([])
    const [movieTitle, setMovieTitle] = useState('')
    const [movieDescription, setMovieDescription] = useState('')
    const [isMoviesLoading, setIsMoviesLoading] = useState(false)

    useEffect(() => {
        handleGetMovies()
    }, [])

    const handleInputChange = (e, {name, value}) => {
        if (name === 'movieTitle') {
            setMovieTitle(value)
        } else if (name === 'movieDescription') {
            setMovieDescription(value)
        }
    }

    const handleGetMovies = async () => {
        try {
            setIsMoviesLoading(true)
            const response = await movieApi.getMovies(user.data.externalId, "PUBLICATION_DATE", "DESC")
            setMovies(response.data)
        } catch (error) {
            handleLogError(error)
        } finally {
            setIsMoviesLoading(false)
        }
    }

    const handleDeleteMovie = async (externalId) => {
        try {
            await movieApi.deleteMovie(user, externalId)
            await handleGetMovies()
        } catch (error) {
            handleLogError(error)
        }
    }

    const handleAddMovie = async () => {
        const trimmedTitle = movieTitle.trim()
        const trimmedDescription = movieDescription.trim()

        if (!(trimmedTitle && trimmedDescription)) {
            return
        }

        const movie = {title: trimmedTitle, description: trimmedDescription}

        try {
            await movieApi.addMovie(user, movie)
            clearMovieForm()
            await handleGetMovies()
        } catch (error) {
            handleLogError(error)
        }
    }

    const clearMovieForm = () => {
        setMovieTitle('')
        setMovieDescription('')
    }

    if (!isUser) {
        return <Navigate to='/'/>
    }

    return (
        <Container>
            <MovieTab
                isMoviesLoading={isMoviesLoading}
                movies={movies}
                movieTitle={movieTitle}
                movieDescription={movieDescription}
                handleAddMovie={handleAddMovie}
                handleDeleteMovie={handleDeleteMovie}
                handleInputChange={handleInputChange}
            />
        </Container>
    )
}

export default MoviePage