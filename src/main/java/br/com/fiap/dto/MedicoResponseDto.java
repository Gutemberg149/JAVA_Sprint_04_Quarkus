package br.com.fiap.dto;

import br.com.fiap.models.Medico;

/**
 * DTO para resposta de médico
 */
public class MedicoResponseDto {
    private int id_medico;
    private String nome;
    private String especialidade;
    private int crm;

    /**
     * Converte Medico para DTO
     */
    public static MedicoResponseDto convertToDto(Medico medico) {
        MedicoResponseDto dto = new MedicoResponseDto();
        dto.setId_medico(medico.getIdMedico());
        dto.setNome(medico.getNome());
        dto.setCrm(medico.getCrm());
        dto.setEspecialidade(medico.getEspecialidade());
        return dto;
    }

    /**
     * Construtor padrão
     */
    public MedicoResponseDto() {
    }

    /**
     * Construtor com parâmetros
     */
    public MedicoResponseDto(int id_medico, String nome, String especialidade, int crm) {
        this.id_medico = id_medico;
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    /**
     * Obtém ID do médico
     */
    public int getId_medico() {
        return id_medico;
    }

    /**
     * Define ID do médico
     */
    public void setId_medico(int id_medico) {
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
        return "MedicoResponseDto{" +
                "id_medico=" + id_medico +
                ", nome='" + nome + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", crm=" + crm +
                '}';
    }
}