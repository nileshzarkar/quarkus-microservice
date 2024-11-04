





```t
Helm

13-Helm-Dev-If-Else-EQ
Usecase: 1
values.yaml
...
podLabels:
  environment: dev
...

deployment.yaml
...
          env:
            - name: page.color
              {{- if eq .Values.podLabels.environment "prod" }}
              value: {{ .Values.config.pageColor }}    
              {{- else }}
              value: "blue"
              {{- end }}
          livenessProbe:
...

Usecase: 2
values.yaml
...
podAnnotations: {}
podLabels:
  environment: dev
...

deployment.yaml
...
spec:
  {{- if not .Values.autoscaling.enabled }}
  {{- if eq .Values.podLabels.environment "prod" }}
  replicas: 4
  {{- else if eq .Values.podLabels.environment "qa" }}  
  replicas: 2
  {{- else }}  
  replicas: {{ .Values.replicaCount }}
  {{- end }}  
  {{- end }}
  selector:
...


14-Helm-Dev-If-Else-AND-BOOLEAN
Usecase: 1
values.yaml
...
podAnnotations: {}
podLabels:
  environment: dev
...
autoscaling:
  enabled: true
  # enabled: false
  minReplicas: 1
...

deployment.yaml
...
spec:
  {{- if and .Values.autoscaling.enabled (eq .Values.podLabels.environment "prod") }}
  replicas: 4
  {{- else if eq .Values.podLabels.environment "qa" }}  
  replicas: 2
  {{- else }}  
  replicas: {{ .Values.replicaCount }}
  {{- end }}  
  selector:
...


15-Helm-Dev-If-Else-OR
Usecase: 1
values.yaml
...
podAnnotations: {}
podLabels:
  environment: qa
...

deployment.yaml
...
    {{- include "htmlpage.labels" . | nindent 4 }}
spec:
  {{- if and .Values.autoscaling.enabled (eq .Values.podLabels.environment "prod") }}
  replicas: 4
  {{- else if or (eq .Values.podLabels.environment "testing") (eq .Values.podLabels.environment "qa") }}  
  replicas: 2
  {{- else }}  
  replicas: {{ .Values.replicaCount }}
  {{- end }}  
  selector:
...  


16-Helm-Dev-If-Else-NOT
Usecase: 1
values.yaml
...
podAnnotations: {}
podLabels:
  environment: qa
...

deployment.yaml
...
    {{- include "htmlpage.labels" . | nindent 4 }}
spec:
  {{- if not (eq .Values.podLabels.environment "prod") }}
  replicas: 1
  {{- else }}  
  replicas: {{ .Values.replicaCount }}
  {{- end }}  
  selector:
  ...


17-Helm-Dev-WITH
Usecase: 1
values.yaml
...
podAnnotations:
  appName: htmlDynamicPage
podLabels:
  environment: qa
...

deployment.yaml
...
  template:
    metadata:
      {{- with .Values.podAnnotations }}
      annotations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      labels:
...

Usecase: 2
Try to access any Root Object in "with" action block
values.yaml
...
podAnnotations:
  appName: htmlDynamicPage
podLabels:
  environment: qa
...

deployment.yaml
...
  template:
    metadata:
      {{- with .Values.podAnnotations }}
      annotations:
        {{- toYaml . | nindent 8 }}
        appManagedBy: {{ .Release.Service }}
      {{- end }}
      labels:
...
Error: template: htmlpage/templates/deployment.yaml:23:33: executing "htmlpage/templates/deployment.yaml" at <.Release.Service>: nil pointer evaluating interface {}.Service

Usecase: 3
Try to access any Root Object in "with" action block with $
values.yaml
...
podAnnotations:
  appName: htmlDynamicPage
podLabels:
  environment: qa
...

deployment.yaml
...
  template:
    metadata:
      {{- with .Values.podAnnotations }}
      annotations:
        {{- toYaml . | nindent 8 }}
        appManagedBy: {{ $.Release.Service }}
      {{- end }}
      labels:
...


18-Helm-Dev-WITH-If-Else
Usecase: 1 
This case is only with WITH and no if-else (Retrieve single object using scope Part of 17-Helm-Dev-WITH)
values.yaml
...
service:
  type: NodePort
  port: 8082
  nodePort: 30082
  targetPort: 8082
...

service.yaml
...
spec:
  {{- with .Values.service }}
  type: {{ .type }}
  ports:
    - port: {{ .port }}
      targetPort: {{ .targetPort }}
      nodePort: {{ .nodePort }}
      protocol: TCP
      name: http
  {{- end }}    
  selector:
...


19-Helm-Dev-Variables
Usecase: 1
deployment.yaml
{{- $chartname := .Chart.Name | quote | upper -}}
apiVersion: apps/v1
kind: Deployment
...
   annotations:
        {{- toYaml . | nindent 8 }}
        appManagedBy: {{ $.Release.Service }}
        appHelmChart: {{ $chartname }}
      {{- end }}
      labels:
...


20-Helm-Dev-Range-List
Usecase: 1
values.yaml
...
namespaces:
  - name: namespace1
  - name: namespace2
  - name: namespace3
...

namespaces.yaml
{{- range .Values.namespaces}}
apiVersion: v1
kind: Namespace
metadata:
  name: {{ .name }}
---
{{- end }}  

Usecase: 2
values.yaml
...
environments:
  - name: dev
  - name: qa
  - name: uat  
  - name: prod
...

namespace-with-variable.yaml
{{- range $environment := .Values.environments}}
apiVersion: v1
kind: Namespace
metadata:
  name: {{ $environment.name }}
---
{{- end }}  


21-Helm-Dev-Range-Dict
Usecase: 1
values.yaml
...
volumes: []

volumeMounts:
  - name: config-volume
    mountPath: /app/config
  - name: data-volume
    mountPath: /app/data

nodeSelector: {}
...

deployment.yaml
...
          volumeMounts:
            {{- range .Values.volumeMounts }}
            - name: {{ .name }}
              mountPath: {{ .mountPath }}
            {{- end }}
      volumes:
        - name: config-volume
          emptyDir: {}
        - name: data-volume
          emptyDir: {}
      {{- with .Values.nodeSelector }}
      nodeSelector:
...

Usecase: 2
values.yaml
...
env:
  - name: APP_ENV
    value: "production"
  - name: LOG_LEVEL
    value: "info"
  - name: FEATURE_FLAG
    value: "true"  
...

deployment.yaml
...
          env:
            - name: page.color
              {{- if eq .Values.podLabels.environment "prod" }}
              value: {{ .Values.config.pageColor }}    
              {{- else }}
              value: "blue"
              {{- end }}
              {{- range .Values.env }}
              name: {{ .name }}
              value: {{ .value | quote }}
              {{- end }}
          livenessProbe:
...


23-Helm-Dev-Printf-Function
Usecase: 1
Creating a Custom Label with a Dynamic Value
Suppose you want to create a label that includes the chart name and version from your Chart.yaml

deployment.yaml
...
metadata:
  name: {{ include "htmlpage.fullname" . }}
  labels:
    app.kubernetes.io/name: {{ .Chart.Name }}
    app.kubernetes.io/version: {{ printf "%s-%s" .Chart.Name .Chart.Version }}
spec:
...

Usecase: 2
Padding a Number with Zeros
You might want to format a replica number with leading zeros, such as "001" instead of just 1.

values.yaml
...
replicaIndex: 1
...

configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ printf "%s-config-%03d" .Chart.Name (int .Values.replicaIndex) }}
data:
  key: "someValue"

Usecase: 3
Formatting a URL with printf
Suppose you want to create a URL string that includes a service name and port.

values.yaml
...
service:
  type: NodePort
  port: 8082
  nodePort: 30082
  targetPort: 8082
  name: my-htmlpage-service
...

deployment.yaml
...
          env:
  ...
              name: SERVICE_URL
              value: {{ printf "http://%s:%d" .Values.service.name (int .Values.service.port) }}
...

Usecase: 4
Adding Line Breaks in a ConfigMap
Sometimes you may need to include formatted multiline strings in a ConfigMap or Secret.

configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ printf "%s-config-%03d" .Chart.Name (int .Values.replicaIndex) }}
data:
  config: |
    {{ printf "appName: %s\n    appVersion: %s" .Chart.Name .Chart.Version }}


24-Helm-Dev-call-template-in-template
In Helm, you can define templates within other templates using define and template functions. This approach is useful when you have reusable components or want to keep your templates DRY (Don't Repeat Yourself).
Here’s an example where we define a reusable label template within another template in Helm:
Example Setup
Suppose you want to standardize your labels across multiple Kubernetes resources, like Deployment, Service, etc., so that they all use the same labels structure.
Step 1: Define the Reusable Labels Template in _helpers.tpl
In Helm, the _helpers.tpl file is commonly used for defining reusable templates. You can create a labels template that other templates can call.

_helpers.tpl:
{{- define "my-app.labels" -}}
app.kubernetes.io/name: {{ .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
environment: {{ .Values.environment | default "dev" }}
{{- end }}

In this template:
    my-app.labels is a reusable template name.
    It defines standard labels with chart and release metadata.
    It also includes a custom environment label from values.yaml, defaulting to "dev" if not provided.

Step 2: Use the labels Template in deployment.yaml
In your deployment.yaml template, you can call my-app.labels using the template function.

deployment.yaml:
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "my-app.fullname" . }}
  labels:
    {{- include "my-app.labels" . | nindent 4 }}
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      app.kubernetes.io/name: {{ .Chart.Name }}
  template:
    metadata:
      labels:
        {{- include "my-app.labels" . | nindent 8 }}
    spec:
      containers:
        - name: {{ .Chart.Name }}
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          ports:
            - containerPort: 8080
Explanation
    include "my-app.labels" .: Calls the my-app.labels template we defined in _helpers.tpl.
    | nindent 4 and | nindent 8: Indents the output from my-app.labels to align correctly within the YAML structure.

Step 3: Use the Same Labels Template in service.yaml
The same labels can also be used in other resources like Service.

service.yaml:
apiVersion: v1
kind: Service
metadata:
  name: {{ include "my-app.fullname" . }}
  labels:
    {{- include "my-app.labels" . | nindent 4 }}
spec:
  selector:
    app.kubernetes.io/name: {{ .Chart.Name }}
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080

values.yaml Example
Define the environment and other values in values.yaml:
replicaCount: 2
environment: production
image:
  repository: my-app
  tag: "1.0.0"

Result
Using templates within templates allows you to reuse the my-app.labels template in both deployment.yaml and service.yaml. Any changes to labels (like adding more metadata) can be done once in _helpers.tpl, and they’ll be reflected across all resources. This approach keeps your Helm chart maintainable and consistent.
```