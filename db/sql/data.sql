--insert into users (username, password, enabled) VALUES
--                                                    ('admin', 'to_be_encoded', true),
--                                                    ('user', 'to_be_encoded', true);
--
--insert into authorities (username, authority) VALUES
--                                                  ('admin', 'admin'),
--                                                  ('user', 'user');




insert into customers (email, pwd) values
            ('account@gmail.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO'),
            ('cards@gmail.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO'),
            ('loans@gmail.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO'),
            ('balance@gmail.com', '$2a$10$WMpZkvZxV59.gGO0LojrW.x/SllGzOzHlM4eGasTvZWu9nt56zajO');
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

--Oauth
insert into partners(
    client_id,
    client_name,
    client_secret,
    scopes,
    grant_types,
    authentication_methods,
    redirect_uri,
    redirect_uri_logout
)
values ('rosaNegra',
            'rosa negra',
            '$2y$10$1USgXlbASTrzEkHZk2JXPOewzSXLreK9neDjmapoDxTf/YVQyO4wa',
            'read,write',
            'authorization_code,refresh_token',
            'client_secret_basic,client_secret_jwt',
            'https://oauthdebugger.com/debug',
            'https://springone.io/authorized')
