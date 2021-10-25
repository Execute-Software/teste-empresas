package software.execute.empresas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static software.execute.empresas.web.rest.TestUtil.sameInstant;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import software.execute.empresas.IntegrationTest;
import software.execute.empresas.domain.Empresa;
import software.execute.empresas.repository.EmpresaRepository;
import software.execute.empresas.service.criteria.EmpresaCriteria;
import software.execute.empresas.service.dto.EmpresaDTO;
import software.execute.empresas.service.mapper.EmpresaMapper;

/**
 * Integration tests for the {@link EmpresaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpresaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SOBRE = "AAAAAAAAAA";
    private static final String UPDATED_SOBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_RAZAO_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZAO_SOCIAL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_CONSTITUICAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CONSTITUICAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_CONSTITUICAO = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_LINK_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_LINK_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_LINKEDIN = "AAAAAAAAAA";
    private static final String UPDATED_LINK_LINKEDIN = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_LINK_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_SITE = "AAAAAAAAAA";
    private static final String UPDATED_SITE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final ZonedDateTime DEFAULT_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CRIADO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CRIADO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/empresas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpresaMockMvc;

    private Empresa empresa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .nome(DEFAULT_NOME)
            .sobre(DEFAULT_SOBRE)
            .cnpj(DEFAULT_CNPJ)
            .razaoSocial(DEFAULT_RAZAO_SOCIAL)
            .dataConstituicao(DEFAULT_DATA_CONSTITUICAO)
            .logoUrl(DEFAULT_LOGO_URL)
            .linkFacebook(DEFAULT_LINK_FACEBOOK)
            .linkInstagram(DEFAULT_LINK_INSTAGRAM)
            .linkLinkedin(DEFAULT_LINK_LINKEDIN)
            .linkTwitter(DEFAULT_LINK_TWITTER)
            .site(DEFAULT_SITE)
            .ativo(DEFAULT_ATIVO)
            .criado(DEFAULT_CRIADO);
        return empresa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empresa createUpdatedEntity(EntityManager em) {
        Empresa empresa = new Empresa()
            .nome(UPDATED_NOME)
            .sobre(UPDATED_SOBRE)
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .dataConstituicao(UPDATED_DATA_CONSTITUICAO)
            .logoUrl(UPDATED_LOGO_URL)
            .linkFacebook(UPDATED_LINK_FACEBOOK)
            .linkInstagram(UPDATED_LINK_INSTAGRAM)
            .linkLinkedin(UPDATED_LINK_LINKEDIN)
            .linkTwitter(UPDATED_LINK_TWITTER)
            .site(UPDATED_SITE)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO);
        return empresa;
    }

    @BeforeEach
    public void initTest() {
        empresa = createEntity(em);
    }

    @Test
    @Transactional
    void createEmpresa() throws Exception {
        int databaseSizeBeforeCreate = empresaRepository.findAll().size();
        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);
        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresaDTO)))
            .andExpect(status().isCreated());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate + 1);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEmpresa.getSobre()).isEqualTo(DEFAULT_SOBRE);
        assertThat(testEmpresa.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testEmpresa.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testEmpresa.getDataConstituicao()).isEqualTo(DEFAULT_DATA_CONSTITUICAO);
        assertThat(testEmpresa.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testEmpresa.getLinkFacebook()).isEqualTo(DEFAULT_LINK_FACEBOOK);
        assertThat(testEmpresa.getLinkInstagram()).isEqualTo(DEFAULT_LINK_INSTAGRAM);
        assertThat(testEmpresa.getLinkLinkedin()).isEqualTo(DEFAULT_LINK_LINKEDIN);
        assertThat(testEmpresa.getLinkTwitter()).isEqualTo(DEFAULT_LINK_TWITTER);
        assertThat(testEmpresa.getSite()).isEqualTo(DEFAULT_SITE);
        assertThat(testEmpresa.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testEmpresa.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void createEmpresaWithExistingId() throws Exception {
        // Create the Empresa with an existing ID
        empresa.setId(1L);
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        int databaseSizeBeforeCreate = empresaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = empresaRepository.findAll().size();
        // set the field null
        empresa.setNome(null);

        // Create the Empresa, which fails.
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresaDTO)))
            .andExpect(status().isBadRequest());

        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCriadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = empresaRepository.findAll().size();
        // set the field null
        empresa.setCriado(null);

        // Create the Empresa, which fails.
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        restEmpresaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresaDTO)))
            .andExpect(status().isBadRequest());

        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmpresas() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sobre").value(hasItem(DEFAULT_SOBRE.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].dataConstituicao").value(hasItem(DEFAULT_DATA_CONSTITUICAO.toString())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].linkFacebook").value(hasItem(DEFAULT_LINK_FACEBOOK)))
            .andExpect(jsonPath("$.[*].linkInstagram").value(hasItem(DEFAULT_LINK_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].linkLinkedin").value(hasItem(DEFAULT_LINK_LINKEDIN)))
            .andExpect(jsonPath("$.[*].linkTwitter").value(hasItem(DEFAULT_LINK_TWITTER)))
            .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));
    }

    @Test
    @Transactional
    void getEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get the empresa
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL_ID, empresa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empresa.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.sobre").value(DEFAULT_SOBRE.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL))
            .andExpect(jsonPath("$.dataConstituicao").value(DEFAULT_DATA_CONSTITUICAO.toString()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.linkFacebook").value(DEFAULT_LINK_FACEBOOK))
            .andExpect(jsonPath("$.linkInstagram").value(DEFAULT_LINK_INSTAGRAM))
            .andExpect(jsonPath("$.linkLinkedin").value(DEFAULT_LINK_LINKEDIN))
            .andExpect(jsonPath("$.linkTwitter").value(DEFAULT_LINK_TWITTER))
            .andExpect(jsonPath("$.site").value(DEFAULT_SITE))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.criado").value(sameInstant(DEFAULT_CRIADO)));
    }

    @Test
    @Transactional
    void getEmpresasByIdFiltering() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        Long id = empresa.getId();

        defaultEmpresaShouldBeFound("id.equals=" + id);
        defaultEmpresaShouldNotBeFound("id.notEquals=" + id);

        defaultEmpresaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmpresaShouldNotBeFound("id.greaterThan=" + id);

        defaultEmpresaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmpresaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmpresasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nome equals to DEFAULT_NOME
        defaultEmpresaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the empresaList where nome equals to UPDATED_NOME
        defaultEmpresaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmpresasByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nome not equals to DEFAULT_NOME
        defaultEmpresaShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the empresaList where nome not equals to UPDATED_NOME
        defaultEmpresaShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmpresasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultEmpresaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the empresaList where nome equals to UPDATED_NOME
        defaultEmpresaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmpresasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nome is not null
        defaultEmpresaShouldBeFound("nome.specified=true");

        // Get all the empresaList where nome is null
        defaultEmpresaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByNomeContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nome contains DEFAULT_NOME
        defaultEmpresaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the empresaList where nome contains UPDATED_NOME
        defaultEmpresaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmpresasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where nome does not contain DEFAULT_NOME
        defaultEmpresaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the empresaList where nome does not contain UPDATED_NOME
        defaultEmpresaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEmpresasByCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where cnpj equals to DEFAULT_CNPJ
        defaultEmpresaShouldBeFound("cnpj.equals=" + DEFAULT_CNPJ);

        // Get all the empresaList where cnpj equals to UPDATED_CNPJ
        defaultEmpresaShouldNotBeFound("cnpj.equals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmpresasByCnpjIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where cnpj not equals to DEFAULT_CNPJ
        defaultEmpresaShouldNotBeFound("cnpj.notEquals=" + DEFAULT_CNPJ);

        // Get all the empresaList where cnpj not equals to UPDATED_CNPJ
        defaultEmpresaShouldBeFound("cnpj.notEquals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmpresasByCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where cnpj in DEFAULT_CNPJ or UPDATED_CNPJ
        defaultEmpresaShouldBeFound("cnpj.in=" + DEFAULT_CNPJ + "," + UPDATED_CNPJ);

        // Get all the empresaList where cnpj equals to UPDATED_CNPJ
        defaultEmpresaShouldNotBeFound("cnpj.in=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmpresasByCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where cnpj is not null
        defaultEmpresaShouldBeFound("cnpj.specified=true");

        // Get all the empresaList where cnpj is null
        defaultEmpresaShouldNotBeFound("cnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByCnpjContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where cnpj contains DEFAULT_CNPJ
        defaultEmpresaShouldBeFound("cnpj.contains=" + DEFAULT_CNPJ);

        // Get all the empresaList where cnpj contains UPDATED_CNPJ
        defaultEmpresaShouldNotBeFound("cnpj.contains=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmpresasByCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where cnpj does not contain DEFAULT_CNPJ
        defaultEmpresaShouldNotBeFound("cnpj.doesNotContain=" + DEFAULT_CNPJ);

        // Get all the empresaList where cnpj does not contain UPDATED_CNPJ
        defaultEmpresaShouldBeFound("cnpj.doesNotContain=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllEmpresasByRazaoSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where razaoSocial equals to DEFAULT_RAZAO_SOCIAL
        defaultEmpresaShouldBeFound("razaoSocial.equals=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the empresaList where razaoSocial equals to UPDATED_RAZAO_SOCIAL
        defaultEmpresaShouldNotBeFound("razaoSocial.equals=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmpresasByRazaoSocialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where razaoSocial not equals to DEFAULT_RAZAO_SOCIAL
        defaultEmpresaShouldNotBeFound("razaoSocial.notEquals=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the empresaList where razaoSocial not equals to UPDATED_RAZAO_SOCIAL
        defaultEmpresaShouldBeFound("razaoSocial.notEquals=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmpresasByRazaoSocialIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where razaoSocial in DEFAULT_RAZAO_SOCIAL or UPDATED_RAZAO_SOCIAL
        defaultEmpresaShouldBeFound("razaoSocial.in=" + DEFAULT_RAZAO_SOCIAL + "," + UPDATED_RAZAO_SOCIAL);

        // Get all the empresaList where razaoSocial equals to UPDATED_RAZAO_SOCIAL
        defaultEmpresaShouldNotBeFound("razaoSocial.in=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmpresasByRazaoSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where razaoSocial is not null
        defaultEmpresaShouldBeFound("razaoSocial.specified=true");

        // Get all the empresaList where razaoSocial is null
        defaultEmpresaShouldNotBeFound("razaoSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByRazaoSocialContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where razaoSocial contains DEFAULT_RAZAO_SOCIAL
        defaultEmpresaShouldBeFound("razaoSocial.contains=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the empresaList where razaoSocial contains UPDATED_RAZAO_SOCIAL
        defaultEmpresaShouldNotBeFound("razaoSocial.contains=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmpresasByRazaoSocialNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where razaoSocial does not contain DEFAULT_RAZAO_SOCIAL
        defaultEmpresaShouldNotBeFound("razaoSocial.doesNotContain=" + DEFAULT_RAZAO_SOCIAL);

        // Get all the empresaList where razaoSocial does not contain UPDATED_RAZAO_SOCIAL
        defaultEmpresaShouldBeFound("razaoSocial.doesNotContain=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao equals to DEFAULT_DATA_CONSTITUICAO
        defaultEmpresaShouldBeFound("dataConstituicao.equals=" + DEFAULT_DATA_CONSTITUICAO);

        // Get all the empresaList where dataConstituicao equals to UPDATED_DATA_CONSTITUICAO
        defaultEmpresaShouldNotBeFound("dataConstituicao.equals=" + UPDATED_DATA_CONSTITUICAO);
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao not equals to DEFAULT_DATA_CONSTITUICAO
        defaultEmpresaShouldNotBeFound("dataConstituicao.notEquals=" + DEFAULT_DATA_CONSTITUICAO);

        // Get all the empresaList where dataConstituicao not equals to UPDATED_DATA_CONSTITUICAO
        defaultEmpresaShouldBeFound("dataConstituicao.notEquals=" + UPDATED_DATA_CONSTITUICAO);
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao in DEFAULT_DATA_CONSTITUICAO or UPDATED_DATA_CONSTITUICAO
        defaultEmpresaShouldBeFound("dataConstituicao.in=" + DEFAULT_DATA_CONSTITUICAO + "," + UPDATED_DATA_CONSTITUICAO);

        // Get all the empresaList where dataConstituicao equals to UPDATED_DATA_CONSTITUICAO
        defaultEmpresaShouldNotBeFound("dataConstituicao.in=" + UPDATED_DATA_CONSTITUICAO);
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao is not null
        defaultEmpresaShouldBeFound("dataConstituicao.specified=true");

        // Get all the empresaList where dataConstituicao is null
        defaultEmpresaShouldNotBeFound("dataConstituicao.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao is greater than or equal to DEFAULT_DATA_CONSTITUICAO
        defaultEmpresaShouldBeFound("dataConstituicao.greaterThanOrEqual=" + DEFAULT_DATA_CONSTITUICAO);

        // Get all the empresaList where dataConstituicao is greater than or equal to UPDATED_DATA_CONSTITUICAO
        defaultEmpresaShouldNotBeFound("dataConstituicao.greaterThanOrEqual=" + UPDATED_DATA_CONSTITUICAO);
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao is less than or equal to DEFAULT_DATA_CONSTITUICAO
        defaultEmpresaShouldBeFound("dataConstituicao.lessThanOrEqual=" + DEFAULT_DATA_CONSTITUICAO);

        // Get all the empresaList where dataConstituicao is less than or equal to SMALLER_DATA_CONSTITUICAO
        defaultEmpresaShouldNotBeFound("dataConstituicao.lessThanOrEqual=" + SMALLER_DATA_CONSTITUICAO);
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsLessThanSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao is less than DEFAULT_DATA_CONSTITUICAO
        defaultEmpresaShouldNotBeFound("dataConstituicao.lessThan=" + DEFAULT_DATA_CONSTITUICAO);

        // Get all the empresaList where dataConstituicao is less than UPDATED_DATA_CONSTITUICAO
        defaultEmpresaShouldBeFound("dataConstituicao.lessThan=" + UPDATED_DATA_CONSTITUICAO);
    }

    @Test
    @Transactional
    void getAllEmpresasByDataConstituicaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where dataConstituicao is greater than DEFAULT_DATA_CONSTITUICAO
        defaultEmpresaShouldNotBeFound("dataConstituicao.greaterThan=" + DEFAULT_DATA_CONSTITUICAO);

        // Get all the empresaList where dataConstituicao is greater than SMALLER_DATA_CONSTITUICAO
        defaultEmpresaShouldBeFound("dataConstituicao.greaterThan=" + SMALLER_DATA_CONSTITUICAO);
    }

    @Test
    @Transactional
    void getAllEmpresasByLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where logoUrl equals to DEFAULT_LOGO_URL
        defaultEmpresaShouldBeFound("logoUrl.equals=" + DEFAULT_LOGO_URL);

        // Get all the empresaList where logoUrl equals to UPDATED_LOGO_URL
        defaultEmpresaShouldNotBeFound("logoUrl.equals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllEmpresasByLogoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where logoUrl not equals to DEFAULT_LOGO_URL
        defaultEmpresaShouldNotBeFound("logoUrl.notEquals=" + DEFAULT_LOGO_URL);

        // Get all the empresaList where logoUrl not equals to UPDATED_LOGO_URL
        defaultEmpresaShouldBeFound("logoUrl.notEquals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllEmpresasByLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where logoUrl in DEFAULT_LOGO_URL or UPDATED_LOGO_URL
        defaultEmpresaShouldBeFound("logoUrl.in=" + DEFAULT_LOGO_URL + "," + UPDATED_LOGO_URL);

        // Get all the empresaList where logoUrl equals to UPDATED_LOGO_URL
        defaultEmpresaShouldNotBeFound("logoUrl.in=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllEmpresasByLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where logoUrl is not null
        defaultEmpresaShouldBeFound("logoUrl.specified=true");

        // Get all the empresaList where logoUrl is null
        defaultEmpresaShouldNotBeFound("logoUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByLogoUrlContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where logoUrl contains DEFAULT_LOGO_URL
        defaultEmpresaShouldBeFound("logoUrl.contains=" + DEFAULT_LOGO_URL);

        // Get all the empresaList where logoUrl contains UPDATED_LOGO_URL
        defaultEmpresaShouldNotBeFound("logoUrl.contains=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllEmpresasByLogoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where logoUrl does not contain DEFAULT_LOGO_URL
        defaultEmpresaShouldNotBeFound("logoUrl.doesNotContain=" + DEFAULT_LOGO_URL);

        // Get all the empresaList where logoUrl does not contain UPDATED_LOGO_URL
        defaultEmpresaShouldBeFound("logoUrl.doesNotContain=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkFacebookIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkFacebook equals to DEFAULT_LINK_FACEBOOK
        defaultEmpresaShouldBeFound("linkFacebook.equals=" + DEFAULT_LINK_FACEBOOK);

        // Get all the empresaList where linkFacebook equals to UPDATED_LINK_FACEBOOK
        defaultEmpresaShouldNotBeFound("linkFacebook.equals=" + UPDATED_LINK_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkFacebookIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkFacebook not equals to DEFAULT_LINK_FACEBOOK
        defaultEmpresaShouldNotBeFound("linkFacebook.notEquals=" + DEFAULT_LINK_FACEBOOK);

        // Get all the empresaList where linkFacebook not equals to UPDATED_LINK_FACEBOOK
        defaultEmpresaShouldBeFound("linkFacebook.notEquals=" + UPDATED_LINK_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkFacebookIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkFacebook in DEFAULT_LINK_FACEBOOK or UPDATED_LINK_FACEBOOK
        defaultEmpresaShouldBeFound("linkFacebook.in=" + DEFAULT_LINK_FACEBOOK + "," + UPDATED_LINK_FACEBOOK);

        // Get all the empresaList where linkFacebook equals to UPDATED_LINK_FACEBOOK
        defaultEmpresaShouldNotBeFound("linkFacebook.in=" + UPDATED_LINK_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkFacebookIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkFacebook is not null
        defaultEmpresaShouldBeFound("linkFacebook.specified=true");

        // Get all the empresaList where linkFacebook is null
        defaultEmpresaShouldNotBeFound("linkFacebook.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkFacebookContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkFacebook contains DEFAULT_LINK_FACEBOOK
        defaultEmpresaShouldBeFound("linkFacebook.contains=" + DEFAULT_LINK_FACEBOOK);

        // Get all the empresaList where linkFacebook contains UPDATED_LINK_FACEBOOK
        defaultEmpresaShouldNotBeFound("linkFacebook.contains=" + UPDATED_LINK_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkFacebookNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkFacebook does not contain DEFAULT_LINK_FACEBOOK
        defaultEmpresaShouldNotBeFound("linkFacebook.doesNotContain=" + DEFAULT_LINK_FACEBOOK);

        // Get all the empresaList where linkFacebook does not contain UPDATED_LINK_FACEBOOK
        defaultEmpresaShouldBeFound("linkFacebook.doesNotContain=" + UPDATED_LINK_FACEBOOK);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkInstagramIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkInstagram equals to DEFAULT_LINK_INSTAGRAM
        defaultEmpresaShouldBeFound("linkInstagram.equals=" + DEFAULT_LINK_INSTAGRAM);

        // Get all the empresaList where linkInstagram equals to UPDATED_LINK_INSTAGRAM
        defaultEmpresaShouldNotBeFound("linkInstagram.equals=" + UPDATED_LINK_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkInstagramIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkInstagram not equals to DEFAULT_LINK_INSTAGRAM
        defaultEmpresaShouldNotBeFound("linkInstagram.notEquals=" + DEFAULT_LINK_INSTAGRAM);

        // Get all the empresaList where linkInstagram not equals to UPDATED_LINK_INSTAGRAM
        defaultEmpresaShouldBeFound("linkInstagram.notEquals=" + UPDATED_LINK_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkInstagramIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkInstagram in DEFAULT_LINK_INSTAGRAM or UPDATED_LINK_INSTAGRAM
        defaultEmpresaShouldBeFound("linkInstagram.in=" + DEFAULT_LINK_INSTAGRAM + "," + UPDATED_LINK_INSTAGRAM);

        // Get all the empresaList where linkInstagram equals to UPDATED_LINK_INSTAGRAM
        defaultEmpresaShouldNotBeFound("linkInstagram.in=" + UPDATED_LINK_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkInstagramIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkInstagram is not null
        defaultEmpresaShouldBeFound("linkInstagram.specified=true");

        // Get all the empresaList where linkInstagram is null
        defaultEmpresaShouldNotBeFound("linkInstagram.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkInstagramContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkInstagram contains DEFAULT_LINK_INSTAGRAM
        defaultEmpresaShouldBeFound("linkInstagram.contains=" + DEFAULT_LINK_INSTAGRAM);

        // Get all the empresaList where linkInstagram contains UPDATED_LINK_INSTAGRAM
        defaultEmpresaShouldNotBeFound("linkInstagram.contains=" + UPDATED_LINK_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkInstagramNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkInstagram does not contain DEFAULT_LINK_INSTAGRAM
        defaultEmpresaShouldNotBeFound("linkInstagram.doesNotContain=" + DEFAULT_LINK_INSTAGRAM);

        // Get all the empresaList where linkInstagram does not contain UPDATED_LINK_INSTAGRAM
        defaultEmpresaShouldBeFound("linkInstagram.doesNotContain=" + UPDATED_LINK_INSTAGRAM);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkLinkedinIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkLinkedin equals to DEFAULT_LINK_LINKEDIN
        defaultEmpresaShouldBeFound("linkLinkedin.equals=" + DEFAULT_LINK_LINKEDIN);

        // Get all the empresaList where linkLinkedin equals to UPDATED_LINK_LINKEDIN
        defaultEmpresaShouldNotBeFound("linkLinkedin.equals=" + UPDATED_LINK_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkLinkedinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkLinkedin not equals to DEFAULT_LINK_LINKEDIN
        defaultEmpresaShouldNotBeFound("linkLinkedin.notEquals=" + DEFAULT_LINK_LINKEDIN);

        // Get all the empresaList where linkLinkedin not equals to UPDATED_LINK_LINKEDIN
        defaultEmpresaShouldBeFound("linkLinkedin.notEquals=" + UPDATED_LINK_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkLinkedinIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkLinkedin in DEFAULT_LINK_LINKEDIN or UPDATED_LINK_LINKEDIN
        defaultEmpresaShouldBeFound("linkLinkedin.in=" + DEFAULT_LINK_LINKEDIN + "," + UPDATED_LINK_LINKEDIN);

        // Get all the empresaList where linkLinkedin equals to UPDATED_LINK_LINKEDIN
        defaultEmpresaShouldNotBeFound("linkLinkedin.in=" + UPDATED_LINK_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkLinkedinIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkLinkedin is not null
        defaultEmpresaShouldBeFound("linkLinkedin.specified=true");

        // Get all the empresaList where linkLinkedin is null
        defaultEmpresaShouldNotBeFound("linkLinkedin.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkLinkedinContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkLinkedin contains DEFAULT_LINK_LINKEDIN
        defaultEmpresaShouldBeFound("linkLinkedin.contains=" + DEFAULT_LINK_LINKEDIN);

        // Get all the empresaList where linkLinkedin contains UPDATED_LINK_LINKEDIN
        defaultEmpresaShouldNotBeFound("linkLinkedin.contains=" + UPDATED_LINK_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkLinkedinNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkLinkedin does not contain DEFAULT_LINK_LINKEDIN
        defaultEmpresaShouldNotBeFound("linkLinkedin.doesNotContain=" + DEFAULT_LINK_LINKEDIN);

        // Get all the empresaList where linkLinkedin does not contain UPDATED_LINK_LINKEDIN
        defaultEmpresaShouldBeFound("linkLinkedin.doesNotContain=" + UPDATED_LINK_LINKEDIN);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkTwitterIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkTwitter equals to DEFAULT_LINK_TWITTER
        defaultEmpresaShouldBeFound("linkTwitter.equals=" + DEFAULT_LINK_TWITTER);

        // Get all the empresaList where linkTwitter equals to UPDATED_LINK_TWITTER
        defaultEmpresaShouldNotBeFound("linkTwitter.equals=" + UPDATED_LINK_TWITTER);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkTwitterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkTwitter not equals to DEFAULT_LINK_TWITTER
        defaultEmpresaShouldNotBeFound("linkTwitter.notEquals=" + DEFAULT_LINK_TWITTER);

        // Get all the empresaList where linkTwitter not equals to UPDATED_LINK_TWITTER
        defaultEmpresaShouldBeFound("linkTwitter.notEquals=" + UPDATED_LINK_TWITTER);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkTwitterIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkTwitter in DEFAULT_LINK_TWITTER or UPDATED_LINK_TWITTER
        defaultEmpresaShouldBeFound("linkTwitter.in=" + DEFAULT_LINK_TWITTER + "," + UPDATED_LINK_TWITTER);

        // Get all the empresaList where linkTwitter equals to UPDATED_LINK_TWITTER
        defaultEmpresaShouldNotBeFound("linkTwitter.in=" + UPDATED_LINK_TWITTER);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkTwitterIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkTwitter is not null
        defaultEmpresaShouldBeFound("linkTwitter.specified=true");

        // Get all the empresaList where linkTwitter is null
        defaultEmpresaShouldNotBeFound("linkTwitter.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkTwitterContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkTwitter contains DEFAULT_LINK_TWITTER
        defaultEmpresaShouldBeFound("linkTwitter.contains=" + DEFAULT_LINK_TWITTER);

        // Get all the empresaList where linkTwitter contains UPDATED_LINK_TWITTER
        defaultEmpresaShouldNotBeFound("linkTwitter.contains=" + UPDATED_LINK_TWITTER);
    }

    @Test
    @Transactional
    void getAllEmpresasByLinkTwitterNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where linkTwitter does not contain DEFAULT_LINK_TWITTER
        defaultEmpresaShouldNotBeFound("linkTwitter.doesNotContain=" + DEFAULT_LINK_TWITTER);

        // Get all the empresaList where linkTwitter does not contain UPDATED_LINK_TWITTER
        defaultEmpresaShouldBeFound("linkTwitter.doesNotContain=" + UPDATED_LINK_TWITTER);
    }

    @Test
    @Transactional
    void getAllEmpresasBySiteIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where site equals to DEFAULT_SITE
        defaultEmpresaShouldBeFound("site.equals=" + DEFAULT_SITE);

        // Get all the empresaList where site equals to UPDATED_SITE
        defaultEmpresaShouldNotBeFound("site.equals=" + UPDATED_SITE);
    }

    @Test
    @Transactional
    void getAllEmpresasBySiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where site not equals to DEFAULT_SITE
        defaultEmpresaShouldNotBeFound("site.notEquals=" + DEFAULT_SITE);

        // Get all the empresaList where site not equals to UPDATED_SITE
        defaultEmpresaShouldBeFound("site.notEquals=" + UPDATED_SITE);
    }

    @Test
    @Transactional
    void getAllEmpresasBySiteIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where site in DEFAULT_SITE or UPDATED_SITE
        defaultEmpresaShouldBeFound("site.in=" + DEFAULT_SITE + "," + UPDATED_SITE);

        // Get all the empresaList where site equals to UPDATED_SITE
        defaultEmpresaShouldNotBeFound("site.in=" + UPDATED_SITE);
    }

    @Test
    @Transactional
    void getAllEmpresasBySiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where site is not null
        defaultEmpresaShouldBeFound("site.specified=true");

        // Get all the empresaList where site is null
        defaultEmpresaShouldNotBeFound("site.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasBySiteContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where site contains DEFAULT_SITE
        defaultEmpresaShouldBeFound("site.contains=" + DEFAULT_SITE);

        // Get all the empresaList where site contains UPDATED_SITE
        defaultEmpresaShouldNotBeFound("site.contains=" + UPDATED_SITE);
    }

    @Test
    @Transactional
    void getAllEmpresasBySiteNotContainsSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where site does not contain DEFAULT_SITE
        defaultEmpresaShouldNotBeFound("site.doesNotContain=" + DEFAULT_SITE);

        // Get all the empresaList where site does not contain UPDATED_SITE
        defaultEmpresaShouldBeFound("site.doesNotContain=" + UPDATED_SITE);
    }

    @Test
    @Transactional
    void getAllEmpresasByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where ativo equals to DEFAULT_ATIVO
        defaultEmpresaShouldBeFound("ativo.equals=" + DEFAULT_ATIVO);

        // Get all the empresaList where ativo equals to UPDATED_ATIVO
        defaultEmpresaShouldNotBeFound("ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByAtivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where ativo not equals to DEFAULT_ATIVO
        defaultEmpresaShouldNotBeFound("ativo.notEquals=" + DEFAULT_ATIVO);

        // Get all the empresaList where ativo not equals to UPDATED_ATIVO
        defaultEmpresaShouldBeFound("ativo.notEquals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where ativo in DEFAULT_ATIVO or UPDATED_ATIVO
        defaultEmpresaShouldBeFound("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO);

        // Get all the empresaList where ativo equals to UPDATED_ATIVO
        defaultEmpresaShouldNotBeFound("ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllEmpresasByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where ativo is not null
        defaultEmpresaShouldBeFound("ativo.specified=true");

        // Get all the empresaList where ativo is null
        defaultEmpresaShouldNotBeFound("ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado equals to DEFAULT_CRIADO
        defaultEmpresaShouldBeFound("criado.equals=" + DEFAULT_CRIADO);

        // Get all the empresaList where criado equals to UPDATED_CRIADO
        defaultEmpresaShouldNotBeFound("criado.equals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado not equals to DEFAULT_CRIADO
        defaultEmpresaShouldNotBeFound("criado.notEquals=" + DEFAULT_CRIADO);

        // Get all the empresaList where criado not equals to UPDATED_CRIADO
        defaultEmpresaShouldBeFound("criado.notEquals=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsInShouldWork() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado in DEFAULT_CRIADO or UPDATED_CRIADO
        defaultEmpresaShouldBeFound("criado.in=" + DEFAULT_CRIADO + "," + UPDATED_CRIADO);

        // Get all the empresaList where criado equals to UPDATED_CRIADO
        defaultEmpresaShouldNotBeFound("criado.in=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado is not null
        defaultEmpresaShouldBeFound("criado.specified=true");

        // Get all the empresaList where criado is null
        defaultEmpresaShouldNotBeFound("criado.specified=false");
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado is greater than or equal to DEFAULT_CRIADO
        defaultEmpresaShouldBeFound("criado.greaterThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the empresaList where criado is greater than or equal to UPDATED_CRIADO
        defaultEmpresaShouldNotBeFound("criado.greaterThanOrEqual=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado is less than or equal to DEFAULT_CRIADO
        defaultEmpresaShouldBeFound("criado.lessThanOrEqual=" + DEFAULT_CRIADO);

        // Get all the empresaList where criado is less than or equal to SMALLER_CRIADO
        defaultEmpresaShouldNotBeFound("criado.lessThanOrEqual=" + SMALLER_CRIADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsLessThanSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado is less than DEFAULT_CRIADO
        defaultEmpresaShouldNotBeFound("criado.lessThan=" + DEFAULT_CRIADO);

        // Get all the empresaList where criado is less than UPDATED_CRIADO
        defaultEmpresaShouldBeFound("criado.lessThan=" + UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void getAllEmpresasByCriadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        // Get all the empresaList where criado is greater than DEFAULT_CRIADO
        defaultEmpresaShouldNotBeFound("criado.greaterThan=" + DEFAULT_CRIADO);

        // Get all the empresaList where criado is greater than SMALLER_CRIADO
        defaultEmpresaShouldBeFound("criado.greaterThan=" + SMALLER_CRIADO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmpresaShouldBeFound(String filter) throws Exception {
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empresa.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].sobre").value(hasItem(DEFAULT_SOBRE.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].dataConstituicao").value(hasItem(DEFAULT_DATA_CONSTITUICAO.toString())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].linkFacebook").value(hasItem(DEFAULT_LINK_FACEBOOK)))
            .andExpect(jsonPath("$.[*].linkInstagram").value(hasItem(DEFAULT_LINK_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].linkLinkedin").value(hasItem(DEFAULT_LINK_LINKEDIN)))
            .andExpect(jsonPath("$.[*].linkTwitter").value(hasItem(DEFAULT_LINK_TWITTER)))
            .andExpect(jsonPath("$.[*].site").value(hasItem(DEFAULT_SITE)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].criado").value(hasItem(sameInstant(DEFAULT_CRIADO))));

        // Check, that the count call also returns 1
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmpresaShouldNotBeFound(String filter) throws Exception {
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmpresaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmpresa() throws Exception {
        // Get the empresa
        restEmpresaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa
        Empresa updatedEmpresa = empresaRepository.findById(empresa.getId()).get();
        // Disconnect from session so that the updates on updatedEmpresa are not directly saved in db
        em.detach(updatedEmpresa);
        updatedEmpresa
            .nome(UPDATED_NOME)
            .sobre(UPDATED_SOBRE)
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .dataConstituicao(UPDATED_DATA_CONSTITUICAO)
            .logoUrl(UPDATED_LOGO_URL)
            .linkFacebook(UPDATED_LINK_FACEBOOK)
            .linkInstagram(UPDATED_LINK_INSTAGRAM)
            .linkLinkedin(UPDATED_LINK_LINKEDIN)
            .linkTwitter(UPDATED_LINK_TWITTER)
            .site(UPDATED_SITE)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO);
        EmpresaDTO empresaDTO = empresaMapper.toDto(updatedEmpresa);

        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empresaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empresaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmpresa.getSobre()).isEqualTo(UPDATED_SOBRE);
        assertThat(testEmpresa.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testEmpresa.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testEmpresa.getDataConstituicao()).isEqualTo(UPDATED_DATA_CONSTITUICAO);
        assertThat(testEmpresa.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testEmpresa.getLinkFacebook()).isEqualTo(UPDATED_LINK_FACEBOOK);
        assertThat(testEmpresa.getLinkInstagram()).isEqualTo(UPDATED_LINK_INSTAGRAM);
        assertThat(testEmpresa.getLinkLinkedin()).isEqualTo(UPDATED_LINK_LINKEDIN);
        assertThat(testEmpresa.getLinkTwitter()).isEqualTo(UPDATED_LINK_TWITTER);
        assertThat(testEmpresa.getSite()).isEqualTo(UPDATED_SITE);
        assertThat(testEmpresa.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testEmpresa.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void putNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empresaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empresaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa
            .nome(UPDATED_NOME)
            .sobre(UPDATED_SOBRE)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .logoUrl(UPDATED_LOGO_URL)
            .linkInstagram(UPDATED_LINK_INSTAGRAM)
            .linkLinkedin(UPDATED_LINK_LINKEDIN);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmpresa.getSobre()).isEqualTo(UPDATED_SOBRE);
        assertThat(testEmpresa.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testEmpresa.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testEmpresa.getDataConstituicao()).isEqualTo(DEFAULT_DATA_CONSTITUICAO);
        assertThat(testEmpresa.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testEmpresa.getLinkFacebook()).isEqualTo(DEFAULT_LINK_FACEBOOK);
        assertThat(testEmpresa.getLinkInstagram()).isEqualTo(UPDATED_LINK_INSTAGRAM);
        assertThat(testEmpresa.getLinkLinkedin()).isEqualTo(UPDATED_LINK_LINKEDIN);
        assertThat(testEmpresa.getLinkTwitter()).isEqualTo(DEFAULT_LINK_TWITTER);
        assertThat(testEmpresa.getSite()).isEqualTo(DEFAULT_SITE);
        assertThat(testEmpresa.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testEmpresa.getCriado()).isEqualTo(DEFAULT_CRIADO);
    }

    @Test
    @Transactional
    void fullUpdateEmpresaWithPatch() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();

        // Update the empresa using partial update
        Empresa partialUpdatedEmpresa = new Empresa();
        partialUpdatedEmpresa.setId(empresa.getId());

        partialUpdatedEmpresa
            .nome(UPDATED_NOME)
            .sobre(UPDATED_SOBRE)
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .dataConstituicao(UPDATED_DATA_CONSTITUICAO)
            .logoUrl(UPDATED_LOGO_URL)
            .linkFacebook(UPDATED_LINK_FACEBOOK)
            .linkInstagram(UPDATED_LINK_INSTAGRAM)
            .linkLinkedin(UPDATED_LINK_LINKEDIN)
            .linkTwitter(UPDATED_LINK_TWITTER)
            .site(UPDATED_SITE)
            .ativo(UPDATED_ATIVO)
            .criado(UPDATED_CRIADO);

        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpresa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmpresa))
            )
            .andExpect(status().isOk());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
        Empresa testEmpresa = empresaList.get(empresaList.size() - 1);
        assertThat(testEmpresa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEmpresa.getSobre()).isEqualTo(UPDATED_SOBRE);
        assertThat(testEmpresa.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testEmpresa.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testEmpresa.getDataConstituicao()).isEqualTo(UPDATED_DATA_CONSTITUICAO);
        assertThat(testEmpresa.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testEmpresa.getLinkFacebook()).isEqualTo(UPDATED_LINK_FACEBOOK);
        assertThat(testEmpresa.getLinkInstagram()).isEqualTo(UPDATED_LINK_INSTAGRAM);
        assertThat(testEmpresa.getLinkLinkedin()).isEqualTo(UPDATED_LINK_LINKEDIN);
        assertThat(testEmpresa.getLinkTwitter()).isEqualTo(UPDATED_LINK_TWITTER);
        assertThat(testEmpresa.getSite()).isEqualTo(UPDATED_SITE);
        assertThat(testEmpresa.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testEmpresa.getCriado()).isEqualTo(UPDATED_CRIADO);
    }

    @Test
    @Transactional
    void patchNonExistingEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empresaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empresaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpresa() throws Exception {
        int databaseSizeBeforeUpdate = empresaRepository.findAll().size();
        empresa.setId(count.incrementAndGet());

        // Create the Empresa
        EmpresaDTO empresaDTO = empresaMapper.toDto(empresa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpresaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(empresaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empresa in the database
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpresa() throws Exception {
        // Initialize the database
        empresaRepository.saveAndFlush(empresa);

        int databaseSizeBeforeDelete = empresaRepository.findAll().size();

        // Delete the empresa
        restEmpresaMockMvc
            .perform(delete(ENTITY_API_URL_ID, empresa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Empresa> empresaList = empresaRepository.findAll();
        assertThat(empresaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
