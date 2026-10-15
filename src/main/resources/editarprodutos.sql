USE cafeteria;

SELECT DATABASE() AS banco_atual;

SELECT COUNT(*) AS quantidade
FROM cafeteria.produtos;
SELECT id, nome
FROM cafeteria.produtos
LIMIT 5;
DELETE FROM cafeteria.produtos
WHERE id > 0;
SELECT COUNT(*) AS quantidade
FROM cafeteria.produtos;
SELECT COUNT(*) AS quantidade
FROM cafeteria.produtos;