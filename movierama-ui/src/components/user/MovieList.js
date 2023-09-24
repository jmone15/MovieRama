import React from 'react'
import {Grid, Header, Icon, Image, Item, Segment, Button} from 'semantic-ui-react'
import {Link} from "react-router-dom";

function MovieList({isMoviesLoading, movies, isAuthenticated, handleVoting}) {
    let movieList


    if (movies.length === 0) {
        movieList = <Item key='no-movie'>No Movie</Item>
    } else {
        movieList = movies.map(movie => {
            return (
                <Item key={movie.externalId}>
                    <Image src='/images/movie-poster.jpg' size='small' bordered rounded/>
                    <Item.Content>
                        <Item.Header>{movie.title}</Item.Header>
                        <Item.Extra>Posted by {isAuthenticated ?
                                <Link to={`/userprofile/${movie.publisher.externalId}`}>{movie.publisher.name}</Link> :
                                 movie.publisher.name
                            }

                        </Item.Extra>
                        <Item.Description>
                            {movie.description}
                        </Item.Description>
                        <Item.Meta>{new Date(movie.publicationDate).toLocaleDateString()}</Item.Meta>
                        {isAuthenticated ? <Button
                            color='blue'
                            content='Like'
                            icon='thumbs up'
                            label={{basic: true, color: 'blue', pointing: 'left', content: movie.likes}}
                            onClick={() => handleVoting(movie.externalId, true)}

                        /> : <Button
                            content='Like'
                            icon='thumbs up'
                            label={{as: 'a', basic: true, content: movie.likes}}

                        />}
                        {isAuthenticated ?
                            <Button
                                color='red'
                                content='Hate'
                                icon='thumbs down'
                                label={{
                                    as: 'a',
                                    basic: true,
                                    color: 'red',
                                    pointing: 'left',
                                    content: movie.hates
                                }}
                                onClick={() => handleVoting(movie.externalId, false)}
                            /> : <Button
                                content='Hate'
                                icon='thumbs down'
                                label={{as: 'a', basic: true, pointing: 'left', content: movie.hates}}
                            />}
                    </Item.Content>
                </Item>
            )
        })
    }

    return (
        <Segment loading={isMoviesLoading} color='blue'>
            <Grid stackable divided>
                <Grid.Row columns='2'>
                    <Grid.Column width='3'>
                        <Header as='h2'>
                            <Icon name='video camera'/>
                            <Header.Content>Movies</Header.Content>
                        </Header>
                    </Grid.Column>
                </Grid.Row>
            </Grid>
            <Item.Group divided unstackable relaxed link>
                {movieList}
            </Item.Group>
        </Segment>
    )
}

export default MovieList