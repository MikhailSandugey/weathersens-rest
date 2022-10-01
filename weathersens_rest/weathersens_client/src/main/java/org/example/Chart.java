package org.example;

import org.example.dto.MeasurementDTO;
import org.example.dto.MeasurementResponse;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Chart {
    public static void main(String[] args) {
        List<Double> temperatures = getTemperatures();
        double[] xData = IntStream.range(0, temperatures.size()).asDoubleStream().toArray();
        double[] yData = temperatures.stream().mapToDouble(o -> o).toArray();

        XYChart chart = QuickChart.getChart("Temperature chart", "X", "Y", "Temperature",
                xData, yData);
        new SwingWrapper(chart).displayChart();
    }

    private static List<Double> getTemperatures() {
        RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/measurements";
        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);
        return response.getMeasurements().stream().map(MeasurementDTO::getValue).collect(Collectors.toList());
    }
}
