CREATE TABLE IF NOT EXISTS public.product
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255)   NOT NULL,
    description       TEXT           NOT NULL,
    short_description TEXT           NOT NULL,
    brand             VARCHAR(255)   NOT NULL,
    image             VARCHAR(255)   NOT NULL,
    unitPrice         NUMERIC(10, 2) NOT NULL,
    quantityPerUnit   INTEGER        NOT NULL
);

CREATE TABLE IF NOT EXISTS public.category
(
    id               SERIAL PRIMARY KEY,
    parent_id        BIGINT,
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
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES public.category (id)
);

CREATE TABLE IF NOT EXISTS public.product_category
(
    product_id  BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES public.product (id),
    FOREIGN KEY (category_id) REFERENCES public.category (id)
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