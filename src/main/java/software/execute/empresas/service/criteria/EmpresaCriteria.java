package software.execute.empresas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link software.execute.empresas.domain.Empresa} entity. This class is used
 * in {@link software.execute.empresas.web.rest.EmpresaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /empresas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmpresaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cnpj;

    private StringFilter razaoSocial;

    private LocalDateFilter dataConstituicao;

    private StringFilter logoUrl;

    private StringFilter linkFacebook;

    private StringFilter linkInstagram;

    private StringFilter linkLinkedin;

    private StringFilter linkTwitter;

    private StringFilter site;

    private BooleanFilter ativo;

    private ZonedDateTimeFilter criado;

    private Boolean distinct;

    public EmpresaCriteria() {}

    public EmpresaCriteria(EmpresaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cnpj = other.cnpj == null ? null : other.cnpj.copy();
        this.razaoSocial = other.razaoSocial == null ? null : other.razaoSocial.copy();
        this.dataConstituicao = other.dataConstituicao == null ? null : other.dataConstituicao.copy();
        this.logoUrl = other.logoUrl == null ? null : other.logoUrl.copy();
        this.linkFacebook = other.linkFacebook == null ? null : other.linkFacebook.copy();
        this.linkInstagram = other.linkInstagram == null ? null : other.linkInstagram.copy();
        this.linkLinkedin = other.linkLinkedin == null ? null : other.linkLinkedin.copy();
        this.linkTwitter = other.linkTwitter == null ? null : other.linkTwitter.copy();
        this.site = other.site == null ? null : other.site.copy();
        this.ativo = other.ativo == null ? null : other.ativo.copy();
        this.criado = other.criado == null ? null : other.criado.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmpresaCriteria copy() {
        return new EmpresaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCnpj() {
        return cnpj;
    }

    public StringFilter cnpj() {
        if (cnpj == null) {
            cnpj = new StringFilter();
        }
        return cnpj;
    }

    public void setCnpj(StringFilter cnpj) {
        this.cnpj = cnpj;
    }

    public StringFilter getRazaoSocial() {
        return razaoSocial;
    }

    public StringFilter razaoSocial() {
        if (razaoSocial == null) {
            razaoSocial = new StringFilter();
        }
        return razaoSocial;
    }

    public void setRazaoSocial(StringFilter razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public LocalDateFilter getDataConstituicao() {
        return dataConstituicao;
    }

    public LocalDateFilter dataConstituicao() {
        if (dataConstituicao == null) {
            dataConstituicao = new LocalDateFilter();
        }
        return dataConstituicao;
    }

    public void setDataConstituicao(LocalDateFilter dataConstituicao) {
        this.dataConstituicao = dataConstituicao;
    }

    public StringFilter getLogoUrl() {
        return logoUrl;
    }

    public StringFilter logoUrl() {
        if (logoUrl == null) {
            logoUrl = new StringFilter();
        }
        return logoUrl;
    }

    public void setLogoUrl(StringFilter logoUrl) {
        this.logoUrl = logoUrl;
    }

    public StringFilter getLinkFacebook() {
        return linkFacebook;
    }

    public StringFilter linkFacebook() {
        if (linkFacebook == null) {
            linkFacebook = new StringFilter();
        }
        return linkFacebook;
    }

    public void setLinkFacebook(StringFilter linkFacebook) {
        this.linkFacebook = linkFacebook;
    }

    public StringFilter getLinkInstagram() {
        return linkInstagram;
    }

    public StringFilter linkInstagram() {
        if (linkInstagram == null) {
            linkInstagram = new StringFilter();
        }
        return linkInstagram;
    }

    public void setLinkInstagram(StringFilter linkInstagram) {
        this.linkInstagram = linkInstagram;
    }

    public StringFilter getLinkLinkedin() {
        return linkLinkedin;
    }

    public StringFilter linkLinkedin() {
        if (linkLinkedin == null) {
            linkLinkedin = new StringFilter();
        }
        return linkLinkedin;
    }

    public void setLinkLinkedin(StringFilter linkLinkedin) {
        this.linkLinkedin = linkLinkedin;
    }

    public StringFilter getLinkTwitter() {
        return linkTwitter;
    }

    public StringFilter linkTwitter() {
        if (linkTwitter == null) {
            linkTwitter = new StringFilter();
        }
        return linkTwitter;
    }

    public void setLinkTwitter(StringFilter linkTwitter) {
        this.linkTwitter = linkTwitter;
    }

    public StringFilter getSite() {
        return site;
    }

    public StringFilter site() {
        if (site == null) {
            site = new StringFilter();
        }
        return site;
    }

    public void setSite(StringFilter site) {
        this.site = site;
    }

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public BooleanFilter ativo() {
        if (ativo == null) {
            ativo = new BooleanFilter();
        }
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
    }

    public ZonedDateTimeFilter getCriado() {
        return criado;
    }

    public ZonedDateTimeFilter criado() {
        if (criado == null) {
            criado = new ZonedDateTimeFilter();
        }
        return criado;
    }

    public void setCriado(ZonedDateTimeFilter criado) {
        this.criado = criado;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmpresaCriteria that = (EmpresaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cnpj, that.cnpj) &&
            Objects.equals(razaoSocial, that.razaoSocial) &&
            Objects.equals(dataConstituicao, that.dataConstituicao) &&
            Objects.equals(logoUrl, that.logoUrl) &&
            Objects.equals(linkFacebook, that.linkFacebook) &&
            Objects.equals(linkInstagram, that.linkInstagram) &&
            Objects.equals(linkLinkedin, that.linkLinkedin) &&
            Objects.equals(linkTwitter, that.linkTwitter) &&
            Objects.equals(site, that.site) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(criado, that.criado) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nome,
            cnpj,
            razaoSocial,
            dataConstituicao,
            logoUrl,
            linkFacebook,
            linkInstagram,
            linkLinkedin,
            linkTwitter,
            site,
            ativo,
            criado,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpresaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cnpj != null ? "cnpj=" + cnpj + ", " : "") +
            (razaoSocial != null ? "razaoSocial=" + razaoSocial + ", " : "") +
            (dataConstituicao != null ? "dataConstituicao=" + dataConstituicao + ", " : "") +
            (logoUrl != null ? "logoUrl=" + logoUrl + ", " : "") +
            (linkFacebook != null ? "linkFacebook=" + linkFacebook + ", " : "") +
            (linkInstagram != null ? "linkInstagram=" + linkInstagram + ", " : "") +
            (linkLinkedin != null ? "linkLinkedin=" + linkLinkedin + ", " : "") +
            (linkTwitter != null ? "linkTwitter=" + linkTwitter + ", " : "") +
            (site != null ? "site=" + site + ", " : "") +
            (ativo != null ? "ativo=" + ativo + ", " : "") +
            (criado != null ? "criado=" + criado + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
