





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
Usecase: 2
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

```