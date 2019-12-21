package br.com.meta.resources;

import br.com.meta.models.Contato;
import br.com.meta.models.Data;
import br.com.meta.services.ContatoService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("/contatos")
public class ContatoResource {

    private static final Logger LOGGER = Logger.getLogger(ContatoResource.class.getName());

    @Inject
    private ContatoService contatoService;

    @RolesAllowed("metaUser")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar(@QueryParam("page") Integer pagina, @QueryParam("size") Integer quantidade ) {
        LOGGER.log(Level.INFO, "Listando contatos de {0} até {1}", new Object[] {pagina, quantidade } );

        final List<Contato> contatoList = contatoService.listar(pagina, quantidade);

        if (contatoList == null) {
            return  Response.status(404).entity(new Data("Recurso não encontrado")).build();
        }
        return  Response.ok(contatoList).build();
    }

    @RolesAllowed("metaUser")
    @GET
    @Path(  "/{idContato}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperar(@PathParam( "idContato") Long idContato) {
        LOGGER.log(Level.INFO, "Recuperando contato::: {0}", idContato );

        final Contato contatoFinal = contatoService.recuperar(idContato);

        if (contatoFinal == null) {
            return  Response.status(404).entity(new Data("Recurso não encontrado")).build();
        }
        return  Response.ok(contatoFinal).build();
    }

    @RolesAllowed("metaUser")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response salvar(ContatoCreate contatoCreate) {
        LOGGER.log(Level.INFO, "Salvando contato.... {0}", contatoCreate);

        Contato contato = parseContato(contatoCreate);
        try {
            contatoService.salvar(contato);
        } catch (Exception e) {
            return Response.serverError().build();
        }

        return Response.ok().entity(contato).build();
    }

    @RolesAllowed("metaUser")
    @PUT
    @Path("{idContato}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizar(@PathParam("idContato") Long idContato, ContatoUpdate contatoCreate) {
        LOGGER.log(Level.INFO, "Atualizando contato.... {0}", contatoCreate);

        Contato contato = parseContato(contatoCreate, idContato);
        try {
            contatoService.atualizar(contato);
        } catch (Exception e) {
            return Response.serverError().build();
        }

        return Response.ok().entity(contato).build();
    }

    private Contato parseContato(ContatoUpdate contatoUpdate, Long idContato) {
        Contato contato = new Contato();
        contato.setId(idContato);
        contato.setCanal(contatoUpdate.getCanal());
        contato.setNome(contatoUpdate.getNome());
        contato.setObs(contatoUpdate.getObs());
        contato.setValor(contatoUpdate.getValor());
        return contato;
    }

    private Contato parseContato(ContatoCreate contatoCreate) {
        Contato contato = new Contato();
        contato.setCanal(contatoCreate.getCanal());
        contato.setNome(contatoCreate.getNome());
        contato.setObs(contatoCreate.getObs());
        contato.setValor(contatoCreate.getValor());
        return contato;
    }

}
