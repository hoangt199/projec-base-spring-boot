package com.example.user.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity for Router
 * Stores information about routes in the system
 */
@Entity
@Table(name = "routers")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Router extends BaseModel {
    
    @Column(nullable = false)
    private String path;
    
    @Column(nullable = false)
    private String name;
    
    private String component;
    
    private String redirect;
    
    private String icon;
    
    @Column(name = "is_hidden")
    private boolean hidden;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "is_active")
    private boolean active = true;
}