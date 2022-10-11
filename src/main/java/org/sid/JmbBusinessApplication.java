package org.sid;


import org.sid.entities.*;
import org.sid.service.PersonnelService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.Date;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(Image.class)
public class JmbBusinessApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmbBusinessApplication.class, args);
	}

	@Bean
	CommandLineRunner start(PersonnelService personnelService) {
		return arg -> {
			AppRole droit1 = new AppRole("ALL_PERSONNELS","Afficher tout le personnel");
			AppRole droit2 = new AppRole("GET_PERSONNEL","Afficher une personne");
			AppRole droit3 = new AppRole("ADD_PERSONNEL", "Ajoute un personnel");
			AppRole droit4 = new AppRole("DELETE_PERSONNEL", "Supprimer une personne");
			AppRole droit5 = new AppRole("ALL_ACTIVITES", "Tous les activites");
			AppRole droit6 = new AppRole("ADD_ACTIVITE","Ajouter une activite");
			AppRole droit7 = new AppRole("DELETE_ACTIVITE", "Supprimer une activite");
			AppRole droit8 = new AppRole("WEEEK_ACTIVITES", "Activite hebdomadaire");
			AppRole droit9 = new AppRole("IMAGE_UPLOADE", "Uploade l'image");
			AppRole droit10 = new AppRole("IMAGE_DOWNLOAD", "Telecharger l'image");

			Personnel admin = new Personnel("jmbadmin","jmb","admin","admin@jmbbusiness.cm","admin@12");

			admin.getRoles().add(droit1);
			admin.getRoles().add(droit2);
			admin.getRoles().add(droit3);
			admin.getRoles().add(droit4);
			admin.getRoles().add(droit5);
			admin.getRoles().add(droit6);
			admin.getRoles().add(droit7);
			admin.getRoles().add(droit8);
			admin.getRoles().add(droit9);
			admin.getRoles().add(droit10);

			Activite activite = new Activite("emplois de temps","Attribuer les taches et envoyer les emplois de temps",new Date());

			admin.getActivites().add(activite);

			personnelService.AddPersonnel(admin);

		};
	}




}
