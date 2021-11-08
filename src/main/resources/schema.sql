DROP SCHEMA IF EXISTS auction;
CREATE SCHEMA IF NOT EXISTS auction;

CREATE TABLE IF NOT EXISTS roles (
    `idroles` INT NOT NULL AUTO_INCREMENT,
    `role` VARCHAR(45) NULL DEFAULT NULL,
    PRIMARY KEY (`idroles`)
);

CREATE TABLE IF NOT EXISTS users (
    `idusers` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `mail` VARCHAR(100) NOT NULL,
    `login` VARCHAR(45) NOT NULL UNIQUE,
    `password` VARCHAR(1000) NOT NULL,
    `balance` DECIMAL(10,2) NULL DEFAULT '0.00',
    `role` INT NOT NULL,
    `avatar` VARCHAR(70) NULL DEFAULT NULL,
    `isBanned` TINYINT NULL DEFAULT 0,
    PRIMARY KEY (`idusers`),
    CONSTRAINT `role`
    FOREIGN KEY (`role`)
    REFERENCES roles (`idroles`)
);

CREATE TABLE IF NOT EXISTS lots (
    `idlots` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `description` VARCHAR(45) NULL DEFAULT NULL,
    `start_time` TIMESTAMP NOT NULL,
    `end_time` TIMESTAMP NOT NULL,
    `bid` DECIMAL(10,2) NOT NULL,
    `seller` INT NOT NULL,
    PRIMARY KEY (`idlots`),
    CONSTRAINT `seller`
    FOREIGN KEY (`seller`)
    REFERENCES users (`idusers`)
);

CREATE TABLE IF NOT EXISTS status (
    `idstatus` INT NOT NULL AUTO_INCREMENT,
    `status` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idstatus`)
);

CREATE TABLE IF NOT EXISTS bid_history (
    `idbid_history` INT NOT NULL AUTO_INCREMENT,
    `id_buyer` INT NOT NULL,
    `id_lot` INT NOT NULL,
    `bid` DECIMAL(10,2) NOT NULL,
    `status` INT NOT NULL,
    PRIMARY KEY (`idbid_history`),
    CONSTRAINT `id_buyer`
    FOREIGN KEY (`id_buyer`)
    REFERENCES users (`idusers`),
    CONSTRAINT `id_lot`
    FOREIGN KEY (`id_lot`)
    REFERENCES lots (`idlots`),
    CONSTRAINT `status`
    FOREIGN KEY (`status`)
    REFERENCES status (`idstatus`)
);

CREATE TABLE IF NOT EXISTS lot_images (
    `idlot_images` INT NOT NULL AUTO_INCREMENT,
    `lot_id` INT NOT NULL,
    `image` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`idlot_images`),
    CONSTRAINT `lot_id`
    FOREIGN KEY (`lot_id`)
    REFERENCES lots (`idlots`)
);
