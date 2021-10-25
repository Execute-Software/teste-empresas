package software.execute.empresas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import software.execute.empresas.domain.Endereco;

/**
 * Spring Data SQL repository for the Endereco entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>, JpaSpecificationExecutor<Endereco> {}
