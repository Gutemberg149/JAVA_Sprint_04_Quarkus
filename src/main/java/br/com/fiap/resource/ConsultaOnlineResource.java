//package br.com.fiap.resource;
//
//import br.com.fiap.dto.ConsultaOnlineRequestDto;
//import br.com.fiap.dto.ConsultaOnlineResponseDto;
//import br.com.fiap.models.ConsultaOnline;
//import br.com.fiap.service.ConsultaOnlineService;
//import jakarta.inject.Inject;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.*;
//import jakarta.ws.rs.NotFoundException;
//import java.net.URI;
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * Recurso REST para consultas online
// */
//@Path("/consultaonline")
//public class ConsultaOnlineResource {
//    @Inject
//    private ConsultaOnlineService consultaOnlineService;
//
//    /**
//     * Lista todas as consultas online
//     */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response listar() {
//        try {
//            List<ConsultaOnlineResponseDto> consultas = consultaOnlineService.listar();
//            return Response.ok(consultas).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Erro interno ao listar consultas online")
//                    .build();
//        }
//    }
//
//    /**
//     * Busca consulta online por ID
//     */
//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response buscarPorId(@PathParam("id") int id) {
//        try {
//            ConsultaOnlineResponseDto consulta = consultaOnlineService.buscarPorId(id);
//            return Response.ok(consulta).build();
//        } catch (NotFoundException e) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("Consulta online não encontrada com ID: " + id)
//                    .build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("ID inválido: " + e.getMessage())
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Erro interno ao buscar consulta online")
//                    .build();
//        }
//    }
//
//    /**
//     * Cadastra nova consulta online
//     */
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response cadastrar(ConsultaOnlineRequestDto consultaDto, @Context UriInfo uriInfo) {
//        try {
//            ConsultaOnline consultaCadastrada = null;
//            try {
//                consultaCadastrada = consultaOnlineService.cadastrar(consultaDto);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
//            builder.path(Integer.toString(consultaCadastrada.getIdConsulta()));
//            URI location = builder.build();
//
//            return Response.created(location)
//                    .build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Dados inválidos: " + e.getMessage())
//                    .build();
//        } catch (RuntimeException e) {
//            if (e.getMessage().contains("constraint") || e.getMessage().contains("duplicate")) {
//                return Response.status(Response.Status.CONFLICT)
//                        .entity("Conflito de dados: " + e.getMessage())
//                        .build();
//            }
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Erro interno ao cadastrar consulta online")
//                    .build();
//        }
//    }
//
//    /**
//     * Atualiza consulta online existente
//     */
//    @PUT
//    @Path("/{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response updateConsultaOnline(ConsultaOnlineRequestDto consultaDto, @PathParam("id") int id) {
//        try {
//            consultaOnlineService.atualizar(consultaDto, id);
//
//            ConsultaOnlineResponseDto updated = consultaOnlineService.buscarPorId(id);
//            return Response.ok(updated).build();
//
//        } catch (NotFoundException e) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("Consulta online não encontrada com ID: " + id + " para atualização")
//                    .build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Dados inválidos: " + e.getMessage())
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Erro interno ao atualizar consulta online")
//                    .build();
//        }
//    }
//
//    /**
//     * Exclui consulta online por ID
//     */
//    @DELETE
//    @Path("/{id}")
//    public Response excluir(@PathParam("id") int id) {
//        try {
//            consultaOnlineService.excluir(id);
//            return Response.noContent()
//                    .build();
//        } catch (NotFoundException e) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("Consulta online não encontrada com ID: " + id + " para exclusão")
//                    .build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("ID inválido: " + e.getMessage())
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Erro interno ao excluir consulta online")
//                    .build();
//        }
//    }
//}

package br.com.fiap.resource;

import br.com.fiap.dto.ConsultaOnlineRequestDto;
import br.com.fiap.dto.ConsultaOnlineResponseDto;
import br.com.fiap.models.ConsultaOnline;
import br.com.fiap.service.ConsultaOnlineService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.NotFoundException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * Recurso REST para consultas online
 */
@Path("/consultaonline")
public class ConsultaOnlineResource {

    @Inject
    private ConsultaOnlineService consultaOnlineService;

    /**
     * Lista todas as consultas online
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        try {
            List<ConsultaOnlineResponseDto> consultas = consultaOnlineService.listar();
            return Response.ok(consultas).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao listar consultas online")
                    .build();
        }
    }

    /**
     * Busca consulta online por ID
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            ConsultaOnlineResponseDto consulta = consultaOnlineService.buscarPorId(id);
            return Response.ok(consulta).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Consulta online não encontrada com ID: " + id)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao buscar consulta online")
                    .build();
        }
    }

    /**
     * Cadastra nova consulta online
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cadastrar(ConsultaOnlineRequestDto consultaDto, @Context UriInfo uriInfo) {
        try {
            // Valida o DTO antes de processar para capturar erros de deserialização ou validação
            consultaDto.validateForCreate();

            ConsultaOnline consultaCadastrada = consultaOnlineService.cadastrar(consultaDto);

            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(Integer.toString(consultaCadastrada.getIdConsulta()));
            URI location = builder.build();

            return Response.created(location).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && (e.getMessage().contains("constraint") || e.getMessage().contains("duplicate"))) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Conflito de dados: " + e.getMessage())
                        .build();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao cadastrar consulta online")
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro de banco de dados ao cadastrar consulta online")
                    .build();
        }
    }

    /**
     * Atualiza consulta online existente
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConsultaOnline(ConsultaOnlineRequestDto consultaDto, @PathParam("id") int id) {
        try {
            consultaOnlineService.atualizar(consultaDto, id);

            ConsultaOnlineResponseDto updated = consultaOnlineService.buscarPorId(id);
            return Response.ok(updated).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Consulta online não encontrada com ID: " + id + " para atualização")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Dados inválidos: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar consulta online")
                    .build();
        }
    }

    /**
     * Exclui consulta online por ID
     */
    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            consultaOnlineService.excluir(id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Consulta online não encontrada com ID: " + id + " para exclusão")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao excluir consulta online")
                    .build();
        }
    }
}
