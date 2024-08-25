package app.bola.flywell.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import app.bola.flywell.data.model.persons.UserBioData;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "userbiodatas", collectionResourceRel = "userbiodatas", itemResourceRel = "userbiodata")
public interface UserBioDataRepository extends JpaRepository<UserBioData, String> {
	
	
	Optional<UserBioData> findByEmailAndPassword(String email, String password);
	
	boolean existsByEmail(String email);
	
	Optional<UserBioData> findByEmail(String userEmail);
}
