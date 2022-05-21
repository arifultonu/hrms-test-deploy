package com.hrms.modules.system.visitorLog;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

@Data
@Entity
@Table(name = "SYS_VISITORS_LOG")
public class VisitorsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    Long id;

    String userId;// can be null ---> for public web page
    String userAgent;
    String ipAddress;
    String referrer; // come from which page ---> previous URL

    String visitPage;
    String visitUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "VISIT_DATETIME")
    LocalDateTime visitTime;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date lastActiveTime;

    String internetProfile;
    String internalReference;
    String externalReference;



    String actionLog; // what type action is taking - Create, Update, Read, Delete or others [Shortly keep, not details]

    // System log fields
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;

    @Column(name = "CREATION_USER")
    String creationUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;

    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;


}