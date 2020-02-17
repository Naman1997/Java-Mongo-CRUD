package crud;

import Database.Connection;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.bson.Document;
import org.bson.conversions.Bson;
import javax.inject.Singleton;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


@Singleton
@Validated
public class MethodImpl implements InterfaceForMethods{

    private final Connection connection;

    public MethodImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public HttpResponse addServers(@NotBlank String name, @NotBlank Integer id, @NotBlank String language, @NotBlank String framework) {
        try{
            Server server = new Server(name, id, language, framework);
            Single<Server> carrier = Single
                    .fromPublisher(connection.getCollection().insertOne(server))
                    .map(success -> server);
            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return io.micronaut.http.HttpResponse.serverError().body(e.getMessage());
        }
    }

    @Override
    public HttpResponse returnServers() {
        try{
            Single<List<Server>> carrier = Flowable
                    .fromPublisher(connection.getCollection().find()).toList();
            return HttpResponse.ok().body(carrier);

        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
    }

    @Override
    public HttpResponse filterServers(@NotBlank String name) {
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

    @Override
    public HttpResponse deleteServers(@NotBlank Integer id) {
        try{

            Document myDoc = connection.getConn().find(eq("_id", id)).first();

            if (myDoc == null) {
                return HttpResponse.notFound().body("Not found any records");
            }

            Bson filter = Filters.eq("_id", id);
            Flowable<DeleteResult> carrier = Flowable.fromPublisher(connection.getCollection().deleteOne(filter));
            return HttpResponse.ok().body(carrier);
        }
        catch (Exception e){
            return HttpResponse.serverError().body(e);
        }
    }
}
