package com.project.radix.Service;

import com.project.radix.DTO.SmartwatchRequest;
import com.project.radix.DTO.SmartwatchResponse;
import com.project.radix.Model.Patient;
import com.project.radix.Model.Smartwatch;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.SmartwatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartwatchService {

    private final SmartwatchRepository smartwatchRepository;
    private final PatientRepository patientRepository;

    private SmartwatchResponse toResponse(Smartwatch sw) {
        String patientName = null;
        if (sw.getPatient() != null) {
            patientName = sw.getPatient().getFullName();
        } else if (sw.getFkPatientId() != null) {
            patientName = patientRepository.findById(sw.getFkPatientId())
                    .map(Patient::getFullName).orElse(null);
        }
        return SmartwatchResponse.builder()
                .id(sw.getId())
                .imei(sw.getImei())
                .macAddress(sw.getMacAddress())
                .model(sw.getModel())
                .isActive(sw.getIsActive())
                .patientId(sw.getFkPatientId())
                .patientName(patientName)
                .build();
    }

    @Transactional
    public SmartwatchResponse create(SmartwatchRequest request) {
        if (request.getFkPatientId() == null) {
            throw new RuntimeException("Patient ID is required");
        }
        if (request.getImei() == null || request.getImei().isBlank()) {
            throw new RuntimeException("IMEI is required");
        }
        if (smartwatchRepository.findByImei(request.getImei()).isPresent()) {
            throw new RuntimeException("IMEI already registered");
        }
        if (request.getMacAddress() != null && smartwatchRepository.findByMacAddress(request.getMacAddress()).isPresent()) {
            throw new RuntimeException("MAC address already registered");
        }

        Smartwatch sw = new Smartwatch();
        sw.setFkPatientId(request.getFkPatientId());
        sw.setImei(request.getImei());
        sw.setMacAddress(request.getMacAddress());
        sw.setModel(request.getModel());
        sw.setIsActive(true);

        sw = smartwatchRepository.save(sw);
        return toResponse(sw);
    }

    public List<SmartwatchResponse> getAll() {
        return smartwatchRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public SmartwatchResponse getById(Integer id) {
        return smartwatchRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Smartwatch not found"));
    }

    public List<SmartwatchResponse> getByPatient(Integer patientId) {
        return smartwatchRepository.findByFkPatientId(patientId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SmartwatchResponse update(Integer id, SmartwatchRequest request) {
        Smartwatch sw = smartwatchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Smartwatch not found"));

        if (request.getFkPatientId() != null) sw.setFkPatientId(request.getFkPatientId());
        if (request.getModel() != null) sw.setModel(request.getModel());
        if (request.getMacAddress() != null) sw.setMacAddress(request.getMacAddress());

        sw = smartwatchRepository.save(sw);
        return toResponse(sw);
    }

    @Transactional
    public void deactivate(Integer id) {
        Smartwatch sw = smartwatchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Smartwatch not found"));
        sw.setIsActive(false);
        smartwatchRepository.save(sw);
    }
}
