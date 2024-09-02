import React, {useState} from 'react';
import {NavDropdown, Navbar, Container, Nav, Form, Button} from 'react-bootstrap';
import {FaUser, FaShoppingCart} from 'react-icons/fa';
import {Modal} from 'react-bootstrap';
import AuthService from "../Service/auth.service";

export function NavigationBar() {
    const [showLoginModal, setShowLoginModal] = useState(false);
    const [showRegisterModal, setShowRegisterModal] = useState(false);

    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [validated, setValidated] = useState<boolean>(false);

    const handleLogin = (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);

        AuthService.login(username, password).then(
            () => {
                window.location.reload();
            },
            (error) => {
                alert('Failed to login');
                setLoading(false);
            }
        )
    };

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        AuthService.register(email, username, password, firstName, lastName).then(
            (response) => {
                setSuccessMessage(response.message);
                console.log(response.message);

                setFirstName('');
                setLastName('');
                setEmail('');
                setPassword('');
            },
            (error) => {
                if (error.response) {
                    setErrorMessage(error.response.message);
                    console.log(error.response);
                }
            }
        )
    };

    return (
        <>
            <Modal
                size="lg"
                show={showLoginModal}
                onHide={() => setShowLoginModal(false)}
                aria-labelledby="example-modal-sizes-title-sm"
            >
                <Modal.Header closeButton>
                    <Modal.Title id="example-modal-sizes-title-sm">
                        Formularz Logowania
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleLogin}>
                        <Form.Group controlId="formLogin">
                            <Form.Label>Login</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter login"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formPassword" className="mt-3">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="mt-4">
                            Zaloguj się
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>

            <Modal
                size="lg"
                show={showRegisterModal}
                onHide={() => setShowRegisterModal(false)}
                aria-labelledby="registration-modal-title"
            >
                <Modal.Header closeButton>
                    <Modal.Title id="registration-modal-title">
                        Formularz Rejestracji
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formFirstName">
                            <Form.Label>Imię</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Podaj swoje imię"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                Proszę podać swoje imię.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="formLastName">
                            <Form.Label>Nazwisko</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Podaj swoje nazwisko"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                Proszę podać swoje nazwisko.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="formEmail">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                placeholder="Podaj swój email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                Proszę podać poprawny adres email.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="formUsername">
                            <Form.Label>Username</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Podaj swój username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                Proszę podać swoje imię.
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group controlId="formPassword">
                            <Form.Label>Hasło</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Podaj swoje hasło"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                minLength={8}
                                required
                            />
                            <Form.Text className="text-muted">
                                Hasło musi mieć co najmniej 8 znaków.
                            </Form.Text>
                            <Form.Control.Feedback type="invalid">
                                Proszę podać hasło (min. 8 znaków).
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Button variant="primary" type="submit">
                            Zarejestruj się
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal>

            <Navbar expand="lg" className="bg-body-tertiary flex-column px-3" bg="primary" data-bs-theme="dark" >
                <Container fluid>
                    <Navbar.Brand href="#home">
                        <img
                            alt=""
                            src="/logo192.png"
                            width="30"
                            height="30"
                            className="d-inline-block align-top"
                        />{' '}
                        Shop Media App
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="navbarScroll"/>
                    <Navbar.Collapse id="navbarScroll">
                        <Container className="d-flex justify-content-center">
                            <Form className="d-flex">
                                <Form.Control
                                    type="search"
                                    placeholder="Search"
                                    className="me-2"
                                    aria-label="Search"
                                />
                                <Button variant="outline-success">Search</Button>
                            </Form>
                        </Container>
                        <Nav className="ms-auto">
                            <NavDropdown
                                title={<FaUser size={30}/>}
                                id="user-dropdown"
                                align="end"
                                className="d-flex align-items-center"
                            >
                                <NavDropdown.Item onClick={() => setShowLoginModal(true)}>Logowanie</NavDropdown.Item>
                                <NavDropdown.Item
                                    onClick={() => setShowRegisterModal(true)}>Rejestracja</NavDropdown.Item>
                            </NavDropdown>
                            <Nav.Link href="#cart" className="d-flex align-items-center">
                                <FaShoppingCart size={30}/>
                            </Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
                <Container fluid className="px-3">
                    <Navbar.Toggle aria-controls="navbarScroll"/>
                    <Navbar.Collapse id="navbarScroll">
                        <Nav
                            className="me-auto my-2 my-lg-0"
                            style={{maxHeight: '100px'}}
                            navbarScroll
                        >
                            <NavDropdown title="Laptopy i komputery" id="navbarScrollingDropdown">
                                <NavDropdown.Item href="#action3">Laptopy/Notebooki/Ultrabooki</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Laptopy 2 w 1</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Laptopy biznesowe</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Komputery stacjonarne</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Komputery G4M3R</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Tablety</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Serwery i storage</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Programy</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Akcesoria komputerowe</NavDropdown.Item>
                                <NavDropdown.Divider/>
                                <NavDropdown.Item href="#action5">
                                    Więcej
                                </NavDropdown.Item>
                            </NavDropdown>
                            <NavDropdown title="Smartfony i smartwatche" id="navbarScrollingDropdown">
                                <NavDropdown.Item href="#action3">Smartfony i telefony</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Tablety</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Inteligentne zegarki</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Czytniki ebook</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Ochrona na telefon</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Ładowarki i powerbanki</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Karty pamięci microSD</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Słuchawki True Wireless</NavDropdown.Item>
                                <NavDropdown.Divider/>
                                <NavDropdown.Item href="#action5">
                                    Więcej
                                </NavDropdown.Item>
                            </NavDropdown>
                            <NavDropdown title="Podzespoły komputerowe" id="navbarScrollingDropdown">
                                <NavDropdown.Item href="#action3">Polecane zestawy komputerowe</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Dyski twarde HDD i SSD</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Karty graficzne</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Procesory</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Płyty główne</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Obudowy komputerowe</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Pamięci RAM</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Zasilacze komputerowe</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Chłodzenia komputerowe</NavDropdown.Item>
                                <NavDropdown.Divider/>
                                <NavDropdown.Item href="#action5">
                                    Więcej
                                </NavDropdown.Item>
                            </NavDropdown>
                            <NavDropdown title="Gaming i streaming" id="navbarScrollingDropdown">
                                <NavDropdown.Item href="#action3">Laptopy do gier</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Komputery do gier</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Podzespoły do gier</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Monitory dla graczy</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Konsole</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Myszki i klawiatury dla graczy</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Audio dla graczy</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Meble gamingowe</NavDropdown.Item>
                                <NavDropdown.Divider/>
                                <NavDropdown.Item href="#action5">
                                    Więcej
                                </NavDropdown.Item>
                            </NavDropdown>
                            <NavDropdown title="Urządzenia peryferyjne" id="navbarScrollingDropdown">
                                <NavDropdown.Item href="#action3">Monitory</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Drukarki</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Drukarki ze skanerem</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Urządzenia sieciowe</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Myszki</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Klawiatury</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Słuchawki</NavDropdown.Item>
                                <NavDropdown.Item href="#action3">Głośniki</NavDropdown.Item>
                                <NavDropdown.Divider/>
                                <NavDropdown.Item href="#action5">
                                    Więcej
                                </NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        </>
    );
}