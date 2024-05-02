CREATE TABLE IF NOT EXISTS configs(
    app varchar(32) NOT NULL,
    env varchar(32) NOT NULL,
    ns varchar(64) NULL,
    pkey varchar(64) NOT NULL,
    pval varchar(128) NULL
    );

delete from configs;

insert into configs(app,env,ns,pkey,pval) values ('app1', 'dev','public', 'kk.a', 'dev100');
insert into configs(app,env,ns,pkey,pval) values ('app1', 'dev','public', 'kk.b', 'http://localhost:8100');
insert into configs(app,env,ns,pkey,pval) values ('app1', 'dev','public', 'kk.c', '1000');

CREATE TABLE IF NOT EXISTS locks(
    id int primary key NOT NULL,
    app varchar(32) NOT NULL
    );
delete from locks;
insert into locks(id,app) values (1, 'kkconfig-server');


