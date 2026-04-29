package com.project.radix.Service;

import com.project.radix.DTO.TreatmentCreateRequest;
import com.project.radix.DTO.TreatmentResponse;
import com.project.radix.Model.Patient;
import com.project.radix.Model.Treatment;
import com.project.radix.Model.User;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.TreatmentRepository;
import com.project.radix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ConfinementCalculationService confinementService;

    @Transactional
    public TreatmentResponse createTreatment(TreatmentCreateRequest request, Integer doctorId) {
        ConfinementCalculationService.ConfinementResult calc =
            confinementService.calculate(request.getFkRadioisotopeId(), request.getInitialDose());

        Patient patient = patientRepository.findById(request.getFkPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        User doctor = userRepository.findById(doctorId)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Treatment treatment = new Treatment();
        treatment.setFkPatientId(patient.getId());
        treatment.setFkDoctorId(doctorId);
        treatment.setFkRadioisotopeId(request.getFkRadioisotopeId());
        treatment.setFkSmartwatchId(request.getFkSmartwatchId());
        treatment.setRoom(request.getRoom());
        treatment.setInitialDose(request.getInitialDose());
        treatment.setSafetyThreshold(calc.safetyThreshold());
        treatment.setIsolationDays(calc.isolationDays());
        treatment.setStartDate(LocalDateTime.now());
        treatment.setIsActive(true);

        treatment = treatmentRepository.save(treatment);

        return toResponse(treatment, patient, doctor);
    }

    public List<TreatmentResponse> getAllTreatments() {
        return treatmentRepository.findAll().stream()
            .map(t -> {
                Patient patient = patientRepository.findById(t.getFkPatientId()).orElse(null);
                User doctor = userRepository.findById(t.getFkDoctorId()).orElse(null);
                return toResponse(t, patient, doctor);
            })
            .toList();
    }

    public List<TreatmentResponse> getActiveTreatments() {
        return treatmentRepository.findByIsActiveTrue().stream()
            .map(t -> {
                Patient patient = patientRepository.findById(t.getFkPatientId()).orElse(null);
                User doctor = userRepository.findById(t.getFkDoctorId()).orElse(null);
                return toResponse(t, patient, doctor);
            })
            .toList();
    }

    public List<TreatmentResponse> getTreatmentsByPatient(Integer patientId) {
        return treatmentRepository.findByFkPatientId(patientId).stream()
            .map(t -> {
                Patient patient = patientRepository.findById(t.getFkPatientId()).orElse(null);
                User doctor = userRepository.findById(t.getFkDoctorId()).orElse(null);
                return toResponse(t, patient, doctor);
            })
            .toList();
    }

    public Optional<TreatmentResponse> getTreatmentById(Integer id) {
        return treatmentRepository.findById(id)
            .map(t -> {
                Patient patient = patientRepository.findById(t.getFkPatientId()).orElse(null);
                User doctor = userRepository.findById(t.getFkDoctorId()).orElse(null);
                return toResponse(t, patient, doctor);
            });
    }

    @Transactional
    public Optional<TreatmentResponse> endTreatment(Integer id) {
        return treatmentRepository.findById(id)
            .map(t -> {
                t.setIsActive(false);
                t.setEndDate(LocalDateTime.now());
                treatmentRepository.save(t);

                Patient patient = patientRepository.findById(t.getFkPatientId()).orElse(null);
                User doctor = userRepository.findById(t.getFkDoctorId()).orElse(null);
                return toResponse(t, patient, doctor);
            });
    }

    private TreatmentResponse toResponse(Treatment t, Patient patient, User doctor) {
        String isotopeName = "Unknown";
        return TreatmentResponse.builder()
            .id(t.getId())
            .patientId(t.getFkPatientId())
            .patientName(patient != null ? patient.getFullName() : "Unknown")
            .doctorId(t.getFkDoctorId())
            .doctorName(doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : "Unknown")
            .isotopeId(t.getFkRadioisotopeId())
            .isotopeName(isotopeName)
            .room(t.getRoom())
            .initialDose(t.getInitialDose())
            .safetyThreshold(t.getSafetyThreshold())
            .isolationDays(t.getIsolationDays())
            .startDate(t.getStartDate())
            .endDate(t.getEndDate())
            .isActive(t.getIsActive())
            .build();
    }
}