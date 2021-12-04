package com.springjpa.datajpa.Service;

import com.springjpa.datajpa.Entity.Geo;
import com.springjpa.datajpa.Entity.Station;
import com.springjpa.datajpa.Repository.GeoRepository;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class GeoService {
    @Autowired
    private GeoRepository geoRepository;
    private List<Geo> geoList = new ArrayList<>();
    private Double longitude;
    private Double latitude;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String name;
    private String geoDTO;
    @Autowired
    private WebClient webClient;
    @Autowired
    StationScrapperService stationScrapperService;
    public List<Geo> downloadAllGeos() throws JSONException {
        Iterable<Station> stations = stationScrapperService.getAllStations();
            stations.forEach(station -> {
            name=station.getName();
                System.out.println(name);
            geoDTO = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("query","Warszawa")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
                try {
                    jsonObject = new JSONObject(geoDTO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonArray = jsonObject.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject = jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    latitude = jsonObject.getDouble("latitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    longitude = jsonObject.getDouble("longitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                geoList.add(new Geo(longitude,latitude));
            });
        return geoList;
    }
    public Iterable<Geo> addGeos() throws JSONException {
        return geoRepository.saveAll(downloadAllGeos());
    }
}
