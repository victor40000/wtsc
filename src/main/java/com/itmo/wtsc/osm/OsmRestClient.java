package com.itmo.wtsc.osm;

import com.itmo.wtsc.dto.integration.osm.GetAreasResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OsmRestClient {

    private final String OVERPASS_GET_AREAS_URL
            = "http://www.overpass-api.de/api/interpreter?data=is_in(%s,%s);out;";

    public String getAreasTest(Double latitude, Double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .getForEntity(String.format(OVERPASS_GET_AREAS_URL, latitude, longitude), String.class);
        return response.getBody();
    }

    public GetAreasResponseDto getAreas(Double latitude, Double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GetAreasResponseDto> response = restTemplate
                .getForEntity(String.format(OVERPASS_GET_AREAS_URL, latitude, longitude), GetAreasResponseDto.class);
        return response.getBody();
    }

}
