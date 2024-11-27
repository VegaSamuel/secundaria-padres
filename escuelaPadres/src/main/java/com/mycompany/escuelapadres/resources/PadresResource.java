package com.mycompany.escuelapadres.resources;

import conexion.Conexion;
import dao.PadreDAO;
import dominio.Padre;
import interfaces.IPadreDAO;
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
import seguridad.JWTUtil;

@Path("/padres")
public class PadresResource {
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPadre(@PathParam("id") int id) {
        IPadreDAO padres = null;
        
        try {
            padres = new PadreDAO();
            Padre padre = padres.obten(id);
            
            if(padre != null) {
                return Response.ok(padre).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener el padre").build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginPadre(Padre padre) {
        IPadreDAO padres = null;
        
        try {
            padres = new PadreDAO();
            Padre existente = padres.obtenPorEmail(padre.getEmail());
            
            if(existente != null) {
                JWTUtil jwt = new JWTUtil();
                String token = jwt.generateToken(existente.getNombre());
                return Response.ok().header("Authorization", "Bearer " + token).entity(existente).build();
            }else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("No existe tal usuario").build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error en el login").build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Path("/correo/{correo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPadreEmail(@PathParam("correo") String correo) {
        IPadreDAO padres = null;
        
        try {
            padres = new PadreDAO();
            Padre padre = padres.obtenPorEmail(correo);
            
            if(padre != null) {
                return Response.ok(padre).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener el padre").build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPadre(Padre padre) {
        IPadreDAO padres = null;
        
        try {
            //padres = new PadreDAO();
            //Padre nuevo = padres.agregarPadre(padre);
            
            JWTUtil jwt = new JWTUtil();
            String token = jwt.generateToken(padre.getNombre());
            
            return Response.status(Response.Status.CREATED).header("Authorization", "Bearer " + token).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al añadir al padre" + e.getMessage()).build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyPadre(@PathParam("id") int id, Padre padre) {
        IPadreDAO padres = null;
        
        try {
            padres = new PadreDAO();
            padre.setId(id);
            Padre actualizado = padres.modificarPadre(padre);

            if(actualizado != null) {
                return Response.ok(actualizado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al modificar al padre").build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePadre(int id) {
        IPadreDAO padres = null;
        
        try {
            padres = new PadreDAO();
            Padre eliminado = padres.eliminarPadre(id);
        
            if(eliminado != null) {
                return Response.ok(eliminado).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener al eliminar al padre").build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPadres() {
        IPadreDAO padres = null;
        
        try {
            padres = new PadreDAO();
            List<Padre> lPadres = padres.obtenerPadres();
            return Response.ok(lPadres).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al obtener la lista de padres").build();
        }finally {
            if(padres != null) {
                Conexion.cerrarConexion();
            }
        }
    }
    
}
