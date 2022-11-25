BEGIN;

INSERT INTO address (id, street) VALUES (1, 'Пушкинская');
INSERT INTO address (id, street) VALUES (2, 'Проспект Ленина');


INSERT INTO client (id, name, address_id)
VALUES (3, 'Вера Брежн', 1);
INSERT INTO client (id, name, address_id)
VALUES (4, 'Костя Пушкин', 2);

INSERT INTO phone (id, number, client_id) VALUES (5, '89991112233', 3);
INSERT INTO phone (id, number, client_id) VALUES (6, '89992223344', 3);
INSERT INTO phone (id, number, client_id) VALUES (7, '89993334455', 4);

COMMIT;