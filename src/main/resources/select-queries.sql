SELECT *
FROM category cat
         JOIN attribute at
              ON cat.id = at.category_id;

SELECT pr.name, ca.title, pr.description, at.name, pa.value
FROM category ca
         JOIN attribute at
              ON ca.id = at.category_id
         JOIN product_attribute pa
              ON at.id = pa.attribute_id
         JOIN product pr
              ON pa.product_id = pr.id
WHERE at.name = 'Battery Life' AND pa.value = '10 hours';

SELECT pr.name, at.name, pa.value
FROM category ca
         JOIN attribute at
              ON ca.id = at.category_id
         JOIN product_attribute pa
              ON at.id = pa.attribute_id
         JOIN product pr
              ON pa.product_id = pr.id
WHERE pr.name = 'Smartwatch JKL';