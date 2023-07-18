CREATE TABLE IF NOT EXISTS clients(
    id SERIAL PRIMARY KEY ,
    username VARCHAR(2000) UNIQUE NOT NULL,
    password VARCHAR(2000),
    site VARCHAR(2000) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS shortcuts(
    id SERIAL PRIMARY KEY,
    url VARCHAR(2000) UNIQUE NOT NULL,
    code VARCHAR(2000) UNIQUE NOT NULL,
    total INTEGER,
    client_id INTEGER REFERENCES clients(id)
);

INSERT INTO clients (username, password, site) VALUES ('user1', 'pass', 'google.com');
INSERT INTO clients (username, password, site) VALUES ('user2', 'pass', 'ms.com');


INSERT INTO shortcuts (url, code, total, client_id) VALUES ('http://google.com/stat', 'dd', 0, 1);
INSERT INTO shortcuts (url, code, total, client_id) VALUES ('https://google.com/val', 'gg', 99, 1);
INSERT INTO shortcuts (url, code, total, client_id) VALUES ('http://google.com/col', 'aa', 129, 1);

INSERT INTO shortcuts (url, code, total, client_id) VALUES ('https://ms.com/stat', 'zz', 1, 2);
INSERT INTO shortcuts (url, code, total, client_id) VALUES ('https://ms.com/go', 'xx', 15, 2);
INSERT INTO shortcuts (url, code, total, client_id) VALUES ('http://ms.com/auth', 'qq', 35, 2);

select * from clients inner join shortcuts s on clients.id = s.client_id