package crud;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.QueryValue;

import javax.validation.constraints.NotBlank;

public interface InterfaceForMethods {

    HttpResponse addServers(@NotBlank String name, @NotBlank Integer id, @NotBlank String language, @NotBlank String framework);

    HttpResponse returnServers();

    HttpResponse filterServers(@NotBlank String name);

    HttpResponse deleteServers(@NotBlank Integer id);

}
