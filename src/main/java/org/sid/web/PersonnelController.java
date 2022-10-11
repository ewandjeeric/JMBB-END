package org.sid.web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sid.bean.RefreshToken;
import org.sid.entities.Personnel;
import org.sid.security.SecurityConstants;
import org.sid.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PersonnelController {
	@Autowired
	private PersonnelService personnelservice;
	
	@GetMapping("/personnels")
	@PostAuthorize("hasAuthority('ALL_PERSONNELS')")
	public Page<Personnel> AllPersonnels(
			@RequestParam(name ="nom", defaultValue="") String nom, 
			@RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size){
		
		return personnelservice.AllPersonnels(nom, size, page);
	}

	@GetMapping("/personnels/{id}")
	@PostAuthorize("hasAuthority('GET_PERSONNEL')")
	public Personnel GetPersonnels(@PathVariable(name = "id") Long id){
		return personnelservice.getPersonnel(id);
	}
	
	@PostMapping("/personnels")
	@PostAuthorize("hasAuthority('ADD_PERSONNEL')")
	public Personnel AddUpdate(@Valid @RequestBody Personnel personnel) {
		return personnelservice.AddPersonnel(personnel);
	}
	
	
	@DeleteMapping("/personnels/{id}")
	@PostAuthorize("hasAuthority('DELETE_PERSONNEL')")
	void DeletePersonnel(@PathVariable(name = "id") Long id) {
		personnelservice.DeletePersonnel(id);
	}

	@PostMapping("/resetPassword")
	void ResetPassword(@RequestParam(name = "personnel")Long id,
					   @RequestParam(name = "newpassword")String password){
		personnelservice.ResetPassword(id,password);
	}

	@PostMapping("/refreshToken")
	public void refreshToken(@RequestBody RefreshToken token, HttpServletRequest request,
							 HttpServletResponse response) {
		try {

			String jwt = token.getRefreshtoken();
			Algorithm algorithm = Algorithm.HMAC512(SecurityConstants.SECRET);
			JWTVerifier jwtverifier = JWT.require(algorithm).build();
			DecodedJWT decodejwt = jwtverifier.verify(jwt);
			String username = decodejwt.getSubject();

			Personnel springUser = personnelservice.findByUsername(username);
			System.out.println(springUser.getUsername());

			String JwtaccessToken = JWT.create().withSubject(springUser.getUsername())
					.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
					.withIssuer(request.getRequestURL().toString())
					.withClaim("roles",
							springUser.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList()))
					.sign(Algorithm.HMAC512(SecurityConstants.SECRET));
			response.setHeader(SecurityConstants.HEADER_STRING, JwtaccessToken);

			Map<String, String> idToken = new HashMap<String, String>();
			idToken.put("Acces-Token", SecurityConstants.TOKEN_PREFIX + JwtaccessToken);
			idToken.put("Refresh-Token", jwt);
			response.setContentType("application/json");
			new ObjectMapper().writeValue(response.getOutputStream(), idToken);

		} catch (Exception e) {
			response.setHeader("error-message", e.getMessage());

		}

	}




}
