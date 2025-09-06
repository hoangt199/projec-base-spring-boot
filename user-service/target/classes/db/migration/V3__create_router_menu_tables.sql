-- Create Router table
CREATE TABLE routers (
    id UUID PRIMARY KEY,
    path VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    component VARCHAR(255),
    redirect VARCHAR(255),
    icon VARCHAR(255),
    hidden BOOLEAN DEFAULT FALSE,
    module_id UUID NOT NULL,
    parent_id UUID,
    display_order INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_router_module FOREIGN KEY (module_id) REFERENCES modules(id),
    CONSTRAINT fk_router_parent FOREIGN KEY (parent_id) REFERENCES routers(id),
    CONSTRAINT uk_router_path UNIQUE (path),
    CONSTRAINT uk_router_name UNIQUE (name)
);

-- Create Menu table
CREATE TABLE menus (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    icon VARCHAR(255),
    router_id UUID,
    module_id UUID NOT NULL,
    parent_id UUID,
    display_order INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    visible BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_menu_router FOREIGN KEY (router_id) REFERENCES routers(id),
    CONSTRAINT fk_menu_module FOREIGN KEY (module_id) REFERENCES modules(id),
    CONSTRAINT fk_menu_parent FOREIGN KEY (parent_id) REFERENCES menus(id),
    CONSTRAINT uk_menu_name_module UNIQUE (name, module_id)
);

-- Create UserMenuPermission table
CREATE TABLE user_menu_permissions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    menu_id UUID NOT NULL,
    can_view BOOLEAN DEFAULT FALSE,
    can_create BOOLEAN DEFAULT FALSE,
    can_edit BOOLEAN DEFAULT FALSE,
    can_delete BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_menu_permission_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_menu_permission_menu FOREIGN KEY (menu_id) REFERENCES menus(id),
    CONSTRAINT uk_user_menu UNIQUE (user_id, menu_id)
);

-- Insert sample data for Router
INSERT INTO routers (id, path, name, component, icon, module_id, display_order, active)
VALUES 
    ('11111111-1111-1111-1111-111111111111', '/dashboard', 'Dashboard', 'Dashboard', 'dashboard', '10000000-0000-0000-0000-000000000000', 1, true),
    ('22222222-2222-2222-2222-222222222222', '/users', 'Users', 'Users', 'user', '10000000-0000-0000-0000-000000000000', 2, true),
    ('33333333-3333-3333-3333-333333333333', '/modules', 'Modules', 'Modules', 'module', '10000000-0000-0000-0000-000000000000', 3, true),
    ('44444444-4444-4444-4444-444444444444', '/routers', 'Routers', 'Routers', 'router', '10000000-0000-0000-0000-000000000000', 4, true),
    ('55555555-5555-5555-5555-555555555555', '/menus', 'Menus', 'Menus', 'menu', '10000000-0000-0000-0000-000000000000', 5, true);

-- Insert sample data for Menu
INSERT INTO menus (id, name, description, icon, router_id, module_id, display_order, active, visible)
VALUES 
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Dashboard', 'Dashboard page', 'dashboard', '11111111-1111-1111-1111-111111111111', '10000000-0000-0000-0000-000000000000', 1, true, true),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'User Management', 'User management page', 'user', '22222222-2222-2222-2222-222222222222', '10000000-0000-0000-0000-000000000000', 2, true, true),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Module Management', 'Module management page', 'module', '33333333-3333-3333-3333-333333333333', '10000000-0000-0000-0000-000000000000', 3, true, true),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Router Management', 'Router management page', 'router', '44444444-4444-4444-4444-444444444444', '10000000-0000-0000-0000-000000000000', 4, true, true),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Menu Management', 'Menu management page', 'menu', '55555555-5555-5555-5555-555555555555', '10000000-0000-0000-0000-000000000000', 5, true, true);

-- Insert sample data for UserMenuPermission (for admin user)
INSERT INTO user_menu_permissions (id, user_id, menu_id, can_view, can_create, can_edit, can_delete)
VALUES 
    ('12345678-1234-1234-1234-123456789012', '00000000-0000-0000-0000-000000000000', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', true, true, true, true),
    ('23456789-2345-2345-2345-234567890123', '00000000-0000-0000-0000-000000000000', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', true, true, true, true),
    ('34567890-3456-3456-3456-345678901234', '00000000-0000-0000-0000-000000000000', 'cccccccc-cccc-cccc-cccc-cccccccccccc', true, true, true, true),
    ('45678901-4567-4567-4567-456789012345', '00000000-0000-0000-0000-000000000000', 'dddddddd-dddd-dddd-dddd-dddddddddddd', true, true, true, true),
    ('56789012-5678-5678-5678-567890123456', '00000000-0000-0000-0000-000000000000', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', true, true, true, true);