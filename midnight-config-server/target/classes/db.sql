CREATE TABLE IF NOT EXISTS configs(
    app varchar(32) NOT NULL,
    env varchar(32) NOT NULL,
    ns varchar(64) NULL,
    pkey varchar(64) NOT NULL,
    pval varchar(128) NULL
    );

delete from configs;

insert into configs(app,env,ns,pkey,pval) values ('app1', 'dev','public', 'midnight.a', 'dev100');
insert into configs(app,env,ns,pkey,pval) values ('app1', 'dev','public', 'midnight.b', 'http://localhost:8100');
insert into configs(app,env,ns,pkey,pval) values ('app1', 'dev','public', 'midnight.c', '1000');
