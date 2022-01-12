package com.springjpa.datajpa.Service;

import com.springjpa.datajpa.Entity.Geo;
import com.springjpa.datajpa.Entity.Station;
import com.springjpa.datajpa.Repository.GeoRepository;
import com.springjpa.datajpa.Service.dto.ApiResponseDTO;
import com.springjpa.datajpa.Service.dto.StationDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Data
public class GeoService {

    @Autowired
    private GeoRepository geoRepository;
    private List<Geo> geoList = new ArrayList<>();
    private Double longitude;
    private Double latitude;
    private String name;
    private String geoDTO;
    private ApiResponseDTO response;
    @Autowired
    private WebClient webClient;
    public List<Geo> downloadAllGeos(Collection<Station> stations){
        List<Geo> geoList = new ArrayList<>();
        stations.forEach(station -> {
            Geo geo = downloadSingleGeo(station);
            geoList.add(geo);
        });
        return geoList;
    }

    public Geo downloadSingleGeo(Station station) {
        try {
            name = station.getName();
            System.out.println(name);
            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("query", name)
                            .build())
                    .retrieve()
                    .bodyToMono(ApiResponseDTO.class)
                    .block();
            if (response != null) {
                Geo newGeo = createGeoFromResponse(response);
                System.out.println(newGeo);
                return newGeo;
            }
        } catch (RuntimeException e) {
            System.out.println("Error while fetching geo data for station " + name + ": " + e.getMessage());
        }
        return new Geo();
    }

    private StationDTO getSingleStationFromData(ApiResponseDTO response) {
        if (!CollectionUtils.isEmpty(response.getData()) && response.getData().size() == 1) {
            return response.getData().get(0);
        }
        throw new RuntimeException("Response is empty");
    }

    private Geo createGeoFromResponse(ApiResponseDTO response) {
        StationDTO stationDTO = getSingleStationFromData(response);
        Geo newGeo = new Geo();
        newGeo.setLatitude(stationDTO.getLatitude());
        newGeo.setLongitude(stationDTO.getLongitude());
        return newGeo;
    }

    public Iterable<Geo> addGeos(Collection<Station> stations) {
        return geoRepository.saveAll(downloadAllGeos(stations));
    }
}
