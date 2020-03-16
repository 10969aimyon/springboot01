CREATE TABLE question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(30),
    description TEXT,
    creator INT,
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    tag VARCHAR(256),
    gmt_create BIGINT,
    gmt_modified BIGINT,
)