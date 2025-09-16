-- dml.sql (MySQL 8)
-- Inserts/seed data per spec (client roles defaults, special-case SHS, and user-role mappings)

-- Roles
INSERT INTO roles (description, internal) VALUES
  ('ROLE_ADMIN', TRUE),
  ('ROLE_USER', TRUE),
  ('ROLE_CLIENT_ADMIN', FALSE),
  ('ROLE_CLIENT_USER', FALSE)
ON DUPLICATE KEY UPDATE description = VALUES(description), internal = VALUES(internal);

-- Clients / Organizations
INSERT INTO clients (external_id, name) VALUES
  ('SEARS-HOME-SERVICES', 'SEARS HOME SERVICES'),
  ('SP1', 'SP1'),
  ('TODDS', 'TODDS')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Client role permissions
-- Default for all orgs: ROLE_CLIENT_ADMIN and ROLE_CLIENT_USER
INSERT INTO client_roles (id_client, id_role)
SELECT c.id, r.id
FROM clients c
JOIN roles r ON r.description IN ('ROLE_CLIENT_ADMIN','ROLE_CLIENT_USER')
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;

-- Sears Home Services additionally allowed ROLE_ADMIN and ROLE_USER
INSERT INTO client_roles (id_client, id_role)
SELECT c.id, r.id
FROM clients c
JOIN roles r ON r.description IN ('ROLE_ADMIN','ROLE_USER')
WHERE c.external_id = 'SEARS-HOME-SERVICES'
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;

-- Users (ACTIVE; placeholder bcrypt hashes; unique usernames)
-- NOTE: To avoid collisions and match intent, using 'sp1user' and 'toddsuser'.
-- Users for SEARS HOME SERVICES
INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'admin', '$2y$10$wJalrXUtnFEMI/K7MDENGuEv6Q9r1tGzQ6Q0eQFRCGDpa2BkLomqG', 'adminSALT', 'BCRYPT', 'Administrator', 'admin@searshomeservices.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SEARS HOME SERVICES'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'user', '$2y$10$1a2b3c4d5e6f7g8h9i0jklmnopqrstuvwxYzABCD1234567890abcd', 'userSALT', 'BCRYPT', 'User', 'user@searshomeservices.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SEARS HOME SERVICES'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

-- Users for SP1
INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'sp1admin', '4642e6e7e3a0655e768f8b1a79cd13e3', 'sp1adminSALT', 'MD5', 'SP1 Administrator', 'admin@sp1.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SP1'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'sp1user', '8e8b2a1d7b6c3e5a4c1e2f7d8b4a3c2d', 'sp1userSALT', 'MD5', 'SP1 User', 'user@sp1.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SP1'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

-- Users for TODDS
INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'toddsadmin', 'b0e1b5e5b6a1c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8', 'toddsadminSALT', 'SHA256', 'Todds Administrator', 'admin@todds.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'TODDS'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'toddsuser', 'a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2', 'toddsuserSALT', 'SHA256', 'Todds User', 'user@todds.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'TODDS'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);


-- Assign roles to users (now includes client context)
-- SHS - admin user gets ROLE_ADMIN
INSERT INTO user_roles (id_user, id_client, id_role)
SELECT u.id, c.id, r.id 
FROM users u 
JOIN clients c ON c.name = 'SEARS HOME SERVICES'
JOIN roles r ON r.description = 'ROLE_ADMIN'
WHERE u.username = 'admin'
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;

-- SHS - user gets ROLE_USER
INSERT INTO user_roles (id_user, id_client, id_role)
SELECT u.id, c.id, r.id 
FROM users u 
JOIN clients c ON c.name = 'SEARS HOME SERVICES'
JOIN roles r ON r.description = 'ROLE_USER'
WHERE u.username = 'user'
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;

-- SP1 - sp1admin gets ROLE_CLIENT_ADMIN
INSERT INTO user_roles (id_user, id_client, id_role)
SELECT u.id, c.id, r.id 
FROM users u 
JOIN clients c ON c.name = 'SP1'
JOIN roles r ON r.description = 'ROLE_CLIENT_ADMIN'
WHERE u.username = 'sp1admin'
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;

-- SP1 - sp1user gets ROLE_CLIENT_USER
INSERT INTO user_roles (id_user, id_client, id_role)
SELECT u.id, c.id, r.id 
FROM users u 
JOIN clients c ON c.name = 'SP1'
JOIN roles r ON r.description = 'ROLE_CLIENT_USER'
WHERE u.username = 'sp1user'
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;

-- TODDS - toddsadmin gets ROLE_CLIENT_ADMIN
INSERT INTO user_roles (id_user, id_client, id_role)
SELECT u.id, c.id, r.id 
FROM users u 
JOIN clients c ON c.name = 'TODDS'
JOIN roles r ON r.description = 'ROLE_CLIENT_ADMIN'
WHERE u.username = 'toddsadmin'
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;

-- TODDS - toddsuser gets ROLE_CLIENT_USER
INSERT INTO user_roles (id_user, id_client, id_role)
SELECT u.id, c.id, r.id 
FROM users u 
JOIN clients c ON c.name = 'TODDS'
JOIN roles r ON r.description = 'ROLE_CLIENT_USER'
WHERE u.username = 'toddsuser'
ON DUPLICATE KEY UPDATE date_updated = CURRENT_TIMESTAMP;
