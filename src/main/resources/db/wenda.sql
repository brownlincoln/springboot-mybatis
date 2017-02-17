#create table USER

/*
https://dev.mysql.com/doc/refman/5.7/en/create-table.html

CREATE TABLE IF NOT EXISTS `schema`.`Employee` (
`idEmployee` VARCHAR(45) NOT NULL ,
`Name` VARCHAR(255) NULL ,
`idAddresses` VARCHAR(45) NULL ,
PRIMARY KEY (`idEmployee`) ,
CONSTRAINT `fkEmployee_Addresses`
FOREIGN KEY `fkEmployee_Addresses` (`idAddresses`)
REFERENCES `schema`.`Addresses` (`idAddresses`)
ON DELETE NO ACTION
ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
 */
CREATE TABLE user(
  id int primary key AUTO_INCREMENT,
name VARCHAR(50) not null,
password VARCHAR(50) not null,
  salt VARCHAR(50) not null,
  head_url VARCHAR(100) not null

)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;