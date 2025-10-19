package com.example.boatsafarisalarysystem.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PayStructure")
public class PayStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer structureId;

    @Column(nullable = false)
    private String componentType; // e.g., 'fixed_monthly'

    @Column(nullable = false)
    private Double amount;

    private String description;

    @Column(nullable = false)
    private Date effectiveDate;

    private String role;

    // Getters and Setters
    public Integer getStructureId() { return structureId; }
    public void setStructureId(Integer structureId) { this.structureId = structureId; }
    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}