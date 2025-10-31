package br.com.fiap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para requisição de paciente (sem relação com ConsultaOnline)
 */
@XmlRootElement
public class PacienteRequestDto {

    @JsonProperty("id_paciente")
    private Integer idPaciente;

    @JsonProperty("nome_paciente")
    @NotNull(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String nomePaciente;

    @JsonProperty("cpf_paciente")
    @NotNull(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas números")
    private String cpfPaciente;

    /**
     * Construtor padrão
     */
    public PacienteRequestDto() {
    }

    /**
     * Construtor para criação de pacientes
     */
    public PacienteRequestDto(String nomePaciente, String cpfPaciente) {
        this.nomePaciente = nomePaciente;
        this.cpfPaciente = cpfPaciente;
    }

    /**
     * Obtém ID do paciente
     * Pode ser null para novos pacientes (ID será gerado automaticamente)
     */
    public Integer getIdPaciente() {
        return idPaciente;
    }

    /**
     * Define ID do paciente
     * Normalmente definido após inserção no banco
     */
    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * Obtém nome do paciente
     */
    public String getNomePaciente() {
        return nomePaciente;
    }

    /**
     * Define nome do paciente
     */
    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    /**
     * Obtém CPF do paciente
     */
    public String getCpfPaciente() {
        return cpfPaciente;
    }

    /**
     * Define CPF do paciente
     */
    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    /**
     * Verifica se CPF é válido
     */
    public boolean isCpfValido() {
        return cpfPaciente != null && cpfPaciente.matches("\\d{11}");
    }

    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     */
    public boolean isValid() {
        return nomePaciente != null && !nomePaciente.trim().isEmpty() &&
                cpfPaciente != null && isCpfValido();
    }

    /**
     * Limpa e formata os dados
     */
    public void cleanData() {
        if (nomePaciente != null) {
            nomePaciente = nomePaciente.trim();
        }
        if (cpfPaciente != null) {
            cpfPaciente = cpfPaciente.trim().replaceAll("[^\\d]", "");
        }
    }

    /**
     * Representação em string para debugging
     */
    @Override
    public String toString() {
        return "PacienteRequestDto{" +
                "idPaciente=" + idPaciente +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", cpfPaciente='" + cpfPaciente + '\'' +
                '}';
    }


    /**
     * @deprecated Use getIdPaciente() instead
     */
    @Deprecated
    public Integer getId() {
        return idPaciente;
    }

    /**
     * @deprecated Use setIdPaciente() instead
     */
    @Deprecated
    public void setId(Integer id) {
        this.idPaciente = id;
    }

    /**
     * @deprecated Use getNomePaciente() instead
     */
    @Deprecated
    public String getNome() {
        return nomePaciente;
    }

    /**
     * @deprecated Use setNomePaciente() instead
     */
    @Deprecated
    public void setNome(String nome) {
        this.nomePaciente = nome;
    }

    /**
     * @deprecated Use getCpfPaciente() instead
     */
    @Deprecated
    public String getCpf() {
        return cpfPaciente;
    }

    /**
     * @deprecated Use setCpfPaciente() instead
     */
    @Deprecated
    public void setCpf(String cpf) {
        this.cpfPaciente = cpf;
    }
}