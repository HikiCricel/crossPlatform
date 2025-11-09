package com.example.crossPlatform.model;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long id;
    @Size(min=2, max=100, message = "name")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @ManyToMany()
    private Set<Permission> permissions;
    @OneToMany()
    private Set<User> users;

    @Override
    public String getAuthority(){
        return this.name.toUpperCase();
    }
}
