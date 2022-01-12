package com.springjpa.datajpa.Service;

import com.springjpa.datajpa.Entity.Geo;
import com.springjpa.datajpa.Entity.Station;
import com.springjpa.datajpa.Repository.StationRepository;
import com.springjpa.datajpa.UrlLinks.UrlLinks;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class StationScrapperService {

    private Document document;
    private Elements elements;
    private String newHtml = UrlLinks.LINK_WITH_NAME_PARAM;
    private Elements subElements;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private GeoService geoService;

    /**
     * Get all the characters available from PortalPasazera site
     */
    public List<String> getCharsAvailable() {
        List<String> charsAvailable = new ArrayList<String>();
        try {
            document = Jsoup.connect(UrlLinks.MAIN_LINK).get();
            elements = document.getElementsByClass("swiper-wrapper");
            subElements = elements.first().getElementsByTag("a");
            subElements.forEach(character -> charsAvailable.add(character.text().toLowerCase()));
            return charsAvailable;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Station> getStations(List<String> charsavailable) {
//        Predicate<Station> stationPredicate = station -> station.getName().contains("▲");
        List<Station> stations = new ArrayList<>();
        charsavailable.forEach(character -> {
            try {
                document = Jsoup.connect(UrlLinks.LINK_WITH_NAME_PARAM + character).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = document.getElementsByClass("pagination");
            Elements subElements = elements.first().getElementsByTag("li");
            int lastPage = Integer.parseInt(subElements.get(subElements.size() - 2).text());
            for (int i = 1; i <= lastPage; i++) {
                try {
                    document = Jsoup.connect(UrlLinks.LINK_WITH_NAME_PARAM + character + "&p=" + i).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                elements = document.getElementsByClass("col-6 col-12--phone");
                for (Element e : elements) {
                    stations.add(new Station(e.getElementsByTag("h3").text().replaceAll("▲", "")));
                    System.out.println(e.getElementsByTag("h3").text());
                }
                System.out.println(i);
            }
        });
        List<Station> distinctStations = stations.stream()
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
        setGeosForEachStation(distinctStations);
        return distinctStations;
    }

    private void setGeosForEachStation(List<Station> distinctStations) {
        distinctStations.forEach(station -> {
            Geo geos = geoService.downloadSingleGeo(station);
            station.setGeo(geos);
        });
    }

    public Iterable<Station> populateDB() {
        return stationRepository.saveAll(getStations(getCharsAvailable()));
    }

    public Station getStationByName(String name) {
        return stationRepository.findByName(name);
    }

    public Collection<Station> getAllStations() {
        return stationRepository.findAll();
    }
}
