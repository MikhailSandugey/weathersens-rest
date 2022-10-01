package com.example.weathersens.util;

import com.example.weathersens.models.Measurement;
import com.example.weathersens.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;
        if (measurement.getSensor() == null) {
            return;
        }
        if (sensorService.getByName(measurement.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "This sensor isn't registered!");
        }
    }
}
