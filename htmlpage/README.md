





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





```