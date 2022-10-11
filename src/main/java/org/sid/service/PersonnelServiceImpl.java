package org.sid.service;


import org.sid.dao.PersonnelRepository;
import org.sid.entities.Personnel;
import org.sid.exception.PersonnelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PersonnelServiceImpl implements PersonnelService {
	@Autowired
	private PersonnelRepository personnelrepo;


	@Override
	public Page<Personnel> AllPersonnels(String noms, int page, int size) {
		
		return personnelrepo.findByNomsContaining(noms, PageRequest.of(page, size, Sort.by("id").descending()));
	}
	
	@Override
	public Personnel getPersonnel(Long id) {
		return personnelrepo.findById(id).orElseThrow(() -> new PersonnelException(id));
	}

	@Override
	public Personnel AddPersonnel(Personnel personnel) {

		try {
			personnel.setPassword(new BCryptPasswordEncoder().encode(personnel.getPassword()));
			return personnelrepo.save(personnel);
			
		}catch(DataAccessException e) {
			
			throw new PersonnelException(e.getLocalizedMessage());
		}
		
	}

	@Override
	public Personnel UpdatePersonnel(Personnel personnel) {
		try {
			return personnelrepo.save(personnel);

		}catch(DataAccessException e) {

			throw new PersonnelException(e.getLocalizedMessage());
		}
	}

	@Override
	public void ResetPassword(Long id, String newpassword) {


		try {
			Personnel personnel = this.getPersonnel(id);

			personnel.setPassword(new BCryptPasswordEncoder().encode(newpassword));
			personnelrepo.save(personnel);

		}catch(DataAccessException e) {

			throw new PersonnelException(e.getLocalizedMessage());
		}

	}


	@Override
	public void DeletePersonnel(Long id) {
		personnelrepo.delete(this.getPersonnel(id));
		
	}

	@Override
	public Personnel findByUsername(String username) {
		
		return personnelrepo.findByUsername(username);
	}

	

	

}
