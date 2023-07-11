CREATE TABLE IF NOT EXISTS role (
        id   integer primary key,
        name varchar(255)
);

INSERT INTO role VALUES ( 1, 'ROLE_USER' );
INSERT INTO role VALUES ( 2, 'ROLE_ADMIN' );