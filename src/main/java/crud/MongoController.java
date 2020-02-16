package crud;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.bson.conversions.Bson;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Controller("/")
@Validated
public class MongoController {

    private final MongoClient mongoClient;

    public MongoController(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Put("/servers")
    @Operation(summary = "Add a new server to db",
            description = "This endpoint is used to add details of a new server to the mongodb that is configured"
    )
    public HttpResponse save(@Parameter(description="The name of the server") @NotBlank String name, @Parameter(description="The id of the server") @NotBlank Integer id, @Parameter(description="The name of the language used") @NotBlank String language, @Parameter(description="The name of the framework used") @NotBlank String framework) {
        try{
            Server server = new Server(name, id, language, framework);
            Single<Server> carrier = Single
                    .fromPublisher(getCollection().insertOne(server))
                    .map(success -> server);
            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
    }

    @Get("/getall")
    @Operation(summary = "Get all the servers and their details",
            description = "This endpoint is used to get all the existing servers in the database along with their details"
    )
    public HttpResponse getall() {
        try{
            Single<List<Server>> carrier = Flowable
                    .fromPublisher(getCollection().find()).toList();
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
    public HttpResponse find(@Parameter(description="The name of the server") @NotBlank @QueryValue String name) {
        try{
            Maybe<Server> carrier = Flowable.fromPublisher(
                    getCollection()
                            .find(eq("name", name))
                            .limit(1)
            ).firstElement();

            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
    }

    @Delete("/delete")
    @Operation(summary = "Delete a specific server and its details given its id",
            description = "This endpoint is used to delete a specific server in the database given its id"
    )
    public HttpResponse del(@Parameter(description="The id of the server") @NotBlank @QueryValue Integer id){
        try{
            Bson filter = Filters.eq("_id", id);
            Flowable<DeleteResult> carrier = Flowable.fromPublisher(getCollection().deleteOne(filter));
            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
    }

    private MongoCollection<Server> getCollection() {
        return mongoClient
                .getDatabase("micronaut")
                .getCollection("servers", Server.class);
    }
}