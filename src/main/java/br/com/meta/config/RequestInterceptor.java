package br.com.meta.config;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

import br.com.meta.models.Data;
import org.apache.commons.codec.binary.Base64;

@Provider
public class RequestInterceptor implements ContainerRequestFilter {

    private final static Logger LOGGER = Logger.getLogger(RequestInterceptor.class.getName());

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        Method method = resourceInfo.getResourceMethod();

        if (!method.isAnnotationPresent(PermitAll.class)) {
            if (method.isAnnotationPresent(DenyAll.class)) {
                containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("Acesso bloqueado para todos os usuários !!").build());
                return;
            }

            final MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
            if (authorization == null || authorization.isEmpty()) {
                createUnauthorizedResponse(containerRequestContext, "Acesso não autorizado");
                return;
            }

            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            String usernameAndPassword = new String(Base64.decodeBase64(encodedUserPassword.getBytes()));
            ;

            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            System.out.println(username);
            System.out.println(password);

            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                //Is user valid?
                if (!possuiAcesso(username, password, rolesSet)) {
                    createUnauthorizedResponse(containerRequestContext, "Acesso não autorizado!!!");
                    return;
                }
            }
        }
    }

    private void createUnauthorizedResponse(ContainerRequestContext containerRequestContext, String message) {
        containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(message).type(MediaType.TEXT_PLAIN_TYPE).build());
    }

    private boolean possuiAcesso(final String username, final String password, final Set<String> rolesSet) {
        boolean autorizarAcesso = false;

        if (username.equals("vempra") && password.equals("meta")) {
            String userRole = "metaUser";

            if (rolesSet.contains(userRole)) {
                autorizarAcesso = true;
            }
        }
        return autorizarAcesso;
    }
}
