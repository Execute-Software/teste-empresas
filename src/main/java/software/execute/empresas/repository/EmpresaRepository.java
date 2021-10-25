package software.execute.empresas.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import software.execute.empresas.domain.Empresa;

/**
 * Spring Data SQL repository for the Empresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {}
