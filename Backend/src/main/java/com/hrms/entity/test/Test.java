package com.hrms.entity.test;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.modules.common.entity.AllOrgMst;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_name")
public class Test extends BaseEntity {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "ALL_ORG_MST_SECTION_ID",nullable = true)
    private AllOrgMst allOrgMstSection;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALL_ORG_MST_SUB_SECTION_ID",nullable = true)
    private AllOrgMst allOrgMstSubSection;


	
}