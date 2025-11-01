package com.example.pos_backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.util.Set; // Birden fazla 'User' için

@Entity
@Table (name = "roles") // Arkadaşınızın SQL'indeki tablo adı
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleID; // Arkadaşınızın SQL'indeki 'roleID'

    @Column(length = 50, unique = true, nullable = false)
    private String roleName; // Arkadaşınızın SQL'indeki 'roleName'

    // --- İlişki ---
    // Bir Rol (Role), birden fazla Kullanıcıya (User) atanabilir.
    // 'mappedBy = "role"': Bu, 'User' sınıfındaki 'private Role role;' alanına
    // bağlı olduğunu ve 'roles' tablosunda ekstra sütun oluşturulmamasını söyler.
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<User> users;

    // --- Constructor (Yapıcı Metotlar) ---
    public Role() {
    }

    // DatabaseInitializerService'te kullanmak için
    public Role(String roleName) {
        this.roleName = roleName;
    }

    // --- Getter ve Setter Metotları ---

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}