CREATE TABLE clientes
(
    id          BIGINT NOT NULL,
    nome        VARCHAR(255),
    telefone    BIGINT,
    correntista BOOLEAN,
    saldo_cc    FLOAT,
    CONSTRAINT pk_clientes PRIMARY KEY (id)
);