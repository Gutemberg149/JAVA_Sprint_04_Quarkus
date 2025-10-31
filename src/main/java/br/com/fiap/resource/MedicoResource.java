package br.com.fiap.resource;

import br.com.fiap.dto.MedicoRequestDto;
import br.com.fiap.dto.MedicoResponseDto;
import br.com.fiap.models.Medico;
import br.com.fiap.service.MedicoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * Recurso REST para médicos
 */
@Path("/medicos")
public class MedicoResource {
    @Inject
    private MedicoService medicoService;

    /**
     * Lista todos os médicos
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        try {
            List<MedicoResponseDto> medicos = medicoService.listar();
            return Response.ok(medicos).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao listar médicos: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Busca médico por ID
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            MedicoResponseDto medico = medicoService.buscarPorId(id);
            if (medico == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Médico não encontrado com ID: " + id)
                        .build();
            }
            return Response.ok(medico).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Médico não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao buscar médico: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Cadastra novo médico
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cadastrar(MedicoRequestDto medicoDto, @Context UriInfo uriInfo) {
        try {
            Medico medicoCadastrado = null;
            try {
                medicoCadastrado = medicoService.cadastrar(medicoDto);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(Integer.toString(medicoCadastrado.getIdMedico()));
            URI location = builder.build();

            return Response.created(location)
                    .entity("Médico cadastrado com sucesso com ID: " + medicoCadastrado.getIdMedico())
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("constraint") || e.getMessage().contains("duplicate")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Conflito de dados: " + e.getMessage())
                        .build();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao cadastrar médico: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Atualiza médico existente
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(MedicoRequestDto medicoDto, @PathParam("id") int id) {
        try {
            medicoDto.setId_medico(id);
            medicoService.atualizar(medicoDto);
            return Response.ok()
                    .entity("Médico atualizado com sucesso")
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Médico não encontrado com ID: " + id + " para atualização")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar médico: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Exclui médico por ID
     */
    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            medicoService.excluir(id);
            return Response.noContent()
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Médico não encontrado com ID: " + id + " para exclusão")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao excluir médico: " + e.getMessage())
                    .build();
        }
    }
}