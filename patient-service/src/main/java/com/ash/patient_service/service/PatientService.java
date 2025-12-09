package com.ash.patient_service.service;

import com.ash.patient_service.dto.PatientResponseDTO;
import com.ash.patient_service.mapper.PatientMapper;
import com.ash.patient_service.model.Patient;
import com.ash.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();

//        List<PatientResponseDTO> responseDTOs = patients.stream()
//                .map(PatientMapper::toDTO).toList();
//        return  responseDTOs;

        return patients.stream()
                .map(PatientMapper::toDTO).toList();
    }

}
