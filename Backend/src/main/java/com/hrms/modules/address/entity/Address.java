package com.hrms.modules.address.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.modules.common.entity.AllOrgMst;
import javax.persistence.*;

@Entity
@Table(name = "BAS_ADDRESS")
public class Address {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    String title;
    String name;

    String addressType;
    String addressCode;
    String address;
    String addressee;

    String addressLine1;
    String addressLine2;
    String addressLine3;

    String postalCode;
    String addressPhoneNumber;
    String addressFaxNumber;
    String emailAddress;
    Boolean isActive;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALL_ORG_MST_ID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AllOrgMst allOrgMst;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressPhoneNumber() {
        return addressPhoneNumber;
    }

    public void setAddressPhoneNumber(String addressPhoneNumber) {
        this.addressPhoneNumber = addressPhoneNumber;
    }

    public String getAddressFaxNumber() {
        return addressFaxNumber;
    }

    public void setAddressFaxNumber(String addressFaxNumber) {
        this.addressFaxNumber = addressFaxNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public AllOrgMst getAllOrgMst() {
        return allOrgMst;
    }

    public void setAllOrgMst(AllOrgMst allOrgMst) {
        this.allOrgMst = allOrgMst;
    }



}
