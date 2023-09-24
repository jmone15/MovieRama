import React, {useEffect, useState} from 'react'
import {Button, Container, Form, Icon} from 'semantic-ui-react'
import {movieApi} from '../misc/MovieApi'
import {handleLogError} from '../misc/Helpers'
import {useAuth} from "../context/AuthContext";
import MovieList from "../user/MovieList";

function Home() {
    const Auth = useAuth()
    const user = Auth.getUser()
    const isAuthenticated = Auth.userIsAuthenticated()

    const [movies, setMovies] = useState([])
    const [isMoviesLoading, setIsMoviesLoading] = useState(false)

    const [term, setTerm] = useState('')
    const [direction, setDirection] = useState('')

    const directions = [
        {key: 'asc', text: 'Ascending', value: 'ASC'},
        {key: 'desc', text: 'Descending', value: 'DESC'},
    ]

    const terms = [
        {key: 'pub', text: 'Publication Date', value: 'PUBLICATION_DATE'},
        {key: 'like', text: 'Likes', value: 'LIKES'},
        {key: 'hate', text: 'Hates', value: 'HATES'},
    ]

    useEffect(() => {
        handleGetMovies()
    }, [])

    const handleGetMovies = async () => {
        setIsMoviesLoading(true)
        try {
            const response = await movieApi.getMovies(null, null, null)
            setMovies(response.data)
        } catch (error) {
            handleLogError(error)
        } finally {
            setIsMoviesLoading(false)
        }
    }

    const handleInputChange = (e, {name, value}) => {
        if (name === 'term') {
            setTerm(value)
        } else if (name === 'direction') {
            setDirection(value)
        }
    }

    const handleSortMovies = async () => {
        try {
            const response = await movieApi.getMovies(null, term, direction)
            setMovies(response.data)
        } catch (error) {
            handleLogError(error)
        } finally {
            setIsMoviesLoading(false)
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

    return (
        <Container>
            <Form onSubmit={handleSortMovies}>
                <Form.Group widths='equal'>
                    <Form.Select
                        fluid
                        name='term'
                        label='Term'
                        options={terms}
                        placeholder='Publication date'
                        onChange={handleInputChange}
                    />
                    <Form.Select
                        fluid
                        name='direction'
                        label='Direction'
                        options={directions}
                        placeholder='Ascending'
                        onChange={handleInputChange}
                    />
                    <Button icon labelPosition='right'>Sort<Icon name='search'/></Button>
                </Form.Group>
            </Form>
            <MovieList
                isMoviesLoading={isMoviesLoading}
                movies={movies}
                isAuthenticated={isAuthenticated}
                handleVoting={handleVoting}
            />
        </Container>
    )
}

export default Home