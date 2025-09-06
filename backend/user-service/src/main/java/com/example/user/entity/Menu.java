package com.example.user.entity;

import com.example.common.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity for Menu
 * Stores information about menus in the system
 */
@Entity
@Table(name = "menus")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseModel {
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    private String icon;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "router_id")
    private Router router;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @Column(name = "is_visible")
    private boolean visible = true;
}