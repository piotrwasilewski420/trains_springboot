package com.springjpa.datajpa.Service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponseDTO {

    private List<StationDTO> data;
}
