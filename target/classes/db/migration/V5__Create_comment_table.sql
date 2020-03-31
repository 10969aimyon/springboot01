create table comment
(
    id bigint auto_increment primary key,
    parent_id int not null ,
    type int not null ,
    commentator int not null ,
    gmt_create bigint not null ,
    gmt_modified bigint not null ,
    like_count bigint default 0
);