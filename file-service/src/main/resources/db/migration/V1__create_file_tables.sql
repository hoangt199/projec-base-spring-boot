-- Create file_info table
CREATE TABLE file_info (
    id UUID PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    content_type VARCHAR(100),
    media_type VARCHAR(50),
    extension VARCHAR(20),
    is_folder BOOLEAN NOT NULL DEFAULT FALSE,
    parent_folder_id UUID,
    owner_id UUID NOT NULL,
    group_id UUID,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    access_permissions VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_file_parent FOREIGN KEY (parent_folder_id) REFERENCES file_info(id)
);

-- Create file_permission table
CREATE TABLE file_permission (
    id UUID PRIMARY KEY,
    file_id UUID NOT NULL,
    user_id UUID NOT NULL,
    permission_type VARCHAR(20) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_permission_file FOREIGN KEY (file_id) REFERENCES file_info(id),
    CONSTRAINT uk_file_user UNIQUE (file_id, user_id)
);

-- Create indexes
CREATE INDEX idx_file_owner ON file_info(owner_id);
CREATE INDEX idx_file_parent ON file_info(parent_folder_id);
CREATE INDEX idx_file_group ON file_info(group_id);
CREATE INDEX idx_file_extension ON file_info(extension);
CREATE INDEX idx_file_media_type ON file_info(media_type);
CREATE INDEX idx_file_deleted ON file_info(is_deleted);

CREATE INDEX idx_permission_file ON file_permission(file_id);
CREATE INDEX idx_permission_user ON file_permission(user_id);
CREATE INDEX idx_permission_type ON file_permission(permission_type);