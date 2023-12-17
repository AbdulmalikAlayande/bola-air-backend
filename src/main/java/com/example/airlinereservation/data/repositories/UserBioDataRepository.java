package com.example.airlinereservation.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.airlinereservation.data.model.persons.UserBioData;

import java.util.Optional;

public interface UserBioDataRepository extends JpaRepository<UserBioData, String> {
	
	void deleteByUserName(String userName);
	Optional<UserBioData> findByUserName(String userName);
	
	Optional<UserBioData> findByEmailAndPassword(String email, String password);
	
	boolean existsByEmail(String email);
	
	Optional<UserBioData> findByEmail(String userEmail);
}
