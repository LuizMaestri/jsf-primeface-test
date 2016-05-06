CREATE TABLE users
(
  cod_usuario SERIAL PRIMARY KEY NOT NULL,
  nme_usuario VARCHAR(30) NOT NULL,
  nme_senha VARCHAR(32) NOT NULL,
  nme_login VARCHAR(20) NOT NULL UNIQUE,
  fl_admin BOOLEAN DEFAULT false
);

INSERT INTO users (nme_usuario, nme_senha, nme_login, fl_admin) VALUES ('admin', md5('admin'), 'admin', TRUE);

CREATE TABLE call
(
  cod_chamado SERIAL PRIMARY KEY NOT NULL,
  cod_usuario_pedido INTEGER REFERENCES users (cod_usuario),
  cod_usuario_atendido INTEGER REFERENCES users (cod_usuario),
  dsc_versao_sistema VARCHAR(50),
  nme_chamado VARCHAR(250),
  dsc_chamado TEXT,
  dsc_solucao TEXT,
  tpo_chamado INTEGER,
  tpo_prioridade INTEGER,
  sta_chamado INTEGER,
  dta_abertura TIMESTAMP(6) DEFAULT statement_timestamp(),
  dta_fechamento TIMESTAMP(6)
);