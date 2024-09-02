import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {NavigationBar} from "./Component/NavigationBar";
import {Col, Container, Row} from 'react-bootstrap';
import {HomePage} from "./Page/HomePage";

function App() {
    return (
        <>
            <Container fluid>
                <Row>
                    <Col className="px-0">
                        <NavigationBar/>
                    </Col>
                </Row>
                <Row className="py-5" style={{ backgroundColor: '#f6f6f6' }}>
                    <Col>
                        <Container fluid className="mt-4">
                            <HomePage></HomePage>
                        </Container>
                    </Col>
                </Row>
            </Container>
        </>
    );
}

export default App;
