package br.com.fiap.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) para requisição de exame
 * Utilizado para transferência de dados entre camadas da aplicação
 * O ID é gerado automaticamente pelo banco de dados
 */
@XmlRootElement
public class ExameRequestDto {

    private Integer id_exame;

    @NotNull(message = "Nome do exame é obrigatório")
    @NotBlank(message = "Nome do exame não pode estar em branco")
    private String nome_exame;

    @NotNull(message = "Resultado do exame é obrigatório")
    private String resultado_exame;

    @NotNull(message = "Status do resultado é obrigatório") // Adicionado
    private String status_resultado;

    /**
     * Construtor padrão
     */
    public ExameRequestDto() {
    }

    /**
     * Construtor para criação de novos exames sem ID
     * @param nome_exame Nome do exame
     * @param resultado_exame Resultado do exame
     * @param status_resultado Status do resultado
     */
    public ExameRequestDto(String nome_exame, String resultado_exame, String status_resultado) {
        this.nome_exame = nome_exame;
        this.resultado_exame = resultado_exame;
        this.status_resultado = status_resultado;

    }

    /**
     * Obtém ID do exame
     * Pode retornar null para exames novos que ainda não foram persistidos
     * @return ID do exame ou null se não foi gerado ainda
     */
    public Integer getId_exame() {
        return id_exame;
    }

    /**
     * Define ID do exame
     * Normalmente chamado pelo DAO após inserção no banco
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
     * @return Status do resultado
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
     * Verifica se o exame já possui um ID atribuído
     * @return true se o exame possui ID, false caso contrário
     */
    public boolean possuiId() {
        return id_exame != null;
    }

    /**
     * Verifica se resultado é normal baseado no conteúdo do resultado
     * @return "Resultado normal" se contém "normal", caso contrário "Resultado fora do padrão"
     */
    public String verificarResultado() {
        if (resultado_exame != null && resultado_exame.toLowerCase().contains("normal")) {
            return "Resultado normal";
        } else {
            return "Resultado fora do padrão";
        }
    }

    /**
     * Representação em string do objeto ExameRequestDto
     * @return String representando o exame
     */
    @Override
    public String toString() {
        return "ExameRequestDto{" +
                "id_exame=" + id_exame +
                ", nome_exame='" + nome_exame + '\'' +
                ", resultado_exame='" + resultado_exame + '\'' +
                ", status_resultado='" + status_resultado + '\'' + // Adicionado
                '}';
    }
}