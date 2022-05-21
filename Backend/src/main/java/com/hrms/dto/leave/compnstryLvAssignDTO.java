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
public class compnstryLvAssignDTO {
    private List<Long> hrCrEmpIdList;
    private String status;
}
