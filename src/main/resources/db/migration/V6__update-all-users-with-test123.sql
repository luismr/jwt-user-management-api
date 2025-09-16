-- V6__update-all-users-with-test123.sql
-- Update all existing users with new hashes for password 'test123'

-- Update BCrypt users (admin, user)
UPDATE users 
SET password_hash = '$2a$10$VVb80cqWj.VEJ/zV1BXoKO.xYSbryLc8XIytV04C98NTqpCBsi.Vq',
    password_salt = '$2a$10$VVb80cqWj.VEJ/zV1BXoKO'
WHERE password_type = 'BCRYPT';

-- Update MD5 users (sp1admin, sp1user)
UPDATE users 
SET password_hash = 'a7a372980bee87cba4896863c7d65690',
    password_salt = 'b741c9d3d3c18125e3ddd5ca0e0125c5'
WHERE password_type = 'MD5';

-- Update SHA256 users (toddsadmin, toddsuser)
UPDATE users 
SET password_hash = '59ec6013960bec186b6a50e22657ae6483bbccfcdf01b5826117b816609b695f',
    password_salt = 'c0ba5a991e160a42ac9c81222f8640ae'
WHERE password_type = 'SHA256';
