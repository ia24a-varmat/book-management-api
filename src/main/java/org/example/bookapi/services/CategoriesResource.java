package org.example.bookapi.services;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.bookapi.dao.CategoryDao;
import org.example.bookapi.model.Category;
import org.example.bookapi.service.CategoryService;

import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoriesResource {

    private final CategoryService service = new CategoryService();

    @GET
    @PermitAll
    public Response getAllCategories() {
        List<Category> categories = service.findAll();
        return Response.ok(categories).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response getCategoryById(@PathParam("id") int id) {
        Category category = service.findById(id);

        if (category == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found")
                    .build();
        }

        return Response.ok(category).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response createCategory(@Valid Category category) {
        try {
            service.save(category);

            return Response.status(Response.Status.CREATED)
                    .entity(category)
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Category could not be created")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateCategory(@PathParam("id") int id, @Valid Category category) {
        Category existing = service.findById(id);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found")
                    .build();
        }

        try {
            category.setCategoryId(id);
            service.update(category);
            return Response.ok(category).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Category could not be updated")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCategory(@PathParam("id") int id) {
        boolean deleted = service.delete(id);

        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Category not found")
                    .build();
        }

        return Response.ok("Category deleted").build();
    }
}