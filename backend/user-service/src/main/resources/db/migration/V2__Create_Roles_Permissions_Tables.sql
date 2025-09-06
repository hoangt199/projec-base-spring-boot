-- Create roles table
CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create permissions table
CREATE TABLE permissions (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create role_permissions join table
CREATE TABLE role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- Modify users table to use role_id instead of role string
ALTER TABLE users 
    DROP COLUMN role,
    ADD COLUMN role_id UUID,
    ADD CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles(id);

-- Create indexes
CREATE INDEX idx_roles_name ON roles(name);
CREATE INDEX idx_permissions_name ON permissions(name);

-- Insert default roles
INSERT INTO roles (id, name, description, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'USER', 'Regular user with basic privileges', NOW(), NOW()),
    (gen_random_uuid(), 'ADMIN', 'Administrator with full access', NOW(), NOW());

-- Insert default permissions
INSERT INTO permissions (id, name, description, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'user:read', 'Can view user information', NOW(), NOW()),
    (gen_random_uuid(), 'user:create', 'Can create new users', NOW(), NOW()),
    (gen_random_uuid(), 'user:update', 'Can update user information', NOW(), NOW()),
    (gen_random_uuid(), 'user:delete', 'Can delete users', NOW(), NOW()),
    (gen_random_uuid(), 'role:read', 'Can view role information', NOW(), NOW()),
    (gen_random_uuid(), 'role:create', 'Can create new roles', NOW(), NOW()),
    (gen_random_uuid(), 'role:update', 'Can update role information', NOW(), NOW()),
    (gen_random_uuid(), 'role:delete', 'Can delete roles', NOW(), NOW()),
    (gen_random_uuid(), 'permission:read', 'Can view permission information', NOW(), NOW()),
    (gen_random_uuid(), 'permission:assign', 'Can assign permissions to roles', NOW(), NOW());

-- Assign permissions to roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'USER' AND p.name = 'user:read';

-- Update existing users to use role_id
UPDATE users
SET role_id = (SELECT id FROM roles WHERE name = 'USER')
WHERE role_id IS NULL;