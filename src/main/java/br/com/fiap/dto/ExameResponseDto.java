package br.com.fiap.dto;

import br.com.fiap.models.Exame;

/**
 * DTO (Data Transfer Object) para resposta de exame
 * Utilizado para transferir dados do exame para o cliente
 * Inclui o ID gerado automaticamente pelo banco de dados
 */
public class ExameResponseDto {
    private Integer id_exame;
    private String nome_exame;
    private String resultado_exame;
    private String status_resultado;

    /**
     * Converte entidade Exame para DTO de resposta
     * @param exame Entidade Exame a ser convertida
     * @return DTO populado com dados do exame
     */
    public static ExameResponseDto convertToDto(Exame exame) {
        ExameResponseDto dto = new ExameResponseDto();
        dto.setId_exame(exame.getId_exame());
        dto.setNome_exame(exame.getNome_exame());
        dto.setResultado_exame(exame.getResultado_exame());
        dto.setStatus_resultado(exame.getStatus_resultado());
        return dto;
    }

    /**
     * Obtém ID do exame gerado automaticamente pelo banco
     * @return ID único do exame
     */
    public Integer getId_exame() {
        return id_exame;
    }

    /**
     * Define ID do exame
     * @param id_exame ID gerado automaticamente pelo banco de dados
     */
    public void setId_exame(Integer id_exame) {
        this.id_exame = id_exame;
    }

    /**
     * Obtém nome do exame
     * @return Nome do exame
     */
    public String getNome_exame() {
        return nome_exame;
    }

    /**
     * Define nome do exame
     * @param nome_exame Nome do exame
     */
    public void setNome_exame(String nome_exame) {
        this.nome_exame = nome_exame;
    }

    /**
     * Obtém resultado do exame
     * @return Resultado do exame
     */
    public String getResultado_exame() {
        return resultado_exame;
    }

    /**
     * Define resultado do exame
     * @param resultado_exame Resultado do exame
     */
    public void setResultado_exame(String resultado_exame) {
        this.resultado_exame = resultado_exame;
    }

    /**
     * Obtém status do resultado
     * @return Status do resultado (pode ser definido manualmente ou calculado)
     */
    public String getStatus_resultado() {
        return status_resultado;
    }

    /**
     * Define status do resultado
     * @param status_resultado Status do resultado
     */
    public void setStatus_resultado(String status_resultado) {
        this.status_resultado = status_resultado;
    }

    /**
     * Representação em string do objeto para debugging
     * @return String formatada com todos os dados do exame
     */
    @Override
    public String toString() {
        return "ExameResponseDto{" +
                "id_exame=" + id_exame +
                ", nome_exame='" + nome_exame + '\'' +
                ", resultado_exame='" + resultado_exame + '\'' +
                ", status_resultado='" + status_resultado + '\'' +
                '}';
    }
}
