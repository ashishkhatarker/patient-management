package com.ash.patient_service.controller;

import com.ash.patient_service.dto.PatientRequestDTO;
import com.ash.patient_service.dto.PatientResponseDTO;
import com.ash.patient_service.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
       List<PatientResponseDTO> patients = patientService.getPatients();

       return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO requestDTO){
    PatientResponseDTO patient  =  patientService.createPatient(requestDTO);

    return ResponseEntity.ok().body(patient);
    }

}
