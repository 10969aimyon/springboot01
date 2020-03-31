create table question (
    id int auto_increment primary key,
    title varchar(30),
    description text,
    creator int,
    view_count int default 0,
    like_count int default 0,
    comment_count int default 0,
    tag varchar(256),
    gmt_create bigint,
    gmt_modified bigint
);