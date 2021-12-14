package com.springjpa.datajpa.Controller;

import com.springjpa.datajpa.Service.GeoService;
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
    @Autowired
    private GeoService geoService;

    @PostMapping("/addGeos")
    public String saveGeos() throws JSONException {
        geoService.addGeos();
        return "zapisano koordynaty";
    }
    @GetMapping("/test")
    public String testGeos(){
        geoService.getGeoList();
        return "zapisano koordynaty";
    }
}
