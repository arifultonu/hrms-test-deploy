package com.hrms.dto.attn;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ViaHrAttnDTO {
    private  Long hrCrEmp;
    private LocalDate attendanceDate;

    private LocalTime inTime;

    private LocalTime outTime;
    private String remarks;
}
