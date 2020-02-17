package crud;

import Database.Connection;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Controller("/")
@Singleton
@Validated
public class MongoController {


    private final Connection connection;

    public MongoController(Connection connection) {
        this.connection = connection;
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
    public HttpResponse save(@Parameter(description="The name of the server") @NotBlank String name, @Parameter(description="The id of the server") @NotBlank Integer id, @Parameter(description="The name of the language used") @NotBlank String language, @Parameter(description="The name of the framework used") @NotBlank String framework) {
        try{
            Server server = new Server(name, id, language, framework);
            Single<Server> carrier = Single
                    .fromPublisher(connection.getCollection().insertOne(server))
                    .map(success -> server);
            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e.getMessage());
        }
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
        try{
            Single<List<Server>> carrier = Flowable
                    .fromPublisher(connection.getCollection().find()).toList();
            return HttpResponse.ok().body(carrier);

        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
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
        try{

            Document myDoc = connection.getConn().find(eq("name", name)).first();

            if (myDoc == null) {
                return HttpResponse.notFound().body("Not found any records");
            }

            Single<List<Server>> carrier = Flowable
                    .fromPublisher(connection.getCollection().find(eq("name", name))).toList();
            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e.getMessage());
        }
    }

    @Delete("/delete")
    @Operation(summary = "Delete a specific server and its details given its id",
            description = "This endpoint is used to delete a specific server in the database given its id"
    )
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Server.class),
            examples = @ExampleObject(value = "Deleted server with ID=111")),responseCode = "200", description = "Server found and deleted")
    @ApiResponse(content = @Content(mediaType = "text/plain",
            examples = @ExampleObject(value = "Not found any records")),responseCode = "400", description = "No server with the specified ID found.")
    public HttpResponse del(@Parameter(description="The id of the server") @NotBlank @QueryValue Integer id){
        try{

            Document myDoc = connection.getConn().find(eq("_id", id)).first();

            if (myDoc == null) {
                return HttpResponse.notFound().body("Not found any records");
            }

            Bson filter = Filters.eq("_id", id);
            Flowable<DeleteResult> carrier = Flowable.fromPublisher(connection.getCollection().deleteOne(filter));
            return HttpResponse.ok().body("Deleted server with ID="+id);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
    }


}