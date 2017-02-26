#create table USER

drop table if EXISTS user;
CREATE TABLE user(
  id int primary key AUTO_INCREMENT,
name VARCHAR(50) not null,
password VARCHAR(50) not null,
  salt VARCHAR(50) not null,
  head_url VARCHAR(100) not null

)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS question;
CREATE TABLE question (
  id int PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(3000) not null,
  content TEXT not null,
  createdDate DATETIME not null,
  userId int not null,
  commentCount int DEFAULT 0
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS message;
CREATE TABLE message (
  id int NOT NULL AUTO_INCREMENT,
  from_id int NULL,
  to_id int NULL ,
  content text NULL ,
  created_date DATETIME NULL ,
  has_read int NULL ,
  conversation_id VARCHAR(45) NOT NULL ,
  PRIMARY KEY (id),
  INDEX conversation_index (conversation_id ASC ),
  INDEX created_date (created_date ASC )
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

DROP TABLE IF EXISTS comment;
CREATE TABLE comment (
  id int NOT NULL AUTO_INCREMENT,
  content TEXT NOT NULL ,
  user_id INT NOT NULL ,
  entity_id INT NOT NULL ,
  entity_type INT NOT NULL ,
  created_date DATETIME NOT NULL ,
  status int NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  INDEX entity_index (entity_id ASC , entity_type ASC )
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

#ticket database
DROP TABLE IF EXISTS login_ticket;
CREATE TABLE login_ticket (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL ,
  ticket VARCHAR(45) NOT NULL ,
  expired DATETIME NOT NULL ,
  status INT NOT NULL ,
  PRIMARY KEY (id),
  UNIQUE INDEX ticket_UNIQUE (ticket ASC )
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


#feed
DROP TABLE IF EXISTS feed;
CREATE TABLE feed (
  id INT NOT NULL AUTO_INCREMENT,
  type INT NOT NULL ,
  user_id INT NOT NULL ,
  created_date DATETIME NOT NULL ,
  data VARCHAR(255) NOT NULL ,
  PRIMARY KEY (id),
  INDEX user_index (user_id ASC )
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;