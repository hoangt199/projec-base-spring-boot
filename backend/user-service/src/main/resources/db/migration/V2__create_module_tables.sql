-- Create modules table
CREATE TABLE modules (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    module_key VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_module_roles table
CREATE TABLE user_module_roles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    module_id UUID NOT NULL,
    role_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    UNIQUE (user_id, module_id, role_id)
);

-- Create indexes
CREATE INDEX idx_modules_module_key ON modules(module_key);
CREATE INDEX idx_modules_is_active ON modules(is_active);
CREATE INDEX idx_user_module_roles_user_id ON user_module_roles(user_id);
CREATE INDEX idx_user_module_roles_module_id ON user_module_roles(module_id);
CREATE INDEX idx_user_module_roles_role_id ON user_module_roles(role_id);

-- Insert default modules
INSERT INTO modules (id, name, module_key, description, is_active)
VALUES 
    (gen_random_uuid(), 'User Management', 'user-management', 'Module for managing users and permissions', TRUE),
    (gen_random_uuid(), 'File Management', 'file-management', 'Module for managing unstructured data files', TRUE);