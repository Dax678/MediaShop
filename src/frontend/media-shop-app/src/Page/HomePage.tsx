import React, {useEffect, useState} from 'react';
import {Container, Carousel, Button, Card, Row, Col, Spinner} from 'react-bootstrap';
import apiProductService from "../Service/api-product.service";
import Product from "../Interface/Product";

export function HomePage() {
    const [products, setProducts] = useState<Product[]>([]);

    const [dailyPromotion, setDailyPromotion] = useState({
        product: {
            name: '',
            shortDescription: '',
            unitPrice: 0,
        },
        discount: {
            value: 0,
        },
    });

    const [dailyPromotionLoading, setDailyPromotionLoading] = useState(false);
    const [recommendedProductsLoading, setRecommendedProductsLoading] = useState(false);

    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const [currentPage, setCurrentPage] = useState(0);
    const [itemsPerPage] = useState(3);
    const [totalPages, setTotalPages] = useState(2);

    useEffect(() => {
        const filters = {
            isAvailable: true,
            page: 0,
            size: 20,
            sortedBy: 'price',
            sortDirection: 'DESC'
        };
        const fetchProducts = async () => {
            setRecommendedProductsLoading(true);
            const response = await apiProductService.getProductsByCategoryNameFilter("Laptopy 2 w 1", filters)
                .then(
                    (response) => {
                        console.log(response.data);
                        setProducts(response.data.product);
                        setRecommendedProductsLoading(false);
                    },
                    (error) => {
                        setErrorMessage(error.message);
                        setRecommendedProductsLoading(false);
                    })
        }

        const fetchDailyPromotion = async () => {
            setDailyPromotionLoading(true);
            const response = await apiProductService.getProductByDiscount("DAILY_PROMOTION")
                .then(
                    (response) => {
                        console.log(response.data);
                        setDailyPromotion(response.data);
                        setDailyPromotionLoading(false);
                    },
                    (error) => {
                        setErrorMessage(error.message);
                        setDailyPromotionLoading(false);
                    })
        }


        fetchProducts();
        fetchDailyPromotion();
    }, [])

    const indexOfLastItem = (currentPage + 1) * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = products.slice(indexOfFirstItem, indexOfLastItem);

    const handleNextPage = () => {
        setCurrentPage(prevPage => prevPage + 1);
    };

    const handlePrevPage = () => {
        if (currentPage > 0) {
            setCurrentPage(prevPage => prevPage - 1);
        }
    };

    return (
        <>
            <Row>
                <Col xs={6} md={6}>
                    <Container className="px-5">
                        <h1 className="pb-3">Codzienna Promocja: </h1>
                        {recommendedProductsLoading ? (
                            <Spinner animation="border" role="status">
                                <span className="visually-hidden">Loading...</span>
                            </Spinner>
                        ) : errorMessage ? (
                            <p>Error: {errorMessage}</p>
                        ) : (
                            <>
                                <Row>
                                    <Col sm={1} md={2} lg={4} xl={6} xxl={6} className="mb-4" style={{ width: '35rem' }}>
                                        <Card border="warning" bg='warning' text='black' className="d-flex flex-row">
                                            <Card.Img variant="left" src="/Image/example2.jpg" style={{ width: '250px', height: 'auto' }} />
                                            <Card.Body>
                                                <Card.Title>{dailyPromotion.product.name}</Card.Title>
                                                <Card.Text>{dailyPromotion.product.shortDescription}</Card.Text>
                                                <Card.Text><del>Cena przed obniżką: {dailyPromotion.product.unitPrice} PLN</del></Card.Text>
                                                <Card.Text>Aktualna cena: {dailyPromotion.product.unitPrice - dailyPromotion.discount.value} PLN</Card.Text>
                                                <Button variant="dark">Open</Button>
                                            </Card.Body>
                                        </Card>
                                    </Col>
                                </Row>
                            </>
                            )}
                            </Container>
                            </Col>
                            <Col xs={12} md={6}>
                        <Container className="px-5">
                            <h1 className="pb-3">Polecane produkty: </h1>
                            {recommendedProductsLoading ? (
                                <Spinner animation="border" role="status">
                                    <span className="visually-hidden">Loading...</span>
                                </Spinner>
                            ) : errorMessage ? (
                                <p>Error: {errorMessage}</p>
                            ) : (
                                <>
                                    <Row>
                                        {currentItems.map((product) => (
                                            <Col key={product.id} sm={1} md={2} lg={4} xl={6} xxl={6} className="mb-4"
                                                 style={{width: '15rem'}}>
                                                <Card bg='secondary' text='white'>
                                                    <Card.Img variant="top" src="/Image/example2.jpg"/>
                                                    <Card.Body>
                                                        <Card.Title>{product.name}</Card.Title>
                                                        <Card.Text>{product.shortDescription}</Card.Text>
                                                        <Card.Text>{product.unitPrice} PLN</Card.Text>
                                                        <Button variant="light">Open</Button>
                                                    </Card.Body>
                                                </Card>
                                            </Col>
                                        ))}
                                    </Row>
                                    <div className="d-flex justify-content-center mt-3">
                                        <Button variant="primary" className="me-2" onClick={handlePrevPage}
                                                disabled={currentPage === 0}>Poprzedni</Button>
                                        <Button variant="primary" onClick={handleNextPage}
                                                disabled={currentPage === totalPages}>Następny</Button>
                                    </div>
                                </>
                            )}
                        </Container>
                </Col>
            </Row>
            <Container>

            </Container>
        </>
)
}