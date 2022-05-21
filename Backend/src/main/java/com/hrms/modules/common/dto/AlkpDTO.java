package com.hrms.modules.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlkpDTO {

    private String title;
    private String keyword;
    private String code;
    private String sequence;
    private Long parentId;
    private boolean isActive=true;

}
