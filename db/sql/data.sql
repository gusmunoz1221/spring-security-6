--insert into users (username, password, enabled) VALUES
--                                                    ('admin', 'to_be_encoded', true),
--                                                    ('user', 'to_be_encoded', true);
--
--insert into authorities (username, authority) VALUES
--                                                  ('admin', 'admin'),
--                                                  ('user', 'user');




insert into customers (email, pwd) values
            ('account@gmail.com', 'to_be_encoded'),
            ('cards@gmail.com', 'to_be_encoded'),
            ('loans@gmail.com', 'to_be_encoded'),
            ('balance@gmail.com', 'to_be_encoded');
--para roles
insert into roles(role_name, description, id_customer) values
            ('ROLE_ADMIN' , 'cant View account endpoint' , 1),
            ('ROLE_ADMIN' , 'cant View cards endpoint' , 2),
            ('ROLE_USER', 'cant View loans endpoint' , 3),
            ('ROLE_USER', 'cant View balance endpoint',4);


--para authorities
--insert into roles(role_name, description, id_customer) values
--    ('VIEW_ACCOUNT' , 'can View account endpoint' , 1),
--    ('VIEW_CARDS'   , 'can View cards endpoint'   , 2),
--    ('VIEW_LOANS'   , 'can View loans endpoint'   , 3),
--    ('VIEW_BALANCE' , 'can View balance endpoint' , 4);


