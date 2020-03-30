CREATE TABLE notification(
    id INT AUTO_INCREMENT PRIMARY KEY,
    notifier INT NOT NULL ,
    receiver INT NOT NULL ,
    outer_id INT NOT NULL ,
    type int NOT NULL ,
    gmt_create BIGINT NOT NULL ,
    status INT DEFAULT 0 NOT NULL
)