package lk.ijse.apigateway;

import lk.ijse.apigateway.filter.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AuthenticationFilter filter) {
        return builder.routes()
                // Protect the Crop routes
                .route("crop-inventory-service", r -> r
                        .path("/api/crops", "/api/crops/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://CROP-INVENTORY-SERVICE"))
                // Protect the Zone routes
                .route("zone-management-service", r -> r
                        .path("/api/zones", "/api/zones/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://ZONE-MANAGEMENT-SERVICE"))
                // Protect the Sensor routes
                .route("sensor-telemetry-service", r -> r
                        .path("/api/sensors", "/api/sensors/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://SENSOR-TELEMETRY-SERVICE"))
                // Protect the Automation routes
                .route("automation-control-service", r -> r
                        .path("/api/automation", "/api/automation/**")
                        .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://AUTOMATION-CONTROL-SERVICE"))
                .build();
    }
}