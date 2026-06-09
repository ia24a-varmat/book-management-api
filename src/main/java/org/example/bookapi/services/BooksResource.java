package org.example.bookapi.services;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.example.bookapi.model.Book;
import org.example.bookapi.service.BookService;

import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BooksResource {


    private final BookService service = new BookService();

    @GET
    @Path("/ping")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping() {
        return Response.ok("API is running").build();
    }

    @GET
    @PermitAll
    public Response getAllBooks() {
        List<Book> books = service.findAll();
        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response getBookById(@PathParam("id") int id) {
        Book book = service.findById(id);

        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found")
                    .build();
        }

        return Response.ok(book).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response createBook(@Valid Book book) {
        service.save(book);

        return Response.status(Response.Status.CREATED)
                .entity(book)
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response updateBook(@PathParam("id") int id, @Valid Book book) {
        Book existing = service.findById(id);

        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found")
                    .build();
        }

        book.setBookId(id);
        service.update(book);

        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBook(@PathParam("id") int id) {
        boolean deleted = service.delete(id);

        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Book not found")
                    .build();
        }

        return Response.ok("Book deleted").build();
    }

    @GET
    @Path("/count")
    @PermitAll
    public Response countBooks() {
        long count = service.count();
        return Response.ok(count).build();
    }

    @GET
    @Path("/available/{available}")
    @PermitAll
    public Response getBooksByAvailable(@PathParam("available") boolean available) {
        List<Book> books = service.findByAvailable(available);
        return Response.ok(books).build();
    }

    @GET
    @Path("/price/{price}")
    @PermitAll
    public Response getBooksByPrice(@PathParam("price") BigDecimal price) {
        List<Book> books = service.findByPriceGreaterThan(price);
        return Response.ok(books).build();
    }

    @POST
    @Path("/bulk")
    @RolesAllowed("ADMIN")
    public Response createMultipleBooks(@Valid List<Book> books) {
        service.saveAll(books);

        return Response.status(Response.Status.CREATED)
                .entity(books)
                .build();
    }

    @DELETE
    @Path("/date/{date}")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBooksByDate(@PathParam("date") String dateString) {
        LocalDate date = LocalDate.parse(dateString);

        int deleted = service.deleteByPublishDate(date);

        return Response.ok(deleted + " books deleted").build();
    }
}