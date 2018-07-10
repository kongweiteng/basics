package com.enn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    /*@Value("${zuul.prefix}")
    private String prefix;

    @Value("${zuul.routes.energy-provider-business}")
    private String business;

    @Value("${zuul.routes.energy-provider-system}")
    private String system;
*/
    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("业务-business", "/energy-proxy/energy-provider-business/v2/api-docs", "2.0"));
        resources.add(swaggerResource("系统管理-system", "/energy-proxy/energy-provider-system/v2/api-docs", "2.0"));
        resources.add(swaggerResource("系统管理-passage", "/energy-proxy/energy-provider-passage/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}