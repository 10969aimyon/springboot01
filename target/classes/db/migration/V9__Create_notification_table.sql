create table notification(
    id int auto_increment primary key,
    notifier int not null ,
    receiver int not null ,
    outer_id int not null ,
    type int not null ,
    gmt_create bigint not null ,
    status int default 0 not null
);