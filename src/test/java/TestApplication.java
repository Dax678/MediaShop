import org.example.mediashop.MediaShopApplication;
import org.example.mediashop.TestConfig.ContainersConfig;
import org.springframework.boot.SpringApplication;

public class TestApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(MediaShopApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
