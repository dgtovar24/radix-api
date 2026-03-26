package com.proyecto.radix.FHIR;

import org.hl7.fhir.r4.model.*;
import com.proyecto.radix.Model.Paciente;
import java.util.Date;

public class PacienteToFhirConverter {

    public static Patient convertToFhir(Paciente paciente) {
        Patient fhirPatient = new Patient();
        
        fhirPatient.setId(paciente.getId().toString());
        
        HumanName name = new HumanName();
        name.setFamily(paciente.getName());
        name.addGiven(paciente.getName());
        fhirPatient.addName(name);
        
        if (paciente.getPhone() != null) {
            ContactPoint phone = new ContactPoint();
            phone.setSystem(ContactPoint.ContactPointSystem.PHONE);
            phone.setUse(ContactPoint.ContactPointUse.MOBILE);
            phone.setValue(paciente.getPhone());
            fhirPatient.addTelecom(phone);
        }
        
        if (paciente.getAddress() != null) {
            Address address = new Address();
            address.addLine(paciente.getAddress());
            fhirPatient.addAddress(address);
        }
        
        return fhirPatient;
    }

    public static Paciente convertFromFhir(Patient fhirPatient) {
        Paciente paciente = new Paciente();
        
        if (fhirPatient.getIdElement() != null) {
            paciente.setId(Integer.parseInt(fhirPatient.getIdElement().getIdPart()));
        }
        
        if (!fhirPatient.getName().isEmpty()) {
            HumanName name = fhirPatient.getName().get(0);
            StringBuilder fullName = new StringBuilder();
            for (StringType given : name.getGiven()) {
                fullName.append(given.getValue()).append(" ");
            }
            if (name.getFamily() != null) {
                fullName.append(name.getFamily());
            }
            paciente.setName(fullName.toString().trim());
        }
        
        for (ContactPoint telecom : fhirPatient.getTelecom()) {
            if (telecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                paciente.setPhone(telecom.getValue());
            }
        }
        
        if (!fhirPatient.getAddress().isEmpty()) {
            Address addr = fhirPatient.getAddress().get(0);
            StringBuilder addrBuilder = new StringBuilder();
            for (StringType addrLine : addr.getLine()) {
                addrBuilder.append(addrLine.getValue()).append(", ");
            }
            if (addr.getCity() != null) addrBuilder.append(addr.getCity());
            if (addr.getCountry() != null) addrBuilder.append(", ").append(addr.getCountry());
            paciente.setAddress(addrBuilder.toString().trim());
        }
        
        return paciente;
    }
}
