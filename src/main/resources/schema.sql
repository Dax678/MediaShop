CREATE TABLE IF NOT EXISTS public.product
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255)     NOT NULL,
    description       TEXT             NOT NULL,
    short_description TEXT             NOT NULL,
    brand             VARCHAR(255)     NOT NULL,
    image             VARCHAR(255)     NOT NULL,
    unit_price        DOUBLE PRECISION NOT NULL,
    quantity_per_unit INTEGER          NOT NULL
);

CREATE TABLE IF NOT EXISTS public.category
(
    id               SERIAL PRIMARY KEY,
    parent_id        INTEGER,
    title            VARCHAR(255) NOT NULL,
    description      TEXT         NOT NULL,
    meta_title       VARCHAR(255) NOT NULL,
    meta_description TEXT         NOT NULL,
    meta_keywords    TEXT         NOT NULL,
    slug             VARCHAR(255) NOT NULL,
    FOREIGN KEY (parent_id) REFERENCES public.category (id)
);

CREATE TABLE IF NOT EXISTS public.attribute
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.discount
(
    id                  SERIAL PRIMARY KEY,
    value               DOUBLE PRECISION NOT NULL,
    code                VARCHAR(255)     NOT NULL,
    start_date          DATE             NOT NULL,
    end_date            DATE             NOT NULL,
    max_usage           INTEGER          NOT NULL,
    min_purchase_amount DOUBLE PRECISION NOT NULL,
    created_at          TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.product_category
(
    product_id  INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES public.product (id),
    FOREIGN KEY (category_id) REFERENCES public.category (id)
);

CREATE TABLE IF NOT EXISTS public.product_discount
(
    product_id  INTEGER NOT NULL,
    discount_id INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES public.product (id),
    FOREIGN KEY (discount_id) REFERENCES public.discount (id)
);

CREATE TABLE IF NOT EXISTS public.product_attribute
(
    id           SERIAL PRIMARY KEY,
    product_id   BIGINT       NOT NULL,
    attribute_id BIGINT       NOT NULL,
    value        VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES public.product (id),
    FOREIGN KEY (attribute_id) REFERENCES public.attribute (id)
);

CREATE TABLE IF NOT EXISTS public.user_details
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    address      VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.user
(
    id              SERIAL PRIMARY KEY,
    username        VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    role            VARCHAR(255) NOT NULL,
    user_details_id INTEGER UNIQUE REFERENCES public.user_details (id)
);

CREATE TABLE IF NOT EXISTS public."order"
(
    id               SERIAL PRIMARY KEY,
    user_id          INTEGER,
    total_amount     DOUBLE PRECISION NOT NULL,
    status           VARCHAR(255)     NOT NULL,
    payment_status   VARCHAR(255)     NOT NULL,
    payment_method   VARCHAR(255)     NOT NULL,
    shipping_address VARCHAR(255)     NOT NULL,
    shipping_method  VARCHAR(255)     NOT NULL,
    order_date       DATE             NOT NULL,
    delivery_date    DATE,
    FOREIGN KEY (user_id) REFERENCES public.user (id)
);

CREATE TABLE IF NOT EXISTS public.order_item
(
    id         SERIAL PRIMARY KEY,
    order_id   INTEGER        NOT NULL REFERENCES public.order (id),
    product_id INTEGER        NOT NULL REFERENCES public.product (id),
    discount_id   INTEGER,
    price      NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);