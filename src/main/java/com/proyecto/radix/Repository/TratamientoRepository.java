package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Integer> {
    List<Tratamiento> findByStatusOrderByStartDateDesc(String status);
    List<Tratamiento> findByStatusNotOrderByStartDateDesc(String status);
}
