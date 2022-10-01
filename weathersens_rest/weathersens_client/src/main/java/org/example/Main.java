package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String sensorName = "Sensor_1";
        registerSensor(sensorName);

        Random random = new Random();
        double minTemperature = -50;
        double maxTemperature = 50;
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
            sendMeasurement(random.nextDouble() * (maxTemperature - minTemperature) + minTemperature,
                    random.nextBoolean(),
                    sensorName);
        }
    }

    private static void registerSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors/registration";
        Map<String, Object> jsonToSend = new HashMap<>();
        jsonToSend.put("name", sensorName);
        makePostRequest(url, jsonToSend);
    }

    private static void sendMeasurement(double value, boolean isRaining, String sensorName) {
        final String url = "http://localhost:8080/measurements/add";
        Map<String, Object> jsonToSend = new HashMap<>();
        jsonToSend.put("value", value);
        jsonToSend.put("raining", isRaining);
        jsonToSend.put("sensor", Map.of("name", sensorName));
        makePostRequest(url, jsonToSend);
    }

    private static void makePostRequest(String url, Map<String, Object> json) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(json, httpHeaders);
        System.out.println(restTemplate.postForObject(url, request, String.class));
    }
}