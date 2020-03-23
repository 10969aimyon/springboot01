CREATE TABLE comment
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id INT NOT NULL ,
    TYPE INT NOT NULL ,
    commentator INT NOT NULL ,
    gmt_create BIGINT NOT NULL ,
    gmt_modified BIGINT NOT NULL ,
    like_count BIGINT DEFAULT 0
)