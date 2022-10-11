package org.sid.web;

import org.sid.entities.Activite;
import org.sid.service.ActiviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class ActiviteController {
	@Autowired
	private ActiviteService activiteservice;
	
	@GetMapping("/activites")
	@PostAuthorize("hasAuthority('ALL_ACTIVITES')")
	public Page<Activite> AllActivites(
			@RequestParam(name ="nom", defaultValue="") String nom, 
			@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size){
		
		return activiteservice.AllActivites(nom, size, page);
	}
	
	@PostMapping("/activites")
	@PostAuthorize("hasAuthority('ADD_ACTIVITE')")
	public Activite AddUpdateActivite(@Valid @RequestBody Activite activite ) {
		return activiteservice.AddUpdateActivite(activite);
	}
	
	@DeleteMapping("/activites/{id}")
	@PostAuthorize("hasAuthority('DELETE_ACTIVITE')")
	public void DeleteActivite(@PathVariable(name = "id") Long id) {
		activiteservice.deleteActivite(id);
	}


	@PostMapping("/activites/week/{username}")
	@PostAuthorize("hasAuthority('WEEEK_ACTIVITES')")
	public List<Activite> WeekActivite(@PathVariable(name = "username")String username,
									   @RequestParam(name = "datedebut")Date datedebut,
									   @RequestParam(name = "datefin")Date datefin){
		return activiteservice.WeekActivites(datedebut, datefin, username);
	}
	}
