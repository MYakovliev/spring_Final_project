INSERT INTO roles(role) VALUES
                            ('ADMIN'),
                            ('BUYER'),
                            ('SELLER');

INSERT INTO status VALUES (0, 'LOSE'),
                          (1, 'WINING'),
                          (2, 'WON');

INSERT INTO users(name, mail, `login`, password, role) VALUES
            ( 'name1', 'mail1@mail.ma', 'login1', '$2a$12$swOvQiIkAIu8.OapWlPJkeRkwhEl9gZM60b8M9DKOAB7fI3Sm4Kxa', 2),
            ( 'name2', 'mail2@mail.ma', 'login2', '$2a$12$swOvQiIkAIu8.OapWlPJkeRkwhEl9gZM60b8M9DKOAB7fI3Sm4Kxa', 3);

INSERT INTO lots(name, description, start_time, end_time, bid, seller)
VALUES ('name1','desc1', NOW(), '2022-01-01', 20.21, 2),
       ('name2','desc2', NOW(), '2022-01-01', 20.45, 2);

INSERT INTO bid_history(id_buyer, id_lot, bid, status) VALUES
                                                (1, 1, 20.45, 0),
                                                (1, 1, 21.50, 1);