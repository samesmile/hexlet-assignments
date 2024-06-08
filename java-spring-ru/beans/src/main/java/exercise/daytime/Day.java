package exercise.daytime;
import jakarta.annotation.PostConstruct;


public class Day implements Daytime {

    private String message;
    private String name = "day";

    public String getName() {
        return name;
    }

    @PostConstruct
    public void init() {
        this.message = "Bean DAY is initialized!";
        System.out.println(message);
    }
}
