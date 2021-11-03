package com.example.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws JsonProcessingException {

        SpringApplication.run(Application.class, args);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://91.241.64.178:7081/api/users";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);


        String cookie = response.getHeaders().get("set-cookie").get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie);

        User user = new User(Long.valueOf(3), "James", "Brown", (byte) 12);
        ObjectMapper mapper = new ObjectMapper();
        String sus = "";
        sus = mapper.writeValueAsString(user);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(sus, headers);


        ResponseEntity<String> resp = restTemplate
                .exchange(url, HttpMethod.POST, request, String.class);

        String firstPart = resp.getBody();

        user.setName("Thomas");
        user.setLastName("Shelby");
        String updatedInstance = mapper.writeValueAsString(user);;
        HttpEntity<String> requestUpdate = new HttpEntity<>(updatedInstance, headers);
        ResponseEntity<String> updateResp = restTemplate.exchange(url, HttpMethod.PUT, requestUpdate, String.class);

        String secondPart = updateResp.getBody();

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> delRest = restTemplate.exchange(url + "/" + user.getId(), HttpMethod.DELETE, entity, String.class);

        System.out.println(firstPart + secondPart + delRest.getBody());
    }

}
