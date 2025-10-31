package br.com.fiap.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

/**
 * Modelo de entidade Exame
 * Representa um exame médico no sistema
 * O ID é gerado automaticamente pelo banco de dados
 */
@XmlRootElement
public class Exame {

    private Integer id_exame;

    @NotNull(message = "Nome do exame é obrigatório")
    @NotBlank(message = "Nome do exame não pode estar em branco")
    private String nome_exame;

    @NotNull(message = "Resultado do exame é obrigatório")
    private String resultado_exame;

    @NotNull(message = "Status do resultado é obrigatório")
    private String status_resultado;

    /**
     * Construtor padrão
     */
    public Exame() {
    }

    /**
     * Construtor para criação de novos exames
     */
    public Exame(String nome_exame, String resultado_exame, String status_resultado) {
        this.nome_exame = nome_exame;
        this.resultado_exame = resultado_exame;
        this.status_resultado = status_resultado;
    }

    @Override
    public String toString() {
        return "Exame{" +
                "id_exame=" + id_exame +
                ", nome_exame='" + nome_exame + '\'' +
                ", resultado_exame='" + resultado_exame + '\'' +
                ", status_resultado='" + status_resultado + '\'' +
                '}';
    }

    @JsonProperty("ID_EXAME")
    public Integer getId_exame() {
        return id_exame;
    }

    public void setId_exame(Integer id_exame) {
        this.id_exame = id_exame;
    }

    public boolean possuiId() {
        return id_exame != null && id_exame > 0;
    }

    public String getNome_exame() {
        return nome_exame;
    }

    public void setNome_exame(String nome_exame) {
        this.nome_exame = nome_exame;
    }

    public String getResultado_exame() {
        return resultado_exame;
    }

    public void setResultado_exame(String resultado_exame) {
        this.resultado_exame = resultado_exame;
    }

    public String getStatus_resultado() {
        return status_resultado;
    }

    public void setStatus_resultado(String status_resultado) {
        this.status_resultado = status_resultado;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Exame exame = (Exame) obj;
        return id_exame != null && id_exame.equals(exame.id_exame);
    }

    @Override
    public int hashCode() {
        return id_exame != null ? id_exame.hashCode() : 0;
    }
}