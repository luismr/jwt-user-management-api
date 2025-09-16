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

-- Users (ACTIVE; real hashes for password 'test123'; unique usernames)
-- NOTE: To avoid collisions and match intent, using 'sp1user' and 'toddsuser'.
-- Users for SEARS HOME SERVICES
INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'admin', '$2a$10$VVb80cqWj.VEJ/zV1BXoKO.xYSbryLc8XIytV04C98NTqpCBsi.Vq', '$2a$10$VVb80cqWj.VEJ/zV1BXoKO', 'BCRYPT', 'Administrator', 'admin@searshomeservices.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SEARS HOME SERVICES'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'user', '$2a$10$VVb80cqWj.VEJ/zV1BXoKO.xYSbryLc8XIytV04C98NTqpCBsi.Vq', '$2a$10$VVb80cqWj.VEJ/zV1BXoKO', 'BCRYPT', 'User', 'user@searshomeservices.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SEARS HOME SERVICES'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

-- Users for SP1
INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'sp1admin', 'a7a372980bee87cba4896863c7d65690', 'b741c9d3d3c18125e3ddd5ca0e0125c5', 'MD5', 'SP1 Administrator', 'admin@sp1.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SP1'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'sp1user', 'a7a372980bee87cba4896863c7d65690', 'b741c9d3d3c18125e3ddd5ca0e0125c5', 'MD5', 'SP1 User', 'user@sp1.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'SP1'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

-- Users for TODDS
INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'toddsadmin', '59ec6013960bec186b6a50e22657ae6483bbccfcdf01b5826117b816609b695f', 'c0ba5a991e160a42ac9c81222f8640ae', 'SHA256', 'Todds Administrator', 'admin@todds.com', 'ACTIVE', NOW()
FROM clients c WHERE c.name = 'TODDS'
ON DUPLICATE KEY UPDATE email = VALUES(email), status = VALUES(status);

INSERT INTO users (id_client, username, password_hash, password_salt, password_type, name, email, status, date_last_login)
SELECT c.id, 'toddsuser', '59ec6013960bec186b6a50e22657ae6483bbccfcdf01b5826117b816609b695f', 'c0ba5a991e160a42ac9c81222f8640ae', 'SHA256', 'Todds User', 'user@todds.com', 'ACTIVE', NOW()
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
