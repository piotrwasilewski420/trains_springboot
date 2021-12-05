package com.springjpa.datajpa.Service;

import com.springjpa.datajpa.Entity.Geo;
import com.springjpa.datajpa.Entity.Station;
import com.springjpa.datajpa.Repository.GeoRepository;
import com.springjpa.datajpa.Service.dto.ApiResponseDTO;
import com.springjpa.datajpa.Service.dto.StationDTO;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class GeoService {
    @Autowired
    StationScrapperService stationScrapperService;
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

    public List<Geo> downloadAllGeos() throws JSONException {
        Iterable<Station> stations = stationScrapperService.getAllStations();
        stations.forEach(station -> {
            try {
                name = station.getName();
                System.out.println(name);
                ApiResponseDTO response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("query", "Warszawa")
                                .build())
                        .retrieve()
                        .bodyToMono(ApiResponseDTO.class)
                        .block();
                if (response != null) {
                    Geo newGeo = createGeoFromResponse(response);
                    System.out.println(newGeo);
                    geoList.add(newGeo);
                }
            } catch (RuntimeException e) {
                System.out.println("Error while fetching geo data for station " + name + ": " + e.getMessage());
            }

//            try {
//                jsonObject = new JSONObject(geoDTO);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                jsonArray = jsonObject.getJSONArray("data");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                jsonObject = jsonArray.getJSONObject(0);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                latitude = jsonObject.getDouble("latitude");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                longitude = jsonObject.getDouble("longitude");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            geoList.add(new Geo(longitude, latitude));
        });
        return geoList;
    }

    private Geo createGeoFromResponse(ApiResponseDTO response) {
        StationDTO stationDTO = getSingleStationFromData(response);
        Geo newGeo = new Geo();
        newGeo.setLatitude(stationDTO.getLatitude());
        newGeo.setLongitude(stationDTO.getLongitude());
        return newGeo;
    }

    private StationDTO getSingleStationFromData(ApiResponseDTO response) {
        if (!CollectionUtils.isEmpty(response.getData()) && response.getData().size() == 1) {
            return response.getData().get(0);
        }
        throw new RuntimeException("Response is empty");
    }

    public Iterable<Geo> addGeos() throws JSONException {
        return geoRepository.saveAll(downloadAllGeos());
    }
}
