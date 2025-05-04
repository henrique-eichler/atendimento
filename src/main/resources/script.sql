drop table pessoa;
drop table profissional;
drop table terapia;
drop table profissional_terapia;
drop table sala;
drop table cronograma;
drop table terapia_sala;
drop table paciente;
drop table responsavel;
drop table responsavel_paciente;
drop table convenio;
drop table convenio_paciente;
drop table terapia_convenio;
drop table agenda;
drop table sessao;
drop table grupo;
drop table usuario;
drop table recurso;
drop table grupo_recurso;
drop table grupo_usuario;
drop table gestao;
drop table tipo_documento;
drop table documento;
drop table gestao_documento;

create table pessoa (
    id bigint not null constraint pk_pessoa primary key,
    nome varchar(255) not null,
    email varchar(255) null constraint uk_usuario_email unique,
    data_nascimento date null,
    sexo char(1) null constraint ch_pessoa_sexo check ( sexo in ('F', 'M') )
);

create table profissional (
    id bigint not null constraint pk_profissional primary key constraint fk_profissional_pessoa references pessoa
);

create table terapia (
    id bigint constraint pk_terapia primary key,
    nome varchar(255) not null constraint uk_terapia_nome unique
);

create table profissional_terapia (
    id bigint not null constraint pk_profissional_terapia primary key,
    profissional bigint not null constraint fk_profissional_terapia_profissional references profissional,
    terapia bigint not null constraint fk_profissional_terapia_terapia references terapia,
    data_validade date not null,
    constraint uk_profissional_terapia unique (profissional, terapia, data_validade)
);

create table sala (
    id bigint not null constraint pk_sala primary key,
    numero bigint not null constraint uk_sala_numvero unique
);

create table cronograma (
    id bigint not null constraint pk_cronograma primary key,
    sala bigint not null constraint fk_cronograma_sala references sala,
    dia_semana char(1) not null constraint ck_cronograma_dia_seamana check ( dia_semana in ('D', '2', '3', '4', '5', '6', 'S') ),
    hora_inicio timestamp not null,
    hora_termino timestamp not null constraint ck_cronograma_hora_termino check ( hora_inicio < hora_termino ),
    constraint uk_cronograma unique (sala, dia_semana, hora_inicio)
);

create table terapia_sala (
    id bigint not null constraint pk_terapia_sala primary key,
    terapia bigint not null constraint fk_terapia_sala_terapia references terapia,
    sala bigint not null constraint fk_terapia_sala_sala references sala,
    constraint uk_terapia_sala unique (terapia, sala)
);

create table paciente (
    id bigint not null constraint pk_paciente primary key constraint fk_paciente_pessoa references pessoa
);

create table responsavel (
    id bigint not null constraint pk_responsavel primary key constraint fk_responsavel_pessoa references pessoa
);

create table responsavel_paciente (
    id bigint not null constraint pk_reponsavel_paciente primary key,
    responsavel bigint not null constraint fk_reponsavel_paciente_responsavel references responsavel,
    paciente bigint not null constraint fk_responsavel_paciente_paciente references paciente,
    grau_parentesco char(1) not null constraint ck_responsavel_paciente_grau_parentesco check (  grau_parentesco in ('P', 'M', 'O')),
    constraint uk_responsavel_paciente unique (responsavel, paciente)
);

create table convenio (
    id bigint not null constraint pk_convenio primary key,
    nome varchar(255) not null constraint uk_covenio_nome unique
);

create table convenio_paciente (
    id bigint not null constraint pk_convenio_paciente primary key,
    convenio bigint not null constraint fk_covnenio_paciente_convenio references convenio,
    paciente bigint not null constraint fk_convenio_paciente_paciente references paciente,
    numero varchar(100) not null,
    constraint uk_convenio_paciente_paciente unique (convenio, paciente),
    constraint uk_convenio_paciente_numero unique (convenio, numero)
);

create table terapia_convenio (
    id bigint not null constraint pk_terapia_convenio primary key,
    terapia bigint not null constraint fk_terapia_convenio_terapia references terapia,
    convenio bigint not null constraint fk_terapia_convenio_convenio references convenio,
    valor_terapia float4 not null,
    valor_profissional float4 not null,
    constraint uk_terapia_convenio unique (terapia, convenio)
);

create table agenda (
    id bigint not null constraint pk_agenda primary key,
    data_agenda date not null,
    cronograma bigint not null constraint fk_agenda_cronograma references cronograma,
    paciente bigint not null constraint fk_agenda_paciente references paciente,
    terapia bigint not null constraint fk_agenda_terapia references terapia,
    convenio bigint not null constraint fk_agenda_convenio references convenio,
    profissional bigint not null constraint fk_agenda_profissional references profissional
);

create table sessao (
    id bigint not null constraint pk_sessao primary key constraint fk_sessao_agenda references agenda,
    data_inicio timestamp not null,
    data_termino timestamp null,
    sala bigint not null constraint fk_sessao_sala references sala,
    paciente bigint not null constraint fk_sessao_paciente references paciente
);

create table grupo (
    id bigint not null constraint pk_grupo primary key,
    nome varchar(100) not null constraint uk_grupo_nome unique,
    grupo_pai bigint null constraint fk_grupo_grupo_pai references grupo
);

create table usuario (
    id bigint not null constraint pk_usuario primary key constraint fk_usuario_pessoa references pessoa,
    senha varchar(255) not null
);

create table recurso (
    id bigint not null constraint pk_recurso primary key,
    descricao varchar(100) not null,
    tipo char(1) not null constraint ck_recurso_tipo check ( tipo in ('M', 'A', 'O') ),
    recurso_pai bigint null constraint fk_recurso_recurso_pai references recurso,
    constraint uk_recurso unique (descricao, tipo)
);

create table grupo_recurso (
    id bigint not null constraint pk_grupo_recurso primary key,
    grupo bigint not null constraint fk_grupo_recurso_grupo references grupo,
    recurso bigint not null constraint fk_grupo_recurso_recurso references recurso,
    tipo char(1) not null constraint ck_grupo_recurso_tipo check ( tipo in ('A', 'D') ),
    constraint uk_grupo_recurso unique (grupo, recurso)
);

create table grupo_usuario (
    id bigint not null constraint pk_grupo_usuario primary key,
    grupo bigint not null constraint fk_grupo_usuario_grupo references grupo,
    usuario bigint not null constraint fk_grupo_usuario_usuario references usuario,
    tipo char(1) not null constraint ck_grupo_usuario_tipo check ( tipo in ('A', 'D') ),
    constraint uk_grupo_usuario unique (grupo, usuario)
);

create table gestao (
    id bigint not null constraint pk_gestao primary key,
    descricao varchar(255) not null,
    gestao_pai bigint null constraint fk_gestao_gestao_pai references gestao,
    constraint uk_gestao unique (descricao, gestao_pai)
);

create table tipo_documento (
    id bigint not null constraint pk_tipo_documento primary key,
    descricao varchar(255) not null,
    mimetype varchar(255) not null
);

create table documento (
    id bigint not null constraint pk_documento primary key,
    nome varchar(255) not null,
    tipo_documento bigint not null constraint fk_documento_tipo_documento references tipo_documento,
    conteudo bytea not null
);

create table gestao_documento (
    id bigint not null constraint pk_gestao_documento primary key,
    gestao bigint not null constraint fk_gestao_documento_gestao references gestao,
    documento bigint not null constraint fk_gestao_documento_documento references documento
);
