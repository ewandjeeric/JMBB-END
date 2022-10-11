package org.sid.service;


import org.sid.entities.Personnel;
import org.springframework.data.domain.Page;

public interface PersonnelService {
	Page<Personnel> AllPersonnels(String noms, int size, int page);
	Personnel getPersonnel(Long id);
	Personnel findByUsername(String username);
	Personnel AddPersonnel(Personnel personnel);
	Personnel UpdatePersonnel(Personnel personnel);
	void ResetPassword(Long id, String newpassword);
	void DeletePersonnel(Long id);
	
	
}
