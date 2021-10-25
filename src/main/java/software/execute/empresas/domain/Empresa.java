package software.execute.empresas.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "sobre")
    private String sobre;

    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "data_constituicao")
    private LocalDate dataConstituicao;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "link_facebook")
    private String linkFacebook;

    @Column(name = "link_instagram")
    private String linkInstagram;

    @Column(name = "link_linkedin")
    private String linkLinkedin;

    @Column(name = "link_twitter")
    private String linkTwitter;

    @Column(name = "site")
    private String site;

    @Column(name = "ativo")
    private Boolean ativo;

    @NotNull
    @Column(name = "criado", nullable = false)
    private ZonedDateTime criado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empresa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Empresa nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobre() {
        return this.sobre;
    }

    public Empresa sobre(String sobre) {
        this.setSobre(sobre);
        return this;
    }

    public void setSobre(String sobre) {
        this.sobre = sobre;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public Empresa cnpj(String cnpj) {
        this.setCnpj(cnpj);
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return this.razaoSocial;
    }

    public Empresa razaoSocial(String razaoSocial) {
        this.setRazaoSocial(razaoSocial);
        return this;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public LocalDate getDataConstituicao() {
        return this.dataConstituicao;
    }

    public Empresa dataConstituicao(LocalDate dataConstituicao) {
        this.setDataConstituicao(dataConstituicao);
        return this;
    }

    public void setDataConstituicao(LocalDate dataConstituicao) {
        this.dataConstituicao = dataConstituicao;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public Empresa logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLinkFacebook() {
        return this.linkFacebook;
    }

    public Empresa linkFacebook(String linkFacebook) {
        this.setLinkFacebook(linkFacebook);
        return this;
    }

    public void setLinkFacebook(String linkFacebook) {
        this.linkFacebook = linkFacebook;
    }

    public String getLinkInstagram() {
        return this.linkInstagram;
    }

    public Empresa linkInstagram(String linkInstagram) {
        this.setLinkInstagram(linkInstagram);
        return this;
    }

    public void setLinkInstagram(String linkInstagram) {
        this.linkInstagram = linkInstagram;
    }

    public String getLinkLinkedin() {
        return this.linkLinkedin;
    }

    public Empresa linkLinkedin(String linkLinkedin) {
        this.setLinkLinkedin(linkLinkedin);
        return this;
    }

    public void setLinkLinkedin(String linkLinkedin) {
        this.linkLinkedin = linkLinkedin;
    }

    public String getLinkTwitter() {
        return this.linkTwitter;
    }

    public Empresa linkTwitter(String linkTwitter) {
        this.setLinkTwitter(linkTwitter);
        return this;
    }

    public void setLinkTwitter(String linkTwitter) {
        this.linkTwitter = linkTwitter;
    }

    public String getSite() {
        return this.site;
    }

    public Empresa site(String site) {
        this.setSite(site);
        return this;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Empresa ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public ZonedDateTime getCriado() {
        return this.criado;
    }

    public Empresa criado(ZonedDateTime criado) {
        this.setCriado(criado);
        return this;
    }

    public void setCriado(ZonedDateTime criado) {
        this.criado = criado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empresa)) {
            return false;
        }
        return id != null && id.equals(((Empresa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sobre='" + getSobre() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", razaoSocial='" + getRazaoSocial() + "'" +
            ", dataConstituicao='" + getDataConstituicao() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", linkFacebook='" + getLinkFacebook() + "'" +
            ", linkInstagram='" + getLinkInstagram() + "'" +
            ", linkLinkedin='" + getLinkLinkedin() + "'" +
            ", linkTwitter='" + getLinkTwitter() + "'" +
            ", site='" + getSite() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", criado='" + getCriado() + "'" +
            "}";
    }
}
