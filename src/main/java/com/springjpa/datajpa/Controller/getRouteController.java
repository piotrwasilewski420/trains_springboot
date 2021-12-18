package com.springjpa.datajpa.Controller;

import com.springjpa.datajpa.Entity.Station;
import com.springjpa.datajpa.Repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class getRouteController {
    @Autowired
    private StationRepository stationRepository;
    @GetMapping("api/station")
    public Station findStationByName(@RequestParam String name) {
    return stationRepository.findByName(name);
    }
}
