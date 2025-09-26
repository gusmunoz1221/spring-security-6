--insert into users (username, password, enabled) VALUES
--                                                    ('admin', 'to_be_encoded', true),
--                                                    ('user', 'to_be_encoded', true);
--
--insert into authorities (username, authority) VALUES
--                                                  ('admin', 'admin'),
--                                                  ('user', 'user');




insert into customers (email, pwd, rol) VALUES
  ('super_user@gmail.com', 'to_be_encoded', 'admin'),
  ('basic_user@gmail.com', 'to_be_encoded', 'user');