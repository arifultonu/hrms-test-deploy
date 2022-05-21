package com.hrms.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class ReportBetweenDateDTO {

    LocalDate fromDate;
    LocalDate toDate;
    String empCode;
}
