SELECT DISTINCT pr.name, pr.description, ca.title, pr.unit_price
FROM category ca
         JOIN product_category pc
              ON ca.id = pc.category_id
         JOIN product pr
              ON pc.product_id = pr.id;

SELECT pr.name, ca.title, pr.description, at.name, pa.value
FROM category ca
         JOIN product_category pc
              ON ca.id = pc.category_id
         JOIN product pr
              ON pc.product_id = pr.id
         JOIN product_attribute pa
              ON pr.id = pa.product_id
         JOIN attribute at
              ON pa.attribute_id = at.id
WHERE at.name = 'Battery Life'
  AND pa.value = '10 hours';

SELECT us.username, ord.id, STRING_AGG(oi.product_id::TEXT, ',') AS product_ids
FROM public.user us
         JOIN public.order ord
              ON us.id = ord.user_id
         JOIN public.order_item oi
              ON ord.id = oi.order_id
        JOIN public.product pr
              ON oi.product_id = pr.id
GROUP BY
    us.username, ord.id;

SELECT ord.id, us.username, SUM(pr.unit_price)
FROM public.user us
         JOIN public.order ord
              ON us.id = ord.user_id
         JOIN public.order_item oi
              ON ord.id = oi.order_id
         JOIN public.product pr
              ON oi.product_id = pr.id
GROUP BY ord.id, us.username