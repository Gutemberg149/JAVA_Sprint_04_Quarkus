package br.com.fiap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO para requisição de médico
 */
@XmlRootElement
public class MedicoRequestDto {

    private Integer id_medico;

    @NotBlank(message = "Nome é obrigatório")
    @NotNull(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Especialidade é obrigatória")
    @NotNull(message = "Especialidade é obrigatória")
    private String especialidade;

    @Positive(message = "CRM deve ser positivo")
    private int crm;

    /**
     * Construtor padrão
     */
    public MedicoRequestDto() {
    }

    /**
     * Construtor com parâmetros
     */
    public MedicoRequestDto(String nome, String especialidade, int crm) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    /**
     * Construtor completo
     */
    public MedicoRequestDto(Integer id_medico, String nome, String especialidade, int crm) {
        this.id_medico = id_medico;
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    /**
     * Obtém ID do médico
     */
    public Integer getId_medico() {
        return id_medico;
    }

    /**
     * Define ID do médico
     */
    public void setId_medico(Integer id_medico) {
        this.id_medico = id_medico;
    }

    /**
     * Obtém nome do médico
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define nome do médico
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém especialidade
     */
    public String getEspecialidade() {
        return especialidade;
    }

    /**
     * Define especialidade
     */
    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    /**
     * Obtém CRM
     */
    public int getCrm() {
        return crm;
    }

    /**
     * Define CRM
     */
    public void setCrm(int crm) {
        this.crm = crm;
    }

    /**
     * Representação em string do objeto
     */
    @Override
    public String toString() {
        return "MedicoRequestDto{" +
                "id_medico=" + id_medico +
                ", nome='" + nome + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", crm=" + crm +
                '}';
    }
}