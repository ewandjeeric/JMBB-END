package org.sid.service;


import org.sid.dao.ActiviteRepository;
import org.sid.dao.PersonnelRepository;
import org.sid.entities.Activite;
import org.sid.entities.Personnel;
import org.sid.exception.ActiviteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ActiviteServiceImpl implements ActiviteService{
	
	@Autowired
	private ActiviteRepository activiterepo;
	@Autowired
	private PersonnelRepository personnelRepository;

	@Override
	public Page<Activite> AllActivites(String noms, int page, int size) {
		// TODO Auto-generated method stub
		return activiterepo.findByNomContaining(noms, PageRequest.of(page, size, Sort.by("id").descending()));
	}

	@Override
	public Activite AddUpdateActivite(Activite activite) {
		try {
		return activiterepo.save(activite);
		} catch(DataAccessException e) {
			
			throw new ActiviteException(e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteActivite(Long id) {
		activiterepo.delete(activiterepo.findById(id).orElseThrow(()-> new ActiviteException(id)));
		
	}

	@Override
	public List<Activite> WeekActivites(Date datedebut, Date datefin, String username) {

		Personnel personnel = personnelRepository.findByUsername(username);
		return activiterepo.findByDateactiviteBetweenAndPersonnel(datedebut,datefin,personnel);
	}

}
