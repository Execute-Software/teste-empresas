package software.execute.empresas.service.mapper;

import org.mapstruct.*;
import software.execute.empresas.domain.Endereco;
import software.execute.empresas.service.dto.EnderecoDTO;

/**
 * Mapper for the entity {@link Endereco} and its DTO {@link EnderecoDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmpresaMapper.class })
public interface EnderecoMapper extends EntityMapper<EnderecoDTO, Endereco> {
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "nome")
    EnderecoDTO toDto(Endereco s);
}
