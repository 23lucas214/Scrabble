CREATE SEQUENCE partie_id_partie_seq;

CREATE TABLE partie (
                id_partie INTEGER NOT NULL DEFAULT nextval('partie_id_partie_seq'),
                CONSTRAINT partie_pk PRIMARY KEY (id_partie)
);


ALTER SEQUENCE partie_id_partie_seq OWNED BY partie.id_partie;

CREATE TABLE compte (
                pseudo VARCHAR NOT NULL,
                mdpHash VARCHAR NOT NULL,
                points INTEGER NOT NULL,
                pointsPartie INTEGER NOT NULL,
                loginHash VARCHAR NOT NULL,
                id_partie INTEGER NOT NULL,
                CONSTRAINT compte_pk PRIMARY KEY (pseudo)
);


CREATE SEQUENCE piece_id_piece_seq;

CREATE TABLE piece (
                id_piece INTEGER NOT NULL DEFAULT nextval('piece_id_piece_seq'),
                lettre CHAR NOT NULL,
                X INTEGER NOT NULL,
                Y INTEGER NOT NULL,
                id_partie INTEGER NOT NULL,
                pseudo VARCHAR NOT NULL,
                CONSTRAINT piece_pk PRIMARY KEY (id_piece)
);


ALTER SEQUENCE piece_id_piece_seq OWNED BY piece.id_piece;

CREATE TABLE pioche (
                lettre CHAR NOT NULL,
                nbLettre INTEGER NOT NULL,
                id_partie INTEGER NOT NULL,
                CONSTRAINT pioche_pk PRIMARY KEY (lettre)
);


ALTER TABLE pioche ADD CONSTRAINT partie_pioche_fk
FOREIGN KEY (id_partie)
REFERENCES partie (id_partie)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE compte ADD CONSTRAINT partie_compte_fk
FOREIGN KEY (id_partie)
REFERENCES partie (id_partie)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE piece ADD CONSTRAINT partie_piece_fk
FOREIGN KEY (id_partie)
REFERENCES partie (id_partie)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE piece ADD CONSTRAINT compte_piece_fk
FOREIGN KEY (pseudo)
REFERENCES compte (pseudo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
