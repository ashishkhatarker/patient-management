package com.ash.patient_service.service;

import com.ash.patient_service.dto.PatientRequestDTO;
import com.ash.patient_service.dto.PatientResponseDTO;
import com.ash.patient_service.exception.EmailAlreadyExistsException;
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

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient with this email"
            + " already exists "+ patientRequestDTO.getEmail());
        }

          Patient newPatient = patientRepository.save(
                  PatientMapper.toModel(patientRequestDTO));

          // an email address should be unique

          return PatientMapper.toDTO(newPatient);

    }


}
