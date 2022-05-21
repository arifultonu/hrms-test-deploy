package com.hrms.modules.system.counter;

import com.hrms.modules.common.entity.AllOrgMst;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SYS_SYSTEM_COUNTER")
public class SystemCounter {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    // control keys
    @ManyToOne
    AllOrgMst organization;
    String counterName;
    String code;

    String prefix;
    Long nextNumber;
    String suffix;
    Integer step;
    Integer counterWidth;
    String prefixSeparator;
    String suffixSeparator;
//    String outputSeparator

    Long beginLimit;
    Long finalLimit;
    String numerationType; // for generate Integer/Character

    String usedEntityName;
    Boolean fiscalYearPrefix;
    Boolean active;

    // for more flexible filter counter dropdown use below attributes
    String counterType; // use it for more flexible with dropdown option --- preloaded dropdown ---> like Sales Counter, BoM Counter

    // System log fields
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "CREATION_DATETIME")
    Date creationDateTime;
    @Column(name = "CREATION_USER")
    String creationUser;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "LAST_UPDATE_DATETIME")
    Date lastUpdateDateTime;
    @Column(name = "LAST_UPDATE_USER")
    String lastUpdateUser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllOrgMst getOrganization() {
        return organization;
    }

    public void setOrganization(AllOrgMst organization) {
        this.organization = organization;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(Long nextNumber) {
        this.nextNumber = nextNumber;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getCounterWidth() {
        return counterWidth;
    }

    public void setCounterWidth(Integer counterWidth) {
        this.counterWidth = counterWidth;
    }

    public String getPrefixSeparator() {
        return prefixSeparator;
    }

    public void setPrefixSeparator(String prefixSeparator) {
        this.prefixSeparator = prefixSeparator;
    }

    public String getSuffixSeparator() {
        return suffixSeparator;
    }

    public void setSuffixSeparator(String suffixSeparator) {
        this.suffixSeparator = suffixSeparator;
    }

    public Long getBeginLimit() {
        return beginLimit;
    }

    public void setBeginLimit(Long beginLimit) {
        this.beginLimit = beginLimit;
    }

    public Long getFinalLimit() {
        return finalLimit;
    }

    public void setFinalLimit(Long finalLimit) {
        this.finalLimit = finalLimit;
    }

    public String getNumerationType() {
        return numerationType;
    }

    public void setNumerationType(String numerationType) {
        this.numerationType = numerationType;
    }

    public String getUsedEntityName() {
        return usedEntityName;
    }

    public void setUsedEntityName(String usedEntityName) {
        this.usedEntityName = usedEntityName;
    }

    public Boolean getFiscalYearPrefix() {
        return fiscalYearPrefix;
    }

    public void setFiscalYearPrefix(Boolean fiscalYearPrefix) {
        this.fiscalYearPrefix = fiscalYearPrefix;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCounterType() {
        return counterType;
    }

    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Date creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }


}
