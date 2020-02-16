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
            description = "This endpoint is used to add details of a new server to the mongodb that is configured"
    )
    public HttpResponse save(@Parameter(description="The name of the server") @NotBlank String name, @Parameter(description="The id of the server") @NotBlank Integer id, @Parameter(description="The name of the language used") @NotBlank String language, @Parameter(description="The name of the framework used") @NotBlank String framework) {
        try{
            Server server = new Server(name, id, language, framework);
            Single<Server> carrier = Single
                    .fromPublisher(connection.getCollection().insertOne(server))
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
    public HttpResponse find(@Parameter(description="The name of the server") @NotBlank @QueryValue String name) {
        try{

            Document myDoc = connection.getConn().find(eq("name", name)).first();

            if (myDoc == null) {
                return HttpResponse.notFound().body(myDoc);
            }

            Single<List<Server>> carrier = Flowable
                    .fromPublisher(connection.getCollection().find(eq("name", name))).toList();
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
            Flowable<DeleteResult> carrier = Flowable.fromPublisher(connection.getCollection().deleteOne(filter));
            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
    }


}