CREATE DATABASE IF NOT EXISTS jcashapp;

USE jcashapp;

CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       number VARCHAR(20) NOT NULL UNIQUE,
                       pin_hash VARCHAR(500) NOT NULL
);

CREATE TABLE accounts (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          user_id INT NOT NULL UNIQUE,
                          balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              sender_account_id INT NOT NULL,
                              receiver_account_id INT NOT NULL,
                              amount DECIMAL(15, 2) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              FOREIGN KEY (sender_account_id) REFERENCES accounts(id),
                              FOREIGN KEY (receiver_account_id) REFERENCES accounts(id)
);

ALTER TABLE transactions
    ADD COLUMN reference_no VARCHAR(30) UNIQUE AFTER id;

UPDATE transactions
SET reference_no = CONCAT(
        'JC',
        DATE_FORMAT(created_at, '%Y%m%d'),
        LPAD(id, 8, '0')
                   )
WHERE reference_no IS NULL;