package com.hrms.dto.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {
    @JsonInclude(NON_NULL)
    @JsonProperty("property")
    private Long allOrgMstSectionId;

    @JsonInclude(NON_NULL)
    @JsonProperty("property")
    private Long allOrgMstSubSection;
}
