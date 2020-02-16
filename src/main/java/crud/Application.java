package crud;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Kaiburr Api",
                version = "1.0",
                description = "CRUD API to manage a collection in mongodb",
                contact = @Contact(url = "https://naman-portfolio.herokuapp.com/", name = "Naman Arora", email = "naman.arora2016@vitstudent.ac.in")
        )
)

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}