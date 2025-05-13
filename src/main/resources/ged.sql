drop table grupo;
drop table usuario;
drop table recurso;
drop table grupo_recurso;
drop table grupo_usuario;
drop table gestao;
drop table tipo_documento;
drop table documento;
drop table gestao_documento;


create table grupo
(
    id        bigint generated always as identity not null
        constraint pk_grupo primary key,
    nome      varchar(100)                        not null
        constraint uk_grupo_nome unique,
    grupo_pai bigint null constraint fk_grupo_grupo_pai references grupo
);

create table usuario
(
    id    bigint generated always as identity not null
        constraint pk_usuario primary key
        constraint fk_usuario_pessoa references pessoa,
    senha varchar(255)                        not null
);

create table recurso
(
    id          bigint generated always as identity not null
        constraint pk_recurso primary key,
    descricao   varchar(100)                        not null,
    tipo        char(1)                             not null
        constraint ck_recurso_tipo check ( tipo in ('M', 'A', 'O') ),
    recurso_pai bigint null constraint fk_recurso_recurso_pai references recurso,
    constraint uk_recurso unique (descricao, tipo)
);

create table grupo_recurso
(
    id      bigint generated always as identity not null
        constraint pk_grupo_recurso primary key,
    grupo   bigint                              not null
        constraint fk_grupo_recurso_grupo references grupo,
    recurso bigint                              not null
        constraint fk_grupo_recurso_recurso references recurso,
    tipo    char(1)                             not null
        constraint ck_grupo_recurso_tipo check ( tipo in ('A', 'D') ),
    constraint uk_grupo_recurso unique (grupo, recurso)
);

create table grupo_usuario
(
    id      bigint generated always as identity not null
        constraint pk_grupo_usuario primary key,
    grupo   bigint                              not null
        constraint fk_grupo_usuario_grupo references grupo,
    usuario bigint                              not null
        constraint fk_grupo_usuario_usuario references usuario,
    tipo    char(1)                             not null
        constraint ck_grupo_usuario_tipo check ( tipo in ('A', 'D') ),
    constraint uk_grupo_usuario unique (grupo, usuario)
);

create table gestao
(
    id         bigint generated always as identity not null
        constraint pk_gestao primary key,
    descricao  varchar(255)                        not null,
    gestao_pai bigint null constraint fk_gestao_gestao_pai references gestao,
    constraint uk_gestao unique (descricao, gestao_pai)
);

create table tipo_documento
(
    id        bigint generated always as identity not null
        constraint pk_tipo_documento primary key,
    descricao varchar(255)                        not null,
    mimetype  varchar(255)                        not null
);

create table documento
(
    id             bigint generated always as identity not null
        constraint pk_documento primary key,
    nome           varchar(255)                        not null,
    tipo_documento bigint                              not null
        constraint fk_documento_tipo_documento references tipo_documento,
    conteudo       bytea                               not null
);

create table gestao_documento
(
    id        bigint generated always as identity not null
        constraint pk_gestao_documento primary key,
    gestao    bigint                              not null
        constraint fk_gestao_documento_gestao references gestao,
    documento bigint                              not null
        constraint fk_gestao_documento_documento references documento
);
