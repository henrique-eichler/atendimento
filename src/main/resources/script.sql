drop table sessao;
drop table agenda;
drop table terapia_convenio;
drop table convenio_paciente;
drop table convenio;
drop table responsavel_paciente;
drop table responsavel;
drop table paciente;
drop table recurso_sala;
drop table recurso;
drop table terapia_sala;
drop table cronograma;
drop table sala;
drop table profissional_terapia;
drop table terapia;
drop table profissional;
drop table pessoa;

drop sequence sessao_seq;
drop sequence agenda_seq;
drop sequence terapia_convenio_seq;
drop sequence convenio_paciente_seq;
drop sequence convenio_seq;
drop sequence responsavel_paciente_seq;
drop sequence recurso_sala_seq;
drop sequence recurso_seq;
drop sequence terapia_sala_seq;
drop sequence cronograma_seq;
drop sequence sala_seq;
drop sequence profissional_terapia_seq;
drop sequence terapia_seq;
drop sequence pessoa_seq;

create sequence sessao_seq;
create sequence agenda_seq;
create sequence terapia_convenio_seq;
create sequence convenio_paciente_seq;
create sequence convenio_seq;
create sequence responsavel_paciente_seq;
create sequence recurso_sala_seq;
create sequence recurso_seq;
create sequence terapia_sala_seq;
create sequence cronograma_seq;
create sequence sala_seq;
create sequence profissional_terapia_seq;
create sequence terapia_seq;
create sequence pessoa_seq;

create table pessoa
(
    id              bigint default nextval('pessoa_seq')
        constraint pk_pessoa primary key,
    nome            varchar(255) not null,
    email           varchar(255) null
        constraint uk_usuario_email unique,
    data_nascimento date null,
    sexo            char(1) null
        constraint ch_pessoa_sexo check ( sexo in ('F', 'M') )
);

create table profissional
(
    id bigint
        constraint pk_profissional primary key
        constraint fk_profissional_pessoa references pessoa
);

create table terapia
(
    id   bigint default nextval('terapia_seq')
        constraint pk_terapia primary key,
    nome varchar(255) not null
        constraint uk_terapia_nome unique
);

create table profissional_terapia
(
    id            bigint default nextval('profissional_terapia_seq')
        constraint pk_profissional_terapia primary key,
    profissional  bigint not null
        constraint fk_profissional_terapia_profissional references profissional,
    terapia       bigint not null
        constraint fk_profissional_terapia_terapia references terapia,
    data_validade date   not null,
    constraint uk_profissional_terapia unique (profissional, terapia, data_validade)
);

create table sala
(
    id     bigint default nextval('sala_seq')
        constraint pk_sala primary key,
    numero bigint not null
        constraint uk_sala_numero unique
);

create table cronograma
(
    id           bigint default nextval('cronograma_seq')
        constraint pk_cronograma primary key,
    sala         bigint    not null
        constraint fk_cronograma_sala references sala,
    dia_semana   char(1)   not null
        constraint ck_cronograma_dia_seamana check ( dia_semana in ('D', '2', '3', '4', '5', '6', 'S') ),
    hora_inicio  timestamp not null,
    hora_termino timestamp not null
        constraint ck_cronograma_hora_termino check ( hora_inicio < hora_termino ),
    constraint uk_cronograma unique (sala, dia_semana, hora_inicio)
);

create table terapia_sala
(
    id      bigint default nextval('terapia_sala_seq')
        constraint pk_terapia_sala primary key,
    terapia bigint not null
        constraint fk_terapia_sala_terapia references terapia,
    sala    bigint not null
        constraint fk_terapia_sala_sala references sala,
    constraint uk_terapia_sala unique (terapia, sala)
);

create table recurso
(
    id                 bigint default nextval('recurso_seq')
        constraint pk_recurso primary key,
    nome               varchar(255) not null
        constraint uk_recurso_descricao unique,
    descricao          varchar(255) null,
    numero_propriedade int          not null
        constraint uk_resuro_numero_propriedade unique
);

create table recurso_sala
(
    id      bigint default nextval('recurso_sala_seq')
        constraint pk_recurso_sala primary key,
    recurso bigint not null
        constraint fk_recurso_sala_recurso references recurso,
    sala    bigint not null
        constraint fk_recurso_sala_sala references sala
);

create table paciente
(
    id bigint
        constraint pk_paciente primary key
        constraint fk_paciente_pessoa references pessoa
);

create table responsavel
(
    id bigint
        constraint pk_responsavel primary key
        constraint fk_responsavel_pessoa references pessoa
);

create table responsavel_paciente
(
    id              bigint generated always as identity
        constraint pk_reponsavel_paciente primary key,
    responsavel     bigint  not null
        constraint fk_reponsavel_paciente_responsavel references responsavel,
    paciente        bigint  not null
        constraint fk_responsavel_paciente_paciente references paciente,
    grau_parentesco char(1) not null
        constraint ck_responsavel_paciente_grau_parentesco check ( grau_parentesco in ('P', 'M', 'O')),
    constraint uk_responsavel_paciente unique (responsavel, paciente)
);

create table convenio
(
    id   bigint generated always as identity
        constraint pk_convenio primary key,
    nome varchar(255) not null
        constraint uk_covenio_nome unique
);

create table convenio_paciente
(
    id       bigint generated always as identity
        constraint pk_convenio_paciente primary key,
    convenio bigint       not null
        constraint fk_covnenio_paciente_convenio references convenio,
    paciente bigint       not null
        constraint fk_convenio_paciente_paciente references paciente,
    numero   varchar(100) not null,
    constraint uk_convenio_paciente_paciente unique (convenio, paciente),
    constraint uk_convenio_paciente_numero unique (convenio, numero)
);

create table terapia_convenio
(
    id                 bigint generated always as identity
        constraint pk_terapia_convenio primary key,
    terapia            bigint not null
        constraint fk_terapia_convenio_terapia references terapia,
    convenio           bigint not null
        constraint fk_terapia_convenio_convenio references convenio,
    valor_terapia      float4 not null,
    valor_profissional float4 not null,
    constraint uk_terapia_convenio unique (terapia, convenio)
);

create table agenda
(
    id           bigint generated always as identity
        constraint pk_agenda primary key,
    data_agenda  date   not null,
    cronograma   bigint not null
        constraint fk_agenda_cronograma references cronograma,
    paciente     bigint not null
        constraint fk_agenda_paciente references paciente,
    terapia      bigint not null
        constraint fk_agenda_terapia references terapia,
    convenio     bigint not null
        constraint fk_agenda_convenio references convenio,
    profissional bigint not null
        constraint fk_agenda_profissional references profissional
);

create table sessao
(
    id           bigint generated always as identity
        constraint pk_sessao primary key
        constraint fk_sessao_agenda references agenda,
    data_inicio  timestamp not null,
    data_termino timestamp null,
    sala         bigint    not null
        constraint fk_sessao_sala references sala,
    paciente     bigint    not null
        constraint fk_sessao_paciente references paciente
);
