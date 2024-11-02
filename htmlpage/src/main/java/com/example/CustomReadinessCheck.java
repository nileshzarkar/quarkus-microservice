package com.example;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class CustomReadinessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        // Add custom readiness logic, like checking database connectivity
        return HealthCheckResponse.up("Readiness check");
    }
}
