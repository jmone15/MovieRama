import React from 'react'
import {Form, Icon, Button} from 'semantic-ui-react'

function MovieForm({movieTitle, movieDescription, handleInputChange, handleAddMovie}) {
    const createBtnDisabled = movieTitle.trim() === '' || movieTitle.trim() === ''
    return (
        <Form onSubmit={handleAddMovie}>
            <Form.Group>
                <Form.Input
                    name='movieTitle'
                    placeholder='Title *'
                    value={movieTitle}
                    onChange={handleInputChange}
                />
                <Form.Input
                    name='movieDescription'
                    placeholder='Description'
                    value={movieDescription}
                    onChange={handleInputChange}
                />
                <Button icon labelPosition='right' disabled={createBtnDisabled}>
                    Create<Icon name='add'/>
                </Button>
            </Form.Group>
        </Form>
    )
}

export default MovieForm
