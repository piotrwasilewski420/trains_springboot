package com.springjpa.datajpa.Controller;

import com.springjpa.datajpa.Entity.Station;
import com.springjpa.datajpa.Service.StationScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class StationsController {
    @Autowired
    StationScrapperService stationScrapperService;
    @GetMapping("/api/name/{name}")
    public Station getStationByName(@PathVariable("name") String name){
        return stationScrapperService.getStationByName(name);
    }
}
///api/getStation