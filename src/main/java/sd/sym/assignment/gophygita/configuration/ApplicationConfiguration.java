package sd.sym.assignment.gophygita.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Value("${application.url:http://localhost:8080}")
    public String applicationUrl;
}
