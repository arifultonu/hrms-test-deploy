package com.hrms.dto.leave;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class leaveAssignDTO {
    private Boolean isAllEmp;

    private List<Long> hrCrEmpIdList;

    private List<Long> leaveTypeIdList;
}
