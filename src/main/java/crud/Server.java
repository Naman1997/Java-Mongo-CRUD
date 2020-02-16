package crud;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="Server", description="Server description")
public class Server {

    private String name;
    private int id;
    private String language;
    private String framework;

    public Server(){

    }

    public Server(String name, Integer id, String language, String framework){
        this.name = name;
        this.id = id;
        this.language = language;
        this.framework = framework;
    }

    @Schema(description="Server name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(description="Server id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Schema(description="Server language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Schema(description="Server framework")
    public String getFramework() {
        return framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

}
