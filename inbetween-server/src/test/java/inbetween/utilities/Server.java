package inbetween.utilities;

import inbetween.InBetweenApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class Server extends InBetweenApplication {

    public static void main(String[] args) {
        new Server().configure(new SpringApplicationBuilder())
                .initializers()
                .profiles("local")
                .run(args);
    }

}