package com.example.demo.exceptions;

import com.example.demo.dto.StatusCodeDTO;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StatusCodeDTO> notFoundHandler(NotFoundException notFoundException){
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        statusCodeDTO.setCode(404);
        statusCodeDTO.setMessage(notFoundException.getMessage());
        return new ResponseEntity<>(statusCodeDTO,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<StatusCodeDTO> noContentHandler(NoContentException noContentException){
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        statusCodeDTO.setCode(404);
        statusCodeDTO.setMessage(noContentException.getMessage());
        //Se evita el uso de HttpStatus.NO_CONTENT para poder mostrar un mensaje de respuesta
        return new ResponseEntity<>(statusCodeDTO,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StatusCodeDTO> conflictHandler(ConflictException conflictException){
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        statusCodeDTO.setCode(409);
        statusCodeDTO.setMessage(conflictException.getMessage());
        return new ResponseEntity<>(statusCodeDTO,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<StatusCodeDTO>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException){
        List<StatusCodeDTO> statusCodeDTOList = new ArrayList<>();
        List<ObjectError> errorList = methodArgumentNotValidException.getAllErrors();
        for (ObjectError error : errorList) {
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
            statusCodeDTO.setCode(400);
            statusCodeDTO.setMessage(error.getDefaultMessage());
            statusCodeDTOList.add(statusCodeDTO);
        }
        return new ResponseEntity<>(statusCodeDTOList,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<StatusCodeDTO> conversionFailedHandler(ConversionFailedException conversionFailedException){
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        statusCodeDTO.setCode(400);
        statusCodeDTO.setMessage("Algún valor ingresado no concuerda con el tipo de dato requerido, favor de validarlo");
        if (Objects.requireNonNull(conversionFailedException.getMessage()).contains("DateTimeFormat"))
            statusCodeDTO.setMessage("Formato de fecha debe ser dd/mm/aaaa.");
        return new ResponseEntity<>(statusCodeDTO,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StatusCodeDTO> MessageNotReadableHandler(HttpMessageNotReadableException httpMessageNotReadableException){
        StatusCodeDTO statusCodeDTO = new StatusCodeDTO();
        statusCodeDTO.setCode(400);
        statusCodeDTO.setMessage("Algún valor ingresado no concuerda con el tipo de dato requerido, favor de validarlo");
        if (Objects.requireNonNull(httpMessageNotReadableException.getMessage()).contains("DateTimeParseException"))
            statusCodeDTO.setMessage("Formato de fecha debe ser dd/mm/aaaa.");
        if (Objects.requireNonNull(httpMessageNotReadableException.getMessage()).contains("peopleAmount") || Objects.requireNonNull(httpMessageNotReadableException.getMessage()).contains("seats"))
            statusCodeDTO.setMessage("La cantidad de personas debe ser un valor numérico.");
        return new ResponseEntity<>(statusCodeDTO,HttpStatus.BAD_REQUEST);
    }
}
