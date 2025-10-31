package br.com.fiap.resource;

import br.com.fiap.dto.PacienteRequestDto;
import br.com.fiap.dto.PacienteResponseDto;
import br.com.fiap.service.PacienteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 * Recurso REST para pacientes (sem relação com ConsultaOnline)
 */
@Path("/pacientes")
public class PacienteResource {

    @Inject
    private PacienteService pacienteService;

    /**
     * Lista todos os pacientes
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        try {
            List<PacienteResponseDto> pacientes = pacienteService.listar();
            return Response.ok(pacientes).build();
        } catch (Exception e) {
            System.err.println("Erro ao listar pacientes: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao listar pacientes")
                    .build();
        }
    }

    /**
     * Busca paciente por ID
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            if (id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID do paciente deve ser positivo")
                        .build();
            }

            PacienteResponseDto paciente = pacienteService.buscarPorId(id);
            return Response.ok(paciente).build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Paciente não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            System.err.println("Erro ao buscar paciente ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao buscar paciente")
                    .build();
        }
    }

    /**
     * Busca paciente por CPF
     */
    @GET
    @Path("/cpf/{cpf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorCpf(@PathParam("cpf") String cpf) {
        try {
            if (cpf == null || cpf.length() != 11) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("CPF deve ter exatamente 11 dígitos")
                        .build();
            }

            PacienteResponseDto paciente = pacienteService.buscarPorCpf(cpf);
            if (paciente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Paciente não encontrado com CPF: " + cpf)
                        .build();
            }
            return Response.ok(paciente).build();

        } catch (Exception e) {
            System.err.println("Erro ao buscar paciente CPF " + cpf + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao buscar paciente por CPF")
                    .build();
        }
    }

    /**
     * Cadastra novo paciente
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrar(PacienteRequestDto pacienteDto, @Context UriInfo uriInfo) {
        try {
            // Validar dados básicos antes de processar
            if (pacienteDto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Dados do paciente não podem ser nulos")
                        .build();
            }

            // Limpar e validar dados
            pacienteDto.cleanData();
            if (!pacienteDto.isValid()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Dados do paciente inválidos ou incompletos")
                        .build();
            }

            PacienteResponseDto pacienteCadastrado = pacienteService.cadastrar(pacienteDto);

            // Construir URI do recurso criado
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(Integer.toString(pacienteCadastrado.getIdPaciente()));
            URI location = builder.build();

            return Response.created(location)
                    .entity(pacienteCadastrado)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("constraint") || e.getMessage().contains("duplicate") ||
                    e.getMessage().contains("unique") || e.getMessage().contains("CPF")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Conflito de dados: CPF já cadastrado")
                        .build();
            }
            System.err.println("Erro ao cadastrar paciente: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao cadastrar paciente")
                    .build();
        }
    }

    /**
     * Atualiza paciente existente
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(PacienteRequestDto pacienteDto, @PathParam("id") int id) {
        try {
            if (id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID do paciente deve ser positivo")
                        .build();
            }

            if (pacienteDto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Dados do paciente não podem ser nulos")
                        .build();
            }

            // Limpar e validar dados
            pacienteDto.cleanData();
            if (!pacienteDto.isValid()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Dados do paciente inválidos ou incompletos")
                        .build();
            }

            pacienteDto.setId(id);
            pacienteService.atualizar(pacienteDto);

            return Response.ok("Paciente atualizado com sucesso").build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Paciente não encontrado com ID: " + id)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("constraint") || e.getMessage().contains("duplicate") ||
                    e.getMessage().contains("unique") || e.getMessage().contains("CPF")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Conflito de dados: CPF já cadastrado")
                        .build();
            }
            System.err.println("Erro ao atualizar paciente ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar paciente")
                    .build();
        }
    }

    /**
     * Exclui paciente por ID
     */
    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            if (id <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("ID do paciente deve ser positivo")
                        .build();
            }

            pacienteService.excluir(id);
            return Response.noContent().build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Paciente não encontrado com ID: " + id)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("constraint") || e.getMessage().contains("foreign key")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Não é possível excluir paciente: existem registros relacionados")
                        .build();
            }
            System.err.println("Erro ao excluir paciente ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao excluir paciente")
                    .build();
        }
    }

    /**
     * Health check do recurso
     */
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Response health() {
        return Response.ok("Paciente Resource está funcionando").build();
    }
}