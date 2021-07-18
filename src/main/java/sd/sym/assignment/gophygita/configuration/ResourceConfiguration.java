package sd.sym.assignment.gophygita.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import static sd.sym.assignment.gophygita.constant.ApplicationConstant.APPLICATION_PROPERTIES;

@Configuration
public class ResourceConfiguration extends ResourceManagement {

    @Autowired
    public ResourceConfiguration() {
        super(APPLICATION_PROPERTIES);
    }
}