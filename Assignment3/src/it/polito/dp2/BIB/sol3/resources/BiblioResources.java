package it.polito.dp2.BIB.sol3.resources;

import it.polito.dp2.BIB.sol3.service.jaxb.*;
import it.polito.dp2.BIB.sol3.model.EBiblio;
import it.polito.dp2.BIB.sol3.service.BadRequestServiceException;
import it.polito.dp2.BIB.sol3.service.BiblioService;
import it.polito.dp2.BIB.sol3.service.ConflictServiceException;
import it.polito.dp2.BIB.sol3.service.SearchScope;

import java.math.BigInteger;
import java.net.URI;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/biblio")
@Api(value = "/biblio")
public class BiblioResources {
    public UriInfo uriInfo;
    BiblioService service;

    public BiblioResources(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        this.service = new BiblioService(uriInfo);
    }

    @GET
    @ApiOperation(value = "getBiblio", notes = "read main resource")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Biblio.class)})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EBiblio getBiblio() {
        return new EBiblio(uriInfo.getAbsolutePathBuilder());
    }

    @GET
    @Path("/items")
    @ApiOperation(value = "getItems", notes = "search items")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Items.class)})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Items getItems(
            @ApiParam("The keyword to be used for the search") @QueryParam("keyword") @DefaultValue("") String keyword,
            @ApiParam("The type to be used for the search") @QueryParam("type") @DefaultValue("ALL") SearchScope type,
            @ApiParam("The year before which items are searched") @QueryParam("beforeInclusive") @DefaultValue("10000") int beforeInclusive,
            @ApiParam("The year after which items are searched") @QueryParam("afterInclusive") @DefaultValue("0") int afterInclusive,
            @ApiParam("The page of results to be read") @QueryParam("page") @DefaultValue("1") int page) {
        try {
            return service.getItems(type, keyword, beforeInclusive, afterInclusive, BigInteger.valueOf(page));
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/items/articles")
    @ApiOperation(value = "getArticles", notes = "search articles")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Items.class)})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Items getArticles(
            @ApiParam("The keyword to be used for the search") @QueryParam("keyword") String keyword,
            @ApiParam("The year before which items are searched") @QueryParam("beforeInclusive") @DefaultValue("10000") int beforeInclusive,
            @ApiParam("The year after which items are searched") @QueryParam("afterInclusive") @DefaultValue("0") int afterInclusive,
            @ApiParam("The page of results to be read") @QueryParam("page") @DefaultValue("1") int page) {
        if (keyword == null)
            throw new BadRequestException("keyword is required");
        try {
            return service.getItems(SearchScope.ARTICLES, keyword, beforeInclusive, afterInclusive, BigInteger.valueOf(page));
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/items/books")
    @ApiOperation(value = "getBooks", notes = "search books")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Items.class)})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Items getBooks(
            @ApiParam("The keyword to be used for the search") @QueryParam("keyword") String keyword,
            @ApiParam("The year before which items are searched") @QueryParam("beforeInclusive") @DefaultValue("10000") int beforeInclusive,
            @ApiParam("The year after which items are searched") @QueryParam("afterInclusive") @DefaultValue("0") int afterInclusive,
            @ApiParam("The page of results to be read") @QueryParam("page") @DefaultValue("1") int page) {
        if (keyword == null)
            throw new BadRequestException("keyword is required");
        try {
            return service.getItems(SearchScope.BOOKS, keyword, beforeInclusive, afterInclusive, BigInteger.valueOf(page));
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @POST
    @Path("/items")
    @ApiOperation(value = "createItem", notes = "create a new item", response = Item.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = Item.class),
            @ApiResponse(code = 400, message = "Bad Request")})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createItem(Item item) {
        try {
            Item returnItem = service.createItem(item);
            return Response.created(new URI(returnItem.getSelf())).entity(returnItem).build();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/items/{id}")
    @ApiOperation(value = "getItem", notes = "read a single item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Item.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Item getItem(@ApiParam("The id of the item") @PathParam("id") BigInteger id) {
        Item item;
        try {
            item = service.getItem(id);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (item == null)
            throw new NotFoundException();
        return item;
    }

    @PUT
    @Path("/items/{id}")
    @ApiOperation(value = "updateItem", notes = "update a single item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Item.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Item updateItem(@ApiParam("The id of the item") @PathParam("id") BigInteger id, Item item) {
        Item updated;
        try {
            updated = service.updateItem(id, item);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (updated == null)
            throw new NotFoundException();
        return updated;
    }

    @DELETE
    @Path("/items/{id}")
    @ApiOperation(value = "deleteItem", notes = "delete a single item")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict (item is cited)")})
    public void deleteItem(@ApiParam("The id of the item") @PathParam("id") BigInteger id) {
        BigInteger ret;
        try {
            ret = service.deleteItem(id);
        } catch (ConflictServiceException e) {
            throw new ClientErrorException(409);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (ret == null)
            throw new NotFoundException();
    }

    @GET
    @Path("/items/{id}/citedBy")
    @ApiOperation(value = "getItemCitedBy", notes = "read the items citing an item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Items.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Items getItemCitedBy(@ApiParam("The id of the item for which citing items have to be read") @PathParam("id") BigInteger id) {
        Items items;
        try {
            items = service.getItemCitedBy(id);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (items == null)
            throw new NotFoundException();
        return items;
    }

    @GET
    @Path("/items/{id}/citations/targets")
    @ApiOperation(value = "getItemCitations", notes = "read the target items of the citations from an item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Items.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Items getItemCitations(@ApiParam("The id of the item from which citations are considered") @PathParam("id") BigInteger id) {
        Items items;
        try {
            items = service.getItemCitations(id);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (items == null)
            throw new NotFoundException();
        return items;
    }

    @GET
    @Path("/items/{id}/citations/{tid}")
    @ApiOperation(value = "getItemCitation", notes = "read a citation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Citation.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Citation getItemCitation(
            @ApiParam("The id of the citing item of this citation") @PathParam("id") BigInteger id,
            @ApiParam("The id of the cited item of this citation") @PathParam("tid") BigInteger tid) {
        Citation citation;
        try {
            citation = service.getItemCitation(id, tid);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (citation == null)
            throw new NotFoundException();
        return citation;
    }

    @PUT
    @Path("/items/{id}/citations/{tid}")
    @ApiOperation(value = "createItemCitation", notes = "create a citation", response = Citation.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = Citation.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict")})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createItemCitation(
            @ApiParam("The id of the citing item of this citation") @PathParam("id") BigInteger id,
            @ApiParam("The id of the cited item of this citation") @PathParam("tid") BigInteger tid,
            Citation citation) {
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI u = builder.build();
        UriBuilder fromBuilder = UriBuilder.fromUri(citation.getFrom());
        URI u2 = fromBuilder.path("citations").path(tid.toString()).build();
        if (!u.equals(u2))
            throw new BadRequestException();
        citation.setSelf(u.toString());
        Citation newCitation;
        try {
            newCitation = service.createItemCitation(id, tid, citation);
        } catch (BadRequestServiceException e) {
            throw new BadRequestException();
        } catch (ConflictServiceException e) {
            throw new ClientErrorException(409);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (newCitation == null)
            throw new NotFoundException();
        return Response.created(u).entity(newCitation).build();
    }

    @DELETE
    @Path("/items/{id}/citations/{tid}")
    @ApiOperation(value = "deleteItemCitation", notes = "delete a citation")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "OK", response = Citation.class),
            @ApiResponse(code = 404, message = "Not Found")})
    public void deleteItemCitation(
            @ApiParam("The id of the citing item of this citation") @PathParam("id") BigInteger id,
            @ApiParam("The id of the cited item of this citation") @PathParam("tid") BigInteger tid) {
        boolean success;
        try {
            success = service.deleteItemCitation(id, tid);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        if (!success)
            throw new NotFoundException();
        return;
    }


    @GET
    @Path("/bookshelves")
    @ApiOperation(value = "getBookshelves", notes = "search bookshelves matching the given name", response = Bookshelves.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Bookshelves getBookshelves(@ApiParam("The name to be used for the search") @QueryParam("name") @DefaultValue("") String name) {
        try {
            return service.getBookshelves(name);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @POST
    @Path("/bookshelves")
    @ApiOperation(value = "createBookshelf", notes = "create a new bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = Bookshelf.class),
            @ApiResponse(code = 400, message = "Bad Request")})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createBookshelf(BookshelfCreateResource bookshelfCreateResource) {
        try {
            Bookshelf returnBookshelf = service.createBookshelf(bookshelfCreateResource);
            return Response.created(new URI(returnBookshelf.getSelf())).entity(returnBookshelf).build();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/bookshelves/{bid}")
    @ApiOperation(value = "getBookshelf", notes = "read a single bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Bookshelf.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Bookshelf getBookshelf(
            @ApiParam("The id of the bookshelf") @PathParam("bid") BigInteger bid) {
        try {
            return service.getBookshelf(bid);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @DELETE
    @Path("/bookshelves/{bid}")
    @ApiOperation(value = "deleteBookshelf", notes = "delete a single bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public void deleteBookshelf(
            @ApiParam("The id of the bookshelf") @PathParam("bid") BigInteger bid) {
        try {
            service.deleteBookshelf(bid);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/bookshelves/{bid}/counter")
    @ApiOperation(value = "getBookshelfCounter", notes = "read the counter of a bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = long.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.TEXT_PLAIN})
    public long getBookshelfCounter(
            @ApiParam("The id of the bookshelf") @PathParam("bid") BigInteger bid) {
        try {
            return service.getBookshelfCounter(bid);
        } catch (NullPointerException e) {
            throw new NotFoundException("The bookshelf " + bid + " doesn't exist");
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/bookshelves/{bid}/items")
    @ApiOperation(value = "getBookshelfItems", notes = "read items of the specified bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Items.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Items getBookshelfItems(@ApiParam("The id of the bookshelf") @PathParam("bid") BigInteger bid) {
        try {
            return service.getBookshelfItems(bid);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    /**
     * not sure about the id as query parameter, possibly better in the body
     * PUT /bookshelves/{bid}/items/{id} not possible because there will be an empty body
     */
    @POST
    @Path("/bookshelves/{bid}/items")
    @ApiOperation(value = "addItemToBookshelf", notes = "add item to the specified bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Item.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not Found")})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addItemToBookshelf(
            @ApiParam("The id of the bookshelf") @PathParam("bid") BigInteger bid,
            @ApiParam("The id of the item to be added to the bookshelf") @QueryParam("id") BigInteger id) {
        try {
            Item item = service.addBookshelfItem(bid, id);
            return Response.ok(new URI(item.getSelf())).entity(item).build();
        } catch (NotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/bookshelves/{bid}/items/{id}")
    @ApiOperation(value = "getBookshelfItem", notes = "read the specified item belonging to the specified bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Item.class),
            @ApiResponse(code = 404, message = "Not Found")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Item getBookshelfItem(@ApiParam("The id of the bookshelf") @PathParam("bid") BigInteger bid,
                                 @ApiParam("The id of the item to be added to the bookshelf") @PathParam("id") BigInteger id) {
        try {
            return service.getBookshelfItem(bid, id);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @DELETE
    @Path("/bookshelves/{bid}/items/{id}")
    @ApiOperation(value = "deleteBookshelfItem", notes = "delete a single item from the specified bookshelf")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not Found")})
    public void deletBookshelfItem(@ApiParam("The id of the bookshelf") @PathParam("bid") BigInteger bid,
                                   @ApiParam("The id of the item to be added to the bookshelf") @PathParam("id") BigInteger id) {
        try {
            service.deleteBookshelfItem(bid, id);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }
}
