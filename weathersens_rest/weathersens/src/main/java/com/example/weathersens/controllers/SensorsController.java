package com.example.weathersens.controllers;

import com.example.weathersens.dto.SensorDTO;
import com.example.weathersens.models.Sensor;
import com.example.weathersens.services.SensorService;
import com.example.weathersens.util.MeasurementErrorResponse;
import com.example.weathersens.util.MeasurementErrorsUtil;
import com.example.weathersens.util.MeasurementException;
import com.example.weathersens.util.SensorValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorService sensorService, SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {
        Sensor sensorToRegister = convertToSensor(sensorDTO);
        sensorValidator.validate(sensorToRegister, bindingResult);
        if (bindingResult.hasErrors()) {
            MeasurementErrorsUtil.measurementErrorToClient(bindingResult);
        }
        sensorService.register(sensorToRegister);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleMeasurementException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
