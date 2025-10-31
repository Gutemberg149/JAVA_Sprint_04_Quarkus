package br.com.fiap.dto;

import br.com.fiap.models.Paciente;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO para resposta de paciente (sem relação com ConsultaOnline)
 */
@XmlRootElement
public class PacienteResponseDto {

    @JsonProperty("id_paciente")
    private Integer idPaciente;

    @JsonProperty("nome_paciente")
    private String nomePaciente;

    @JsonProperty("cpf_paciente")
    private String cpfPaciente;

    public PacienteResponseDto() {
    }

    public PacienteResponseDto(Integer idPaciente, String nomePaciente, String cpfPaciente) {
        this.idPaciente = idPaciente;
        this.nomePaciente = nomePaciente;
        this.cpfPaciente = cpfPaciente;
    }

    /**
     * Converte Paciente para DTO
     */
    public static PacienteResponseDto convertToDto(Paciente paciente) {
        if (paciente == null) {
            return null;
        }

        return new PacienteResponseDto(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf()
        );
    }

    /**
     * Converte DTO para Paciente
     */
    public static Paciente convertToEntity(PacienteResponseDto dto) {
        if (dto == null) {
            return null;
        }

        Paciente paciente = new Paciente();
        paciente.setId(dto.getIdPaciente());
        paciente.setNome(dto.getNomePaciente());
        paciente.setCpf(dto.getCpfPaciente());
        return paciente;
    }

    // Getters e Setters
    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getCpfPaciente() {
        return cpfPaciente;
    }

    public void setCpfPaciente(String cpfPaciente) {
        this.cpfPaciente = cpfPaciente;
    }

    @Override
    public String toString() {
        return "PacienteResponseDto{" +
                "idPaciente=" + idPaciente +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", cpfPaciente='" + cpfPaciente + '\'' +
                '}';
    }
}