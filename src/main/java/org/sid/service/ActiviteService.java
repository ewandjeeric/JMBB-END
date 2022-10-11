package org.sid.service;


import org.sid.entities.Activite;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface ActiviteService {
	Page<Activite> AllActivites(String noms, int page, int size);
	Activite AddUpdateActivite(Activite activite);
	void deleteActivite(Long id);
	List<Activite> WeekActivites(Date datedebut, Date datefin, String username);
	

}
