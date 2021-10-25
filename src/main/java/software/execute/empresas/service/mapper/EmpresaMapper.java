package software.execute.empresas.service.mapper;

import org.mapstruct.*;
import software.execute.empresas.domain.Empresa;
import software.execute.empresas.service.dto.EmpresaDTO;

/**
 * Mapper for the entity {@link Empresa} and its DTO {@link EmpresaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmpresaMapper extends EntityMapper<EmpresaDTO, Empresa> {
    @Named("nome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EmpresaDTO toDtoNome(Empresa empresa);
}
