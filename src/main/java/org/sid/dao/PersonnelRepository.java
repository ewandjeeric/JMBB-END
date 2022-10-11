package org.sid.dao;

import org.sid.entities.Personnel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    Page<Personnel> findByNomsContaining(String noms, Pageable pageable);
    Personnel findByUsername(String username);

}
