package com.hrms.acl.authCust.resAuth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.authCust.resDef.SysResourceDefinition;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ACL_SYSTEM_RESOURCE_AUTH")
public class SysResourceAuthorization {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    // system resource, which one, we want to check authorization
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "SYSTEM_RESOURCE_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    SysResourceDefinition systemResource;

    @Column(name = "SYSTEM_RESOURCE_NAME")
    String systemResourceName;              // Entity Name, Menu and Others ....

    String createAuth;                      // form/object
    String readAuth;                        // form/object
    String updateAuth;                      // form/object
    String deleteAuth;                      // form/object
    String queryAuth;                       // list/collection query permission
    String submitAuth;                      // parameter request / single form --- has not domain class

    String crudqsString;
    String othersString;
    String fullPrivilegeString;

    Boolean visibleToAll; // one row will be inserted with blank user and tik this attribute

    // this resource authorization by
    // 1. user or group wise auth
    String username;                        // can be user or group user
    // 2. role base auth
    @ManyToOne
    Role role;

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;
    @Column(name = "CREATION_USER")
    String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SysResourceDefinition getSystemResource() {
        return systemResource;
    }

    public void setSystemResource(SysResourceDefinition systemResource) {
        this.systemResource = systemResource;
    }

    public String getSystemResourceName() {
        return systemResourceName;
    }

    public void setSystemResourceName(String systemResourceName) {
        this.systemResourceName = systemResourceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCreateAuth() {
        return createAuth;
    }

    public void setCreateAuth(String createAuth) {
        this.createAuth = createAuth;
    }

    public String getReadAuth() {
        return readAuth;
    }

    public void setReadAuth(String readAuth) {
        this.readAuth = readAuth;
    }

    public String getUpdateAuth() {
        return updateAuth;
    }

    public void setUpdateAuth(String updateAuth) {
        this.updateAuth = updateAuth;
    }

    public String getDeleteAuth() {
        return deleteAuth;
    }

    public void setDeleteAuth(String deleteAuth) {
        this.deleteAuth = deleteAuth;
    }

    public String getQueryAuth() {
        return queryAuth;
    }

    public void setQueryAuth(String queryAuth) {
        this.queryAuth = queryAuth;
    }

    public String getSubmitAuth() {
        return submitAuth;
    }

    public void setSubmitAuth(String submitAuth) {
        this.submitAuth = submitAuth;
    }

    public String getCrudqsString() {
        return crudqsString;
    }

    public void setCrudqsString(String crudqsString) {
        this.crudqsString = crudqsString;
    }

    public String getOthersString() {
        return othersString;
    }

    public void setOthersString(String othersString) {
        this.othersString = othersString;
    }

    public String getFullPrivilegeString() {
        return fullPrivilegeString;
    }

    public void setFullPrivilegeString(String fullPrivilegeString) {
        this.fullPrivilegeString = fullPrivilegeString;
    }

    public Boolean getVisibleToAll() {
        return visibleToAll;
    }

    public void setVisibleToAll(Boolean visibleToAll) {
        this.visibleToAll = visibleToAll;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }


}
