package com.mycompany.escuelatareas.resources;

import conexion.Conexion;
import dao.TareaDAO;
import dominio.Tarea;
import interfaces.ITareaDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/tareas")
public class TareasResources {
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTarea(@PathParam("id") int id) {
        ITareaDAO tareas = null;
        
        try {
            tareas = new TareaDAO();
            Tarea padre = tareas.obten(id);
            
            if(padre != null) {
                return Response.ok(padre).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la tarea").build();
        }finally {
            if(tareas != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTarea(Tarea tarea) {
        ITareaDAO tareas = null;
        
        try {
            tareas = new TareaDAO();
            Tarea nuevo = tareas.agregarTarea(tarea);
            return Response.status(Response.Status.CREATED).entity(nuevo).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al añadir la tarea").build();
        }finally {
            if(tareas != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyTarea(@PathParam("id") int id, Tarea tarea) {
        ITareaDAO tareas = null;
        
        try {
            tareas = new TareaDAO();
            tarea.setId(id);
            Tarea actualizado = tareas.modificarTarea(tarea);

            if(actualizado != null) {
                return Response.ok(actualizado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al modificar la tarea").build();
        }finally {
            if(tareas != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTarea(int id) {
        ITareaDAO tareas = null;
        
        try {
            tareas = new TareaDAO();
            Tarea eliminado = tareas.eliminarTarea(id);
        
            if(eliminado != null) {
                return Response.ok(eliminado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener al eliminar la tareas").build();
        }finally {
            if(tareas != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTareas() {
        ITareaDAO tareas = null;
        
        try {
            tareas = new TareaDAO();
            List<Tarea> lTareas = tareas.obtenerTareas();
            return Response.ok(lTareas).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la lista de tareas").build();
        }finally {
            if(tareas != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Path("/curso/{idCurso}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTarasByCurso(@PathParam("idCurso") int idCurso) {
        ITareaDAO tareas = null;
        
        try {
            tareas = new TareaDAO();
            List<Tarea> lTareas = tareas.obtenerTareasPorCurso(idCurso);
            return Response.ok(lTareas).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la lista de tareas").build();
        }finally {
            if(tareas != null) {
                Conexion.cerrarConexion();
            }
        }
    }
}
