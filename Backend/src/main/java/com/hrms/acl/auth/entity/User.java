package com.hrms.acl.auth.entity;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACL_USER")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    @NotBlank(message = "*Name is mandatory")
    @Column(name = "USERNAME", length = 15, nullable = false, unique = true)
    private String username;

    @NotBlank
    private String password;
    private String userTitle;
    private Boolean groupUser;  // 0 or 1
    private String groupUsername;

    @Email
    @NotBlank(message = "email must not be empty")
    private String email;
    private Boolean enabled=false;
    private Boolean isEmpCreated=false;

    private Boolean accountExpired;
    private Boolean accountLocked;
    private Boolean passwordExpired;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "acl_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User(String username, String email, String firstName, String lastName, String phone,String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }



    public User() {
    }

    public User(String username, String email, String userTitle, Boolean groupUser, String groupUsername, String encode) {
        this.username=username;
        this.email=email;
        this.userTitle=userTitle;
        this.groupUser=groupUser;
        this.groupUsername=groupUsername;
        this.password=encode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public Boolean getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(Boolean groupUser) {
        this.groupUser = groupUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEmpCreated() {
        return isEmpCreated;
    }

    public void setEmpCreated(Boolean empCreated) {
        isEmpCreated = empCreated;
    }

    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Boolean getPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(Boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getGroupUsername() {
        return groupUsername;
    }

    public void setGroupUsername(String groupUsername) {
        this.groupUsername = groupUsername;
    }
}
