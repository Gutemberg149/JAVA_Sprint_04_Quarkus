package br.com.fiap.resource;

import br.com.fiap.dto.HistoricoConsultaRequestDto;
import br.com.fiap.dto.HistoricoConsultaResponseDto;
import br.com.fiap.models.HistoricoConsulta;
import br.com.fiap.service.HistoricoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.NotFoundException;
import java.net.URI;
import java.util.List;

/**
 * Recurso REST para histórico de consultas
 */
@Path("/historicoconsulta")
public class HistoricoConsultaResource {

    @Inject
    private HistoricoService historicoService;


    /**
     * Lista todos os históricos
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        try {
            List<HistoricoConsultaResponseDto> historicos = (List<HistoricoConsultaResponseDto>) historicoService.listar();
            return Response.ok(historicos).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao listar históricos de consulta")
                    .build();
        }
    }

    /**
     * Busca histórico por ID
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            HistoricoConsultaResponseDto historico = historicoService.buscarPorId(id);
            return Response.ok(historico).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Histórico de consulta não encontrado com ID: " + id)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao buscar histórico de consulta")
                    .build();
        }
    }

    /**
     * Cadastra novo histórico
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrar(HistoricoConsultaRequestDto historicoDto, @Context UriInfo uriInfo) {
        try {
            HistoricoConsultaResponseDto dto = historicoService.cadastrar(historicoDto);

            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(Integer.toString(dto.getIdHistorico()));
            URI location = builder.build();

            return Response.ok(dto).location(location).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao cadastrar histórico de consulta")
                    .build();
        }
    }

    /**
     * Atualiza histórico existente
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizar(HistoricoConsultaRequestDto historicoDto, @PathParam("id") int id) {
        try {
            historicoService.atualizar(historicoDto, id);

            HistoricoConsultaResponseDto updated = historicoService.buscarPorId(id);
            return Response.ok(updated).build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Histórico de consulta não encontrado com ID: " + id + " para atualização")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar histórico de consulta")
                    .build();
        }
    }

    /**
     * Exclui histórico por ID
     */
    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            historicoService.excluir(id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Histórico de consulta não encontrado com ID: " + id + " para exclusão")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao excluir histórico de consulta")
                    .build();
        }
    }
}