package br.com.fiap;




import br.com.fiap.models.ConsultaOnline;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TesteData {

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Teste 1: Data normal
            String json1 = """
                {
                    "dataConsulta": "02-Fev-2001",
                    "status": "Andamento",
                    "link": "www.google.com.br",
                    "idExame": 2,
                    "idMedico": 1,
                    "idPaciente": 5
                }
                """;

            ConsultaOnline consulta1 = mapper.readValue(json1, ConsultaOnline.class);
            System.out.println("✅ Teste 1 - Sucesso!");
            System.out.println("Data: " + consulta1.getDataConsulta());
            System.out.println("Status: " + consulta1.getStatus());
            System.out.println();

            // Teste 2: Outra data
            String json2 = """
                {
                    "dataConsulta": "15-Dez-2023",
                    "status": "Concluída"
                }
                """;

            ConsultaOnline consulta2 = mapper.readValue(json2, ConsultaOnline.class);
            System.out.println("✅ Teste 2 - Sucesso!");
            System.out.println("Data: " + consulta2.getDataConsulta());
            System.out.println();

            // Teste 3: Data inválida (para ver erro)
            try {
                String json3 = "{\"dataConsulta\": \"32-Jan-2023\", \"status\": \"Teste\"}";
                ConsultaOnline consulta3 = mapper.readValue(json3, ConsultaOnline.class);
            } catch (Exception e) {
                System.out.println("✅ Teste 3 - Erro esperado capturado:");
                System.out.println("Erro: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("❌ Erro no teste:");
            e.printStackTrace();
        }
    }
}