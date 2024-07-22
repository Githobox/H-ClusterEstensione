DROP DATABASE IF EXISTS MapDB;
CREATE DATABASE MapDB;

COMMIT;

-- Controllo se l'utente esiste e creazione dell'utente se non esiste
CREATE USER IF NOT EXISTS 'MapUser'@'localhost' IDENTIFIED BY 'map';
GRANT CREATE, SELECT, INSERT, DELETE ON MapDB.* TO 'MapUser'@'localhost';

-- Creazione della tabella e inserimento dei dati
USE MapDB;

CREATE TABLE exampleTab(
    X1 FLOAT,
    X2 FLOAT,
    X3 FLOAT
);

INSERT INTO exampleTab (X1, X2, X3) VALUES (1, 2, 0);
INSERT INTO exampleTab (X1, X2, X3) VALUES (0, 1, -1);
INSERT INTO exampleTab (X1, X2, X3) VALUES (1, 3, 5);
INSERT INTO exampleTab (X1, X2, X3) VALUES (1, 3, 4);
INSERT INTO exampleTab (X1, X2, X3) VALUES (2, 2, 0);

COMMIT;
