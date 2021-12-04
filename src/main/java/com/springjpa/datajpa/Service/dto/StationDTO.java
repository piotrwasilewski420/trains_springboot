package com.springjpa.datajpa.Service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationDTO {

    private Double latitude;
    private Double longitude;
}
