package com.hrms.modules.AttnDataPush.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushData {
    private Long EventType;
    private Long EmployeeID;
    private Long Card;
    private LocalTime AriseTime;
    private LocalDate AriseDate;
    private String Code;
}
