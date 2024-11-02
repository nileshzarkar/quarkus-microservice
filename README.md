# quarkus-microservice

There are 2 microservices currency and exchange
currency is to configure the exchange rate between 2 currencies.
exchange is to convert the given currency to another currency.

POST http://localhost:8080/currency/exchange-rate
{"fromCurrency": "USD", "toCurrency": "EUR", "rate": 0.85}

GET http://localhost:8080/currency/exchange-rate/USD/EUR

POST http://localhost:8081/exchange/USD/EUR/1
POST http://localhost:8081/exchange/USD/EUR/2

Note while running usinf mvn quarkus:dev : 
@RegisterRestClient(baseUri = "http://localhost:8080")  // Adjust this to the actual Currency Service URL

git config --global core.autocrlf false

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

# Set Quarkus to expose port 8081 or what port you want to expose in the Dockerfile
quarkus.container-image.expose=true
quarkus.container-image.ports=8081 ??

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
or

docker tag niles/htmlpage:1.0.0 nileshzarkar/htmlpage:1.0.0
docker push nileshzarkar/htmlpage:1.0.0


=========================


Note while running using docker-compose :
@RegisterRestClient(baseUri = "http://currency-service:8080")  // Adjust this to the actual Currency Service URL

mp.rest.client.CurrencyServiceClient.url=http://currency-service:8080

version: '3.8'
services:
  currency-service:
    image: niles/currency:1.0.0
    ports:
      - "8080:8080"   # Expose currency-service on port 8080
    networks:
      - currency-exchange-network

  exchange-service:
    image: niles/exchange:1.0.0
    ports:
      - "8081:8081"   # Expose exchange-service on port 8081
    environment:
      - MP_REST_CLIENT_CURRENCYSERVICECLIENT_URL=http://currency-service:8080  # Change to use service name
    networks:
      - currency-exchange-network

networks:
  currency-exchange-network:
    driver: bridge


docker-compose up

=========================

kubectl config get-contexts
kubectl config use-context docker-desktop
kubectl get nodes




currency-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: currency-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: currency
  template:
    metadata:
      labels:
        app: currency
    spec:
      containers:
        - name: currency-container
          image: niles/currency:1.0.0  # Replace with your actual image name
          ports:
            - containerPort: 8080

---
apiVersion: v1
kind: Service
metadata:
  name: currency-service
spec:
  selector:
    app: currency
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
      nodePort: 30080  # Expose port 30080 on the node (localhost)
  type: NodePort


exchange-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: exchange-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: exchange
  template:
    metadata:
      labels:
        app: exchange
    spec:
      containers:
        - name: exchange-container
          image: niles/exchange:1.0.0  # Replace with your actual image name
          ports:
            - containerPort: 8081
          env:
            - name: MP_REST_CLIENT_CURRENCYSERVICECLIENT_URL
              value: "http://currency-service:8080"  # Use the Kubernetes service name to communicate with currency

---
apiVersion: v1
kind: Service
metadata:
  name: exchange-service
spec:
  selector:
    app: exchange
  ports:
    - protocol: TCP
      port: 8081
      targetPort: 8081
      nodePort: 30081  # Expose port 30081 on the node (localhost)
  type: NodePort


kubectl apply -f currency-deployment.yaml
kubectl apply -f .\exchange-deployment.yaml 

kubectl delete -f currency-deployment.yaml
kubectl delete -f .\exchange-deployment.yaml 

POST http://localhost:30080/currency/exchange-rate
{"fromCurrency": "USD", "toCurrency": "EUR", "rate": 0.85}

GET http://localhost:30080/currency/exchange-rate/USD/EUR

POST http://localhost:30081/exchange/USD/EUR/1
POST http://localhost:30081/exchange/USD/EUR/2



==========================

