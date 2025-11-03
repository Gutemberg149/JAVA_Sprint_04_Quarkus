package br.com.fiap.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * Modelo de paciente (sem relação com ConsultaOnline)
 */
@XmlRootElement
public class Paciente {

    @JsonProperty("id_paciente")
    private Integer id;

    @JsonProperty("nome_paciente")
    @NotNull(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @JsonProperty("cpf_paciente")
    @NotNull(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas números")
    private String cpf;

    public Paciente() {
    }

    public Paciente(Integer id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public Paciente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public boolean isCpfValido() {
        return cpf != null && cpf.matches("\\d{11}");
    }

    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty() &&
                cpf != null && isCpfValido();
    }

    public void cleanData() {
        if (nome != null) {
            nome = nome.trim();
        }
        if (cpf != null) {
            cpf = cpf.trim().replaceAll("[^\\d]", "");
        }
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Paciente paciente = (Paciente) obj;
        return id != null && id.equals(paciente.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}