package exercise.daytime;
import jakarta.annotation.PostConstruct;


public class Night implements Daytime {

    private String message;
    private String name = "night";

    public String getName() {
        return name;
    }

    @PostConstruct
    public void init() {
        this.message = "Bean NIGHT is initialized!";
        System.out.println(message);
    }
}
