package com.springjpa.datajpa.Controller;

import com.springjpa.datajpa.Entity.Station;
import com.springjpa.datajpa.Service.StationScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoadDbController {
    @Autowired
    private StationScrapperService stationScrapperService;

    @PostMapping("/loadDb")
    public String LoadDb(){
        stationScrapperService.populateDB();
        return "zapisano";
    }

    @GetMapping( "/getStation/{name}")
    public Station getStationByName(@PathVariable String name){
       return stationScrapperService.getStationByName(name);
    }

    @GetMapping( "/getAllStations")
    public Iterable<Station> getAllStations(){
        return stationScrapperService.getAllStations();
    }
}
