package org.sid.dao;


import org.sid.entities.Activite;
import org.sid.entities.Personnel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ActiviteRepository extends JpaRepository<Activite, Long> {
	Page<Activite> findByNomContaining(String nom, Pageable pageale);
	List<Activite> findByDateactiviteBetweenAndPersonnel(Date datedebut, Date datefin, Personnel personnel);
}
