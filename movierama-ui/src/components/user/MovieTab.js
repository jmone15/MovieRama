import React from 'react'
import {Tab} from 'semantic-ui-react'
import MovieTable from './MovieTable'

function MovieTab(props) {
    const {handleInputChange} = props
    const {
        isMoviesLoading,
        movies,
        movieTitle,
        movieDescription,
        handleAddMovie,
        handleDeleteMovie,
    } = props

    const panes = [
        {
            menuItem: {key: 'movies', icon: 'video camera', content: 'Movies'},
            render: () => (
                <Tab.Pane loading={isMoviesLoading}>
                    <MovieTable
                        movies={movies}
                        movieTitle={movieTitle}
                        movieDescription={movieDescription}
                        handleInputChange={handleInputChange}
                        handleAddMovie={handleAddMovie}
                        handleDeleteMovie={handleDeleteMovie}
                    />
                </Tab.Pane>
            )
        }
    ]

    return (
        <Tab menu={{attached: 'top'}} panes={panes}/>
    )
}

export default MovieTab