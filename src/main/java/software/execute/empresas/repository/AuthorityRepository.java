package software.execute.empresas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.execute.empresas.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
