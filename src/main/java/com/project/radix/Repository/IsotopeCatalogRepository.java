package com.project.radix.Repository;

import com.project.radix.Model.IsotopeCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IsotopeCatalogRepository extends JpaRepository<IsotopeCatalog, Integer> {
    Optional<IsotopeCatalog> findByName(String name);
}