```t

n Helm, "chart" and "release" have distinct meanings:

    Chart:
        A chart is a Helm package that contains all the resource definitions necessary to deploy an application in Kubernetes.
        It includes templates, configuration files, and metadata required for deploying a service or application.
        A chart is similar to a blueprint. It’s reusable and can be shared or versioned, allowing you to install or update applications across multiple environments consistently.

    Release:
        A release is an instance of a chart that has been deployed to a Kubernetes cluster.
        When you run helm install, Helm takes the chart and deploys it, creating a release.
        Multiple releases can be created from the same chart, allowing you to deploy multiple instances of the application, each with its own configuration.
        Each release has a unique name and tracks the history of changes, allowing rollbacks or upgrades specific to that instance.

In short:

    Chart = Template or package for deployment.
    Release = A specific instance of that chart deployed on a Kubernetes cluster.

Below config optional:   
kubectl create secret docker-registry regcred --docker-server=https://index.docker.io/v1/ --docker-username=nileshzarkar --docker-password=maheshx@91 --docker-email=nileshzarkar@gmail.com

helm create htmlpage



Update Chart.yaml
apiVersion: v2
name: htmlpage
description: A Helm chart for Kubernetes
type: application
version: 1.0.0
appVersion: "1.0.0"



Update values.yaml
replicaCount: 1
image:
  repository: nileshzarkar/htmlpage
  pullPolicy: Always
  tag: "1.0.0"

Below config optional:   
imagePullSecrets:  
  - name: regcred
...
Below config optional:   
serviceAccount:  
  create: false
  automount: false
  annotations: {}
  name: ""
...
service:
  type: NodePort
  port: 8082
  targetPort: 8082
  nodePort: 30082
config:
  pageColor: "red"
ingress:
...



Update deployment.yaml
...
          ports:
            - name: http
              containerPort: {{ .Values.service.port }}
              protocol: TCP
          env:
            - name: page.color
              value: {{ .Values.config.pageColor }}     
          livenessProbe:
            {{- toYaml .Values.livenessProbe | nindent 12 }}
...



Update service.yaml
apiVersion: v1
kind: Service
...
spec:
  type: {{ .Values.service.type }}
  ports:
    - port: {{ .Values.service.port }}
      targetPort: {{ .Values.service.targetPort }}
      nodePort: {{ .Values.service.nodePort }}
      protocol: TCP
      name: http
...



htmlpage\htmlpage> helm install htmlpage .

htmlpage\htmlpage> helm uninstall htmlpage .
```


























=====================

To monitor your microservices in Kubernetes using Prometheus on Docker Desktop, you can follow these steps. Helm makes it easier to install and manage Prometheus in your Kubernetes environment.
Steps to Set Up Prometheus Using Helm:
1. Ensure Helm is Installed

First, ensure you have Helm installed. You can verify by running:

helm version
Add below dependencies to curency and exchange microservices and create new image 
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>


2. Add the Prometheus Helm Chart Repository

Add the official Prometheus Helm chart repository to Helm:

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

This adds the repository that contains Prometheus charts.

3. Install Prometheus Using Helm

Install Prometheus in your Kubernetes cluster using the following Helm command:

helm install prometheus prometheus-community/prometheus

This will install Prometheus using the default configuration. The release name is prometheus, and the Prometheus components (server, alert manager, etc.) will be deployed in your Kubernetes cluster.

4. Verify Prometheus Deployment

You can check if Prometheus was successfully deployed by listing the pods and services:

kubectl get pods

This should show several Prometheus components, such as prometheus-server, prometheus-alertmanager, etc.

kubectl get svc

You should see a service named prometheus-server, which exposes the Prometheus UI.

5. Access the Prometheus UI

Since you are using Docker Desktop, you can use port-forwarding to access the Prometheus UI from your local machine.

To forward port 9090 (Prometheus default UI port) to your local machine, run:
kubectl edit svc prometheus-server
ports:
  - protocol: TCP
    port: 9090
    targetPort: 9090

kubectl port-forward service/prometheus-server 9090:9090

Now, you can access the Prometheus UI in your browser by going to:

http://localhost:9090

6. Configure Prometheus to Scrape Your Microservices

Prometheus needs to be configured to scrape metrics from your microservices. By default, Quarkus exposes metrics at /q/metrics, but you need to tell Prometheus where to find these endpoints.

To do this, you'll need to modify the Prometheus prometheus.yml configuration file. In Helm, you can override the default configuration using values when installing or updating the Helm chart.

To scrape your services, create a file prometheus-values.yaml with the following content:

serverFiles:
  prometheus.yml:
    scrape_configs:
      - job_name: 'currency-service'
	    metrics_path: '/q/metrics'
        static_configs:
          - targets: ['currency-service:8080']

      - job_name: 'exchange-service'
	    metrics_path: '/q/metrics'
        static_configs:
          - targets: ['exchange-service:8081']

The above configuration tells Prometheus to scrape the currency-service at port 8080 and the exchange-service at port 8081.

7. Apply the Custom Configuration

To apply this configuration, run the following command using Helm:
kubectl edit svc prometheus-server
server:
  service:
    ports:
      - name: web  # Rename the port to something unique
        port: 9090
        targetPort: 9090

helm upgrade --install prometheus prometheus-community/prometheus -f prometheus-values.yaml

This will update the Prometheus installation with the custom scrape configuration, so it starts monitoring your microservices.

8. Verify Metrics Collection

Once you've applied the configuration, go back to the Prometheus UI (http://localhost:9090) and go to the Targets page (http://localhost:9090/targets). You should see your currency-service and exchange-service listed as active targets.

You can also query for specific metrics using the Graph tab in Prometheus. Quarkus exposes many built-in metrics, such as:

    http_server_requests_seconds_count (number of HTTP requests)
    jvm_memory_used_bytes (JVM memory usage)

You can start querying these metrics to monitor your services.









