package com.hrms.modules.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.hris.entity.HrCrEmp;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Alkp extends BaseEntity {


    private String code;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="parent_id",referencedColumnName = "id")
    @JsonIgnore
    private Alkp parentId;

    @OneToMany(mappedBy="parentId")
    public List<Alkp> subALKP = new ArrayList<>();

    private String title;

    private String titleBng;

    private String keyword;

    private String alkpType;

    private Boolean isActive=true;

//    private Long slNo;

    private Long rowCount;

    private Long refId;

    private String sequence;

    private LocalDateTime macLastUpdateDate;
    private Boolean isStable;
    private String fullName;
    private LocalDateTime updateTime;
    private String className;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MODIFIED_BY",referencedColumnName = "id")
    private HrCrEmp modifiedBy;

    private LocalDate created;
    @Column(name = "APRVL_DATE")
    private LocalDate aprvlDate;
}
