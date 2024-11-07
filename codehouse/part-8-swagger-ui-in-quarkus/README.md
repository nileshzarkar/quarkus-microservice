# part-8-swagger-ui-in-quarkus

PS D:\Experiments\quarkus-microservice\codehouse\part-8-swagger-ui-in-quarkus> mvn quarkus:add-extension -Dextensions="quarkus-smallrye-openapi"


http://localhost:8080/q/swagger-ui


# To enable Swagger UI, add the following configuration in your application.properties file
# quarkus.swagger-ui.enable=true
# To disable Swagger UI, set the following property in your application.properties file:
# quarkus.swagger-ui.enable=false

# If you also want to disable the OpenAPI specification endpoint, add:
# quarkus.smallrye-openapi.enable=false