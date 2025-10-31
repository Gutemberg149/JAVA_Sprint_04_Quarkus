package br.com.fiap.dto;

import br.com.fiap.models.Exame;
import br.com.fiap.models.Medico;
import br.com.fiap.models.Paciente;
import br.com.fiap.models.ConsultaOnline;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para resposta de consulta online
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultaOnlineResponseDto {

    private Integer idConsulta;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", locale = "pt_BR")
    private LocalDate dataConsulta;

    private String status;
    private String link;
    private Exame exame;
    private Medico medico;
    private Paciente paciente;

    @JsonIgnore
    private List<Exame> listaExame = new ArrayList<>();

    /**
     * Construtor padrão
     */
    public ConsultaOnlineResponseDto() {
    }

    /**
     * Converte ConsultaOnline para DTO
     */
    public static ConsultaOnlineResponseDto convertToDto(ConsultaOnline consultaOnline) {
        if (consultaOnline == null) {
            return null;
        }

        ConsultaOnlineResponseDto dto = new ConsultaOnlineResponseDto();
        dto.setIdConsulta(consultaOnline.getIdConsulta());
        dto.setDataConsulta(consultaOnline.getDataConsulta());
        dto.setStatus(consultaOnline.getStatus());
        dto.setLink(consultaOnline.getLink());
        dto.setExame(consultaOnline.getExame());
        dto.setMedico(consultaOnline.getMedico());
        dto.setPaciente(consultaOnline.getPaciente());
        dto.setListaExame(consultaOnline.getListaExame());

        return dto;
    }

    /**
     * Obtém ID da consulta
     */
    public Integer getIdConsulta() {
        return idConsulta;
    }

    /**
     * Define ID da consulta
     */
    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    /**
     * Obtém data da consulta
     */
    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    /**
     * Define data da consulta
     */
    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    /**
     * Obtém status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Obtém link
     */
    public String getLink() {
        return link;
    }

    /**
     * Define link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Obtém exame
     */
    public Exame getExame() {
        return exame;
    }

    /**
     * Define exame
     */
    public void setExame(Exame exame) {
        this.exame = exame;
    }

    /**
     * Obtém médico
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * Define médico
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    /**
     * Obtém paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Define paciente
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Obtém lista de exames
     */
    @JsonIgnore
    public List<Exame> getListaExame() {
        return listaExame;
    }

    /**
     * Define lista de exames
     */
    public void setListaExame(List<Exame> listaExame) {
        this.listaExame = listaExame;
    }
}

