package com.hrms.dto.System;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.entity.system.SystemMenu;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
public class SystemMenuAdminCoreUiDTO {

    private String name;
    private String url;
    private String icon;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_menu_id", referencedColumnName = "id")
    @JsonIgnore
    SystemMenu parentMenu;

    @OneToMany(mappedBy ="parentMenu")
    public List<SystemMenu> children = new ArrayList<>();
}
