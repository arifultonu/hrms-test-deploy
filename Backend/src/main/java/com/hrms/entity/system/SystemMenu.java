package com.hrms.entity.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.entity.baseEntity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SYSTEM_MENU")
public class SystemMenu  extends BaseEntity {


    @Column(unique = true)
    String code;
    String description;

    String name;
    String url;
    String requestUrl;
    String customUrl;
    Integer sequence;
    Boolean hasChild;
    Boolean visibleToAll;
    String chkAuthorization;
    String chkAuthorizationChar;
    Boolean leftSideMenu;
    Boolean dashboardMenu;
    Boolean mainHeaderMenu;
    String urlCustom;
    String entityName;


    Boolean isChild;
    Boolean isOpenNewTab;
    Boolean isActive;

    // for ngx-admin template
    private String title;
    private String icon;
    private String link;
    private String home;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_menu_id", referencedColumnName = "id")
    @JsonIgnore
    SystemMenu parentMenu;

    @OneToMany(mappedBy ="parentMenu")
    public List<SystemMenu> children = new ArrayList<>();

    String parentMenuCode;

    Boolean superAdminAccessOnly;
    Boolean adminAccessOnly=false;



    public SystemMenu(String code, String description, String openUrl, String iconHtml, Boolean isActive,
                      Integer sequence,Boolean adminAccessOnly,String link,String title,String home) {
        this.code = code;
        this.description = description;
        this.url = openUrl;
        this.icon = iconHtml;
        this.isActive = isActive;
        this.sequence = sequence;
        this.adminAccessOnly=adminAccessOnly;
        this.link=link;
        this.title=title;
        this.home=home;

//        this.parentMenu = parentMenu;
    }
}