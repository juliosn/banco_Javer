CREATE TABLE clientes
(
    id          BIGINT NOT NULL,
    nome        VARCHAR(255),
    telefone    BIGINT,
    correntista BOOLEAN,
    saldo_cc    FLOAT,
    CONSTRAINT pk_clientes PRIMARY KEY (id)
);

INSERT INTO CLIENTES (ID, NOME, TELEFONE, CORRENTISTA, SALDO_CC) VALUES ( 10, 'GLAUBER', 13245456142142, true, 10000)