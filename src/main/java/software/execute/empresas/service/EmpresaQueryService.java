package software.execute.empresas.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.execute.empresas.domain.*; // for static metamodels
import software.execute.empresas.domain.Empresa;
import software.execute.empresas.repository.EmpresaRepository;
import software.execute.empresas.service.criteria.EmpresaCriteria;
import software.execute.empresas.service.dto.EmpresaDTO;
import software.execute.empresas.service.mapper.EmpresaMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Empresa} entities in the database.
 * The main input is a {@link EmpresaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmpresaDTO} or a {@link Page} of {@link EmpresaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmpresaQueryService extends QueryService<Empresa> {

    private final Logger log = LoggerFactory.getLogger(EmpresaQueryService.class);

    private final EmpresaRepository empresaRepository;

    private final EmpresaMapper empresaMapper;

    public EmpresaQueryService(EmpresaRepository empresaRepository, EmpresaMapper empresaMapper) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
    }

    /**
     * Return a {@link List} of {@link EmpresaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmpresaDTO> findByCriteria(EmpresaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Empresa> specification = createSpecification(criteria);
        return empresaMapper.toDto(empresaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmpresaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmpresaDTO> findByCriteria(EmpresaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Empresa> specification = createSpecification(criteria);
        return empresaRepository.findAll(specification, page).map(empresaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmpresaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Empresa> specification = createSpecification(criteria);
        return empresaRepository.count(specification);
    }

    /**
     * Function to convert {@link EmpresaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Empresa> createSpecification(EmpresaCriteria criteria) {
        Specification<Empresa> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Empresa_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Empresa_.nome));
            }
            if (criteria.getCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnpj(), Empresa_.cnpj));
            }
            if (criteria.getRazaoSocial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazaoSocial(), Empresa_.razaoSocial));
            }
            if (criteria.getDataConstituicao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataConstituicao(), Empresa_.dataConstituicao));
            }
            if (criteria.getLogoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogoUrl(), Empresa_.logoUrl));
            }
            if (criteria.getLinkFacebook() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkFacebook(), Empresa_.linkFacebook));
            }
            if (criteria.getLinkInstagram() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkInstagram(), Empresa_.linkInstagram));
            }
            if (criteria.getLinkLinkedin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkLinkedin(), Empresa_.linkLinkedin));
            }
            if (criteria.getLinkTwitter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkTwitter(), Empresa_.linkTwitter));
            }
            if (criteria.getSite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSite(), Empresa_.site));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), Empresa_.ativo));
            }
            if (criteria.getCriado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriado(), Empresa_.criado));
            }
        }
        return specification;
    }
}
