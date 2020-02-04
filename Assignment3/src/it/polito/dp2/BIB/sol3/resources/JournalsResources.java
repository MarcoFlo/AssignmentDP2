package it.polito.dp2.BIB.sol3.resources;

import io.swagger.annotations.*;
import it.polito.dp2.BIB.sol3.service.JournalsService;
import it.polito.dp2.BIB.sol3.service.jaxb.Journal;
import it.polito.dp2.BIB.sol3.service.jaxb.Journals;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * extra, not completed/correct
 */
@Path("/biblio")
@Api(value = "/biblio")
public class JournalsResources {
    private final static String PAGE_SIZE = "20";
    public UriInfo uriInfo;
    JournalsService service;

    public JournalsResources(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        this.service = new JournalsService(uriInfo);
    }

    @GET
    @Path("/journals")
    @ApiOperation(value = "getJournals", notes = "search journals")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Journals.class)})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Journals getJournals(
            @ApiParam("The keyword to be used for the search") @QueryParam("keyword") @DefaultValue("") String keyword,
            @ApiParam("The page number to be read") @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @ApiParam("The page size") @QueryParam("pageSize") @DefaultValue(JournalsResources.PAGE_SIZE) int pageSize) {
        try {
            return service.getJournals(keyword, pageNumber, pageSize);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }


    @PUT
    @Path("/journals/{issn}")
    @ApiOperation(value = "createUpdateJournals", notes = "create/update a journal")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict")})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createUpdateJournal(
            @ApiParam("The issn of the journal") @PathParam("issn") String issn, Journal journal) {
        if (!journal.getISSN().equals(issn))
            throw new ClientErrorException("The issn " + journal.getISSN() + " doesn't match the one of the original journal", Response.status(Response.Status.CONFLICT)
                    .entity("The issn " + journal.getISSN() + " doesn't match the one of the original journal")
                    .build());
        try {
            return service.createUpdateJournal(journal);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }
}
