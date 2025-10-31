package br.com.fiap.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Modelo de médico
 */
@XmlRootElement
public class Medico {

    @JsonProperty("id_medico")
    private int idMedico;

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
    public Medico() {
    }

    /**
     * Construtor com parâmetros
     */
    public Medico(String nome, String especialidade, int crm) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    /**
     * Construtor completo
     */
    public Medico(int idMedico, String nome, String especialidade, int crm) {
        this.idMedico = idMedico;
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    /**
     * Representação em string
     */
    @Override
    public String toString() {
        return "Medico{" +
                "idMedico=" + idMedico +
                ", nome='" + nome + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", crm=" + crm +
                '}';
    }

    /**
     * Obtém ID do médico (camelCase)
     */
    public int getIdMedico() {
        return idMedico;
    }

    /**
     * Define ID do médico (camelCase)
     */
    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
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
}