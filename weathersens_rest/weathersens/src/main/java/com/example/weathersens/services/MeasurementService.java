package com.example.weathersens.services;

import com.example.weathersens.models.Measurement;
import com.example.weathersens.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    @Transactional
    public void addMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    public Long countRainyDays() {
        return findAll().stream().filter(Measurement::getRaining).count();
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setSensor(sensorService.getByName(measurement.getSensor().getName()).get());
        measurement.setDate_time(LocalDateTime.now());
    }
}
