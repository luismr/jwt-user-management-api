-- ddl.sql (MySQL 8)
-- Schema for clients, users, roles, client_roles, user_roles, logs_logins, and junctions
-- Charset: utf8mb4_0900_ai_ci, Engine: InnoDB

-- Optional: create & use schema
CREATE DATABASE IF NOT EXISTS b2bapp
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;
USE b2bapp;

SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `clients` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `external_id`   VARCHAR(64)     NULL,
  `name`          VARCHAR(128)    NOT NULL,
  `date_created`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_clients_external_id` (`external_id`),
  UNIQUE KEY `uq_clients_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `users` (
  `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_client`       BIGINT UNSIGNED NULL,
  `username`        VARCHAR(128)    NOT NULL,
  `password_hash`   VARCHAR(255)    NOT NULL,
  `password_salt`   VARCHAR(255)    NOT NULL,
  `password_type`   ENUM('BCRYPT','SHA256', 'MD5', 'SHA512') NOT NULL DEFAULT 'MD5',
  `name`            VARCHAR(128)    NOT NULL,
  `email`           VARCHAR(128)    NOT NULL,
  `status`          ENUM('ACTIVE','INACTIVE','SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
  `date_last_login` TIMESTAMP       NULL DEFAULT NULL,
  `date_created`    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated`    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_users_username` (`username`),
  UNIQUE KEY `uq_users_email`    (`email`),
  KEY `ix_users_client` (`id_client`),
  CONSTRAINT `fk_users_client`
    FOREIGN KEY (`id_client`) REFERENCES `clients` (`id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `roles` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `description`   VARCHAR(191)    NOT NULL,
  `internal`      BOOLEAN         NOT NULL DEFAULT FALSE,
  `date_created`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_roles_description` (`description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `client_roles` (
  `id_client`     BIGINT UNSIGNED NOT NULL,
  `id_role`       BIGINT UNSIGNED NOT NULL,
  `date_created`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_client`, `id_role`),
  KEY `ix_client_roles_role` (`id_role`),
  CONSTRAINT `fk_client_roles_client`
    FOREIGN KEY (`id_client`) REFERENCES `clients` (`id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_client_roles_role`
    FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `user_roles` (
  `id_user`       BIGINT UNSIGNED NOT NULL,
  `id_client`     BIGINT UNSIGNED NOT NULL,
  `id_role`       BIGINT UNSIGNED NOT NULL,
  `date_created`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_user`, `id_client`, `id_role`),
  KEY `ix_user_roles_client` (`id_client`),
  KEY `ix_user_roles_role` (`id_role`),
  CONSTRAINT `fk_user_roles_user`
    FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `fk_user_roles_client_role`
    FOREIGN KEY (`id_client`, `id_role`) REFERENCES `client_roles` (`id_client`, `id_role`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `logs_logins` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id`       BIGINT UNSIGNED NOT NULL,
  `type`          VARCHAR(64)     NOT NULL,
  `date_created`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated`  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ix_logs_logins_user_id` (`user_id`),
  KEY `ix_logs_logins_date_created` (`date_created`),
  CONSTRAINT `fk_logs_logins_user`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
