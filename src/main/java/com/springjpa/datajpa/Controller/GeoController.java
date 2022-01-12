package com.springjpa.datajpa.Controller;

import com.springjpa.datajpa.Service.GeoService;
import com.springjpa.datajpa.Service.StationScrapperService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geo")
public class GeoController {

    public GeoController(GeoService geoService, StationScrapperService stationScrapperService) {
        this.geoService = geoService;
        this.stationScrapperService = stationScrapperService;
    }

    private final GeoService geoService;
    private final StationScrapperService stationScrapperService;

    @PostMapping("/addGeos")
    public String saveGeos() throws JSONException {
        geoService.addGeos(stationScrapperService.getAllStations());
        return "zapisano koordynaty";
    }
    @GetMapping("/test")
    public String testGeos(){
        geoService.getGeoList();
        return "zapisano koordynaty";
    }
}
