package com.proyecto.radix.Service;

import com.proyecto.radix.FHIR.PacienteToFhirConverter;
import com.proyecto.radix.Model.Paciente;
import com.proyecto.radix.Repository.PacienteRepository;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FHIRPatientService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    public Patient createPatientFromMap(Map<String, Object> patientMap) {
        Paciente paciente = new Paciente();
        paciente.setName(extractName(patientMap));
        paciente.setPhone(extractPhone(patientMap));
        paciente.setAddress(extractAddress(patientMap));
        paciente = pacienteRepository.save(paciente);
        return PacienteToFhirConverter.convertToFhir(paciente);
    }

    private String extractName(Map<String, Object> patientMap) {
        if (!patientMap.containsKey("name")) return "";
        List<Map<String, Object>> names = (List<Map<String, Object>>) patientMap.get("name");
        if (names.isEmpty()) return "";
        Map<String, Object> nameMap = names.get(0);
        String family = nameMap.containsKey("family") ? nameMap.get("family").toString() : "";
        List<String> givenList = nameMap.containsKey("given") ? (List<String>) nameMap.get("given") : new ArrayList<>();
        String given = String.join(" ", givenList);
        return family + " " + given;
    }

    @SuppressWarnings("unchecked")
    private String extractPhone(Map<String, Object> patientMap) {
        if (!patientMap.containsKey("telecom")) return "";
        List<Map<String, Object>> telecoms = (List<Map<String, Object>>) patientMap.get("telecom");
        for (Map<String, Object> telecom : telecoms) {
            String system = telecom.get("system") != null ? telecom.get("system").toString() : "";
            if ("phone".equals(system)) {
                return telecom.get("value") != null ? telecom.get("value").toString() : "";
            }
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private String extractAddress(Map<String, Object> patientMap) {
        if (!patientMap.containsKey("address")) return "";
        List<Map<String, Object>> addresses = (List<Map<String, Object>>) patientMap.get("address");
        if (addresses.isEmpty()) return "";
        List<String> lines = (List<String>) addresses.get(0).getOrDefault("line", new ArrayList<>());
        return String.join(", ", lines);
    }

    @Transactional
    public Patient createPatient(Patient fhirPatient) {
        Paciente paciente = PacienteToFhirConverter.convertFromFhir(fhirPatient);
        paciente = pacienteRepository.save(paciente);
        return PacienteToFhirConverter.convertToFhir(paciente);
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatient(String id) {
        return pacienteRepository.findById(Integer.parseInt(id))
            .map(PacienteToFhirConverter::convertToFhir);
    }

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return pacienteRepository.findAll().stream()
            .map(PacienteToFhirConverter::convertToFhir)
            .toList();
    }

    @Transactional
    public Patient updatePatientFromMap(String id, Map<String, Object> patientMap) {
        Paciente existing = pacienteRepository.findById(Integer.parseInt(id))
            .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
        
        if (patientMap.containsKey("name")) {
            existing.setName(extractName(patientMap));
        }
        if (patientMap.containsKey("telecom")) {
            String phone = extractPhone(patientMap);
            if (!phone.isEmpty()) {
                existing.setPhone(phone);
            }
        }
        
        existing = pacienteRepository.save(existing);
        return PacienteToFhirConverter.convertToFhir(existing);
    }

    @Transactional
    public Patient updatePatient(String id, Patient fhirPatient) {
        Paciente existing = pacienteRepository.findById(Integer.parseInt(id))
            .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
        
        Paciente updated = PacienteToFhirConverter.convertFromFhir(fhirPatient);
        updated.setId(existing.getId());
        updated = pacienteRepository.save(updated);
        
        return PacienteToFhirConverter.convertToFhir(updated);
    }

    @Transactional
    public void deletePatient(String id) {
        pacienteRepository.deleteById(Integer.parseInt(id));
    }
}