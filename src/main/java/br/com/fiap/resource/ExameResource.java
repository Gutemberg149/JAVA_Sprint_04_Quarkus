package br.com.fiap.resource;

import br.com.fiap.dto.ExameRequestDto;
import br.com.fiap.dto.ExameResponseDto;
import br.com.fiap.models.Exame;
import br.com.fiap.service.ExameService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * Recurso REST para operações com exames médicos
 * Expõe endpoints para CRUD de exames com IDs gerados automaticamente
 */
@Path("/exames")
public class ExameResource {
    @Inject
    private ExameService exameService;

    /**
     * Lista todos os exames cadastrados no sistema
     * @return Response com lista de exames em formato JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        try {
            List<ExameResponseDto> exames = exameService.listar();
            return Response.ok(exames).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao listar exames: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Busca um exame específico pelo ID gerado automaticamente
     * @param id ID do exame gerado pelo banco de dados
     * @return Response com dados do exame em formato JSON
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            ExameResponseDto exame = exameService.buscarPorId(id);
            return Response.ok(exame).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Exame não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao buscar exame")
                    .build();
        }
    }

    /**
     * Cadastra um novo exame no sistema
     * O ID é gerado automaticamente pelo banco de dados
     * @param exameDto DTO com dados do exame a ser cadastrado (sem ID)
     * @param uriInfo Informações de URI para construção da location header
     * @return Response com status 201 Created e location header
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cadastrar(ExameRequestDto exameDto, @Context UriInfo uriInfo) {
        try {
            Exame exameCriado = null;
            try {
                exameCriado = exameService.cadastrar(exameDto);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(Integer.toString(exameCriado.getId_exame()));
            URI location = builder.build();

            return Response.created(location).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao cadastrar exame: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Atualiza um exame existente
     * @param exame Objeto Exame com dados atualizados
     * @param id ID do exame a ser atualizado (gerado automaticamente)
     * @return Response com status 200 OK em caso de sucesso
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(Exame exame, @PathParam("id") int id) {
        try {

            exameService.buscarPorId(id);


            exame.setId_exame(id);


            exameService.atualizar(exame);

            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Exame não encontrado com ID: " + id + " para atualização")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar exame")
                    .build();
        }
    }

    /**
     * Exclui um exame pelo ID gerado automaticamente
     * @param id ID do exame a ser excluído
     * @return Response com status 204 No Content em caso de sucesso
     */
    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            exameService.excluir(id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Exame não encontrado com ID: " + id + " para exclusão")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao excluir exame")
                    .build();
        }
    }

    /**
     * Endpoint adicional para verificar saúde do recurso
     * @return Response com status de saúde do serviço
     */
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Response healthCheck() {
        return Response.ok("Serviço de exames está funcionando corretamente").build();
    }
}