package software.execute.empresas.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmpresaMapperTest {

    private EmpresaMapper empresaMapper;

    @BeforeEach
    public void setUp() {
        empresaMapper = new EmpresaMapperImpl();
    }
}
