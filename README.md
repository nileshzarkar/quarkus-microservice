# quarkus-microservice

There are 2 microservices currency and exchange
currency is to configure the exchange rate between 2 currencies.
exchange is to convert the given currency to another currency.

POST http://localhost:8080/currency/exchange-rate
{"fromCurrency": "USD", "toCurrency": "EUR", "rate": 0.85}

GET http://localhost:8080/currency/exchange-rate/USD/EUR

POST http://localhost:8081/exchange/USD/EUR/1
POST http://localhost:8081/exchange/USD/EUR/2

=========================

Creating a docker image of these microservices without creating a docker file 

Alternatively, if you're using Maven, add the extension manually to your pom.xml:
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-container-image-docker</artifactId>
</dependency>

If you prefer to use other tools like Jib or Buildah, replace container-image-docker with container-image-jib or container-image-buildah

To configure how your Docker image will be generated, you need to define some properties in the application.properties file.

Open the src/main/resources/application.properties file and add the following configurations:

# Application name and version (for tagging the Docker image)
quarkus.application.name=currency
quarkus.application.version=1.0.0

# Docker image build settings
quarkus.container-image.build=true
quarkus.container-image.name=currency
quarkus.container-image.tag=1.0.0

Once you have added the extension and configured the properties, you can build the Quarkus application and the Docker image using the following Maven command:
./mvnw clean package
or 
./mvnw clean package -Dquarkus.container-image.build=true

Once the build is complete, verify that the Docker image was created successfully:
docker images

You can now run the Docker image locally:
docker run -i --rm -p 8080:8080 niles/currency:1.0.0
or
for exchange service
docker run -i --rm -p 8080:8080 niles/exchange:1.0.0

If you want to push the Docker image to a container registry (e.g., Docker Hub or a private registry), you can configure Quarkus to do so by specifying the registry details in the application.properties:
quarkus.container-image.registry=docker.io
quarkus.container-image.group=my-dockerhub-username
or
push the image using:
./mvnw clean package -Dquarkus.container-image.push=true

=========================


