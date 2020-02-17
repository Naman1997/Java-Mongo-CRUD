package crud;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;

@Controller("/")
@Singleton
@Validated
public class MongoController {

    final protected InterfaceForMethods interfaceForMethods;

    public MongoController(InterfaceForMethods interfaceForMethods) {
        this.interfaceForMethods = interfaceForMethods;
    }

    @Put("/servers")
    @Operation(summary = "Add a new server to db",
            description = "This endpoint is used to add details of a new server to the mongodb that is configured")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Server.class),
            examples = @ExampleObject(value = "{\n" +
            "    \"name\": \"fedora\",\n" +
            "    \"id\": 200,\n" +
            "    \"language\": \"Rust\",\n" +
            "    \"framework\": \"Revel\"\n" +
            "}")),responseCode = "200", description = "New Server Added. Returns the server that is added.")
    @ApiResponse(content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\n" +
            "    \"message\": \"Required argument [String framework] not specified\",\n" +
            "    \"path\": \"/framework\",\n" +
            "    \"_links\": {\n" +
            "        \"self\": {\n" +
            "            \"href\": \"/servers\",\n" +
            "            \"templated\": false\n" +
            "        }\n" +
            "    }\n" +
            "}")),responseCode = "400", description = "Invalid Request Error like insufficient parameters passed")
    @ApiResponse(content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\n" +
            "    \"message\": \"Internal Server Error: E11000 duplicate key error collection: micronaut.servers index: _id_ dup key: { _id: 111 }\"\n" +
            "}")),
            responseCode = "500", description = "Server Errors like duplicate keys")
    public HttpResponse save(@NotBlank String name, @NotBlank Integer id, @NotBlank String language, @NotBlank String framework) {
        return interfaceForMethods.addServers(name, id, language, framework);
    }

    @Get("/getall")
    @Operation(summary = "Get all the servers and their details",
            description = "This endpoint is used to get all the existing servers in the database along with their details"
    )
    @ApiResponse(content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "[\n" +
                    "    {\n" +
                    "        \"name\": \"Arch\",\n" +
                    "        \"id\": 1,\n" +
                    "        \"language\": \"Python\",\n" +
                    "        \"framework\": \"Flask\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"name\": \"Mint\",\n" +
                    "        \"id\": 2,\n" +
                    "        \"language\": \"Ruby\",\n" +
                    "        \"framework\": \"Rails\"\n" +
                    "    }"+"\n"+"]")),responseCode = "200", description = "Servers found. Returns all servers.")
    public HttpResponse getall() {
        return interfaceForMethods.returnServers();
    }

    @Get("/getbyname")
    @Operation(summary = "Get a specific server and its details given its name",
            description = "This endpoint is used to get all the existing servers whose name matches a name provided"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Server.class),
            examples = @ExampleObject(value = "[\n" +
                    "    {\n" +
                    "        \"name\": \"Arch\",\n" +
                    "        \"id\": 1,\n" +
                    "        \"language\": \"Python\",\n" +
                    "        \"framework\": \"Flask\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"name\": \"Arch\",\n" +
                    "        \"id\": 3,\n" +
                    "        \"language\": \"Java\",\n" +
                    "        \"framework\": \"Micronaut\"\n" +
                    "    }\n" +
                    "]")),responseCode = "200", description = "Servers found. Returns filtered servers.")
    @ApiResponse(content = @Content(mediaType = "text/plain",
            examples = @ExampleObject(value = "Not found any records")),responseCode = "400", description = "No servers found.")
    public HttpResponse find(@Parameter(description="The name of the server") @NotBlank @QueryValue String name) {
        return interfaceForMethods.filterServers(name);
    }

    @Delete("/delete")
    @Operation(summary = "Delete a specific server and its details given its id",
            description = "This endpoint is used to delete a specific server in the database given its id"
    )
    @ApiResponse(content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = "{\n" +
                    "    \"deletedCount\": 1\n" +
                    "}")),responseCode = "200", description = "Server found and deleted")
    @ApiResponse(content = @Content(mediaType = "text/plain",
            examples = @ExampleObject(value = "Not found any records")),responseCode = "400", description = "No server with the specified ID found.")
    public HttpResponse del(@Parameter(description="The id of the server") @NotBlank @QueryValue Integer id){
        return interfaceForMethods.deleteServers(id);
    }
}