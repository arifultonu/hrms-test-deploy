package com.hrms.dto.attn;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class WrkDeviceAttData2 {
    private Long EventType;
    private Long EmployeeID;
    private Long Card;
    private LocalTime AriseTime;
    private LocalDate AriseDate;
    private String Code;
}
