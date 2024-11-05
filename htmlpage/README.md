
# Helm Tutorial for htmlpage microservice

### 01-Install-Docker-Desktop-and-HelmCLI

### 02-Helm-Install

helm repo list

The command helm repo list is used to display all Helm repositories that have been added to your local Helm client.

Purpose

It helps you see:
- The names of repositories you've added.
- The URLs for each repository, indicating where Helm pulls charts from.

Example

When you run:

helm repo list

You’ll get a table like this:

NAME       	URL

my-bitnami 	https://charts.bitnami.com/bitnami

stable     	https://charts.helm.sh/stable

This output shows:
- my-bitnami: The name you assigned to the Bitnami repository.
- URL: The address where Helm looks for charts in this repository.

Usage

This command is essential for:
- Checking which repositories are configured.
- Verifying repository URLs for troubleshooting or reference.


helm repo add <DESIRED-NAME> <HELM-REPO-URL>
helm repo add mybitnami https://charts.bitnami.com/bitnami

The command helm repo add mybitnami https://charts.bitnami.com/bitnami adds the Bitnami Helm repository to your local Helm setup under the name "mybitnami."

Breakdown of the Command

- helm repo add: This tells Helm to add a new repository.
- mybitnami: This is the local name you’re giving to the repository, making it easier to refer to later. You can name it anything you want (e.g., bitnami, bitnami-repo).
- https://charts.bitnami.com/bitnami: This is the URL for the Bitnami Helm repository, which contains pre-packaged charts for popular applications like MySQL, WordPress, and others.

Purpose

By adding the repository:

- You gain access to all the charts Bitnami provides, so you can easily search, install, and update applications from Bitnami using Helm commands.
- Helm will now look in this URL for any charts you request from mybitnami.

helm search repo <KEY-WORD>

helm search repo nginx

helm search repo apache

helm search repo wildfly

helm repo update  # Make sure we get the latest list of charts

The command helm repo update refreshes your local Helm client’s knowledge of the charts available in all added repositories.

Purpose

- It ensures that Helm fetches the latest list of charts and versions from each repository you’ve added.
- This is essential to get updates for charts that may have been recently added or updated in the repository, allowing you to install or upgrade to the latest versions.

Result

After running this command, Helm will download the latest index of charts from each repository, and you’ll see a message like:

Hang tight while we grab the latest from your chart repositories...

...Successfully got an update from the "mybitnami" chart repository

Update Complete.

helm install <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name>

helm install mynginx mybitnami/nginx

The command helm install mynginx mybitnami/nginx installs the nginx application from the mybitnami repository into your Kubernetes cluster using Helm.

Breakdown of the Command

    helm install: Tells Helm to install a chart.

    mynginx: The name you’re assigning to this Helm release. This release name helps you identify and manage the deployed application in Kubernetes.

    mybitnami/nginx: Refers to the nginx chart in the mybitnami repository. The prefix mybitnami is the name you assigned to the Bitnami repository when you added it with helm repo add.

Purpose

This command:

- Deploys the NGINX application using the predefined configurations in the Helm chart.
- Creates a Helm release called mynginx, which includes the Kubernetes resources required for NGINX (e.g., pods, services).	

Step-04: List Helm Releases

This command lists all of the releases for a specified namespace

List Helm Releases (Default Table Output)

helm list 

helm ls

List Helm Releases (YAML Output)

helm list --output=yaml

List Helm Releases (JSON Output)

helm list --output=json

List Helm Releases with namespace flag

helm list --namespace=default

helm list -n default

The command helm list --namespace=default lists all Helm releases that have been installed in the default namespace of your Kubernetes cluster.

Breakdown of the Command
- helm list: This command lists all Helm releases in the specified namespace (or all namespaces if no specific one is provided).
- --namespace=default: This option limits the output to releases within the default namespace.
Purpose
- It helps you view the status and details of all releases deployed specifically in the default namespace.
- Useful for managing releases in different namespaces and isolating deployments.

Running the command might show:

NAME       	NAMESPACE	REVISION	UPDATED                                	STATUS  	CHART         	APP VERSION

mynginx    	default  	1       	2023-10-25 10:00:00.000000 +0000 UTC	deployed	nginx-9.1.2   	1.19.10

mywordpress	default  	1       	2023-10-25 11:00:00.000000 +0000 UTC	deployed	wordpress-10.0.0	5.7.2

Here:
- NAME: The release name (e.g., mynginx, mywordpress).
- NAMESPACE: The namespace (default).
- STATUS: The release status (e.g., deployed), indicating the current state of each application.

Step-05: List Kubernetes Resources

kubectl get pods

kubectl get svc

Observation: Review the EXTERNAL-IP field and you will see it as localhost. Access the nginx page from local desktop localhost

http://localhost:80

http://127.0.0.1:80

curl http://localhost:80

curl http://127.0.0.1:80

helm ls

helm uninstall <RELEASE-NAME>

helm uninstall mynginx 

The command helm uninstall mynginx removes the Helm release named mynginx from your Kubernetes cluster.

Breakdown of the Command
- helm uninstall: Tells Helm to delete a specified release and all associated resources.
- mynginx: The name of the release you want to uninstall, which was provided when you installed the chart.

Purpose
- It deletes all Kubernetes resources created by the mynginx release, such as pods, services, and deployments, effectively removing the application from the cluster.
- This command is useful when you no longer need the application or want to clear resources.

Example Usage

After running:

helm uninstall mynginx

You’ll see a confirmation message:

release "mynginx" uninstalled

Note
- This command only removes the Helm-managed resources for mynginx. If any other manually created resources depend on it, they’ll need to be deleted separately.

### 03-Helm-Upgrade-with-set-option

Helm Upgrade with set option

Step-01: Introduction
- We are going to upgrade the HELM RELEASE using `helm upgrade` command in combination with `--set "image.tag=<DOCKER-IMAGE-TAGS>`
- We will use the following Helm Commands in this demo.
- helm repo 
- helm search repo
- helm install
- helm upgrade
- helm history
- helm status

Step-02: Custom Helm Repo
Step-02-01: Review our Custom Helm Repo
- [StackSimplify Helm Repo hosted on GitHub](https://stacksimplify.github.io/helm-charts/)
- [GitHub Repository for StackSimplify Helm Repo](https://github.com/stacksimplify/helm-charts)
- [artifacthub.io](https://artifacthub.io): Search for `stacksimplify`
- [mychart1 from artifacthub.io](https://artifacthub.io/packages/helm/stacksimplify/mychart1)


Step-02-02: Add Custom Helm Repo
```t
# List Helm Repositories
helm repo list

# Add Helm Repository
helm repo add <DESIRED-NAME> <HELM-REPO-URL>
helm repo add stacksimplify https://stacksimplify.github.io/helm-charts/

# List Helm Repositories
helm repo list

# Search Helm Repository
helm search repo <KEY-WORD>
helm search repo mychart1
```

Step-03: Install Helm Chart from our Custom Helm Repository
```t
# Install myapp1 Helm Chart
helm install <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name>
helm install myapp1 stacksimplify/mychart1 
```
Step-04: List Resources and Access Application in Browser
```t
# List Helm Release
helm ls 
or 
helm list

# List Pods
kubectl get pods

# List Services 
kubectl get svc

# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231
```

Step-04: Helm Upgrade
- [kubenginx Docker Image with 1.0.0, 2.0.0, 3.0.0, 4.0.0](https://github.com/users/stacksimplify/packages/container/package/kubenginx)
```t
# Review the Docker Image Versions we are using
https://github.com/users/stacksimplify/packages/container/package/kubenginx
Image Tags: 1.0.0, 2.0.0, 3.0.0, 4.0.0

# Helm Upgrade
helm upgrade <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name> --set <OVERRIDE-VALUE-FROM-values.yaml>
helm upgrade myapp1 stacksimplify/mychart1 --set "image.tag=2.0.0"

The --set flag allows you to override chart values directly from the command line. 

In this case, it’s setting the image.tag value to 2.0.0, likely referring to the version of the container image for your application. 

This means that the updated release will deploy a container image with the tag 2.0.0
```
Step-05: List Resources after helm upgrade
```t
# List Helm Releases
helm list 
Observation: We should see Revision as 2

# Additional List commands
helm list --superseded

The command helm list --superseded lists all Helm releases in the cluster that are in the superseded state. Here’s a breakdown:
- helm list: This command lists Helm releases in the cluster, providing information about each release, such as its name, namespace, revision, status, and last update time.
- --superseded: This flag filters the results to only include releases in the "superseded" status.

What does "Superseded" mean?

A Helm release is marked as "superseded" when it has been upgraded to a newer revision. For example:
- If you initially install a release called myapp1 with version 1 of a chart, and then upgrade it to version 2, the original version is marked as "superseded."
- This status means that the release has been replaced by a newer revision but is still stored in Helm’s history.

This command is useful if you want to review or clean up older, replaced releases that are still stored in the Helm history but are no longer active.

helm list --deployed

The command helm list --deployed lists all Helm releases in the cluster that are currently in the deployed state. Here’s what each part means:
- helm list: This command displays a list of Helm releases in the cluster, showing details like release name, namespace, revision, status, and last update time.
- --deployed: This flag filters the results to show only those releases that are actively deployed in the cluster.

What does "Deployed" mean?

When a release is in the "deployed" state, it means:
- The release is currently running and active in the cluster.
- The resources (such as pods, services, and configurations) defined in the Helm chart are operational and accessible.

This command is helpful when you want to view only the active releases, filtering out superseded, failed, or pending releases

# List and Describe Pod
kubectl get pods
kubectl describe pod <POD-NAME> 
Observation: In the Pod Events you should find that "ghcr.io/stacksimplify/kubenginx:2.0.0" is pulled or if already exists on desktop it will be used to create this new pod

# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231
Observation: Version 2 of application should be displayed
```

Step-06: Do two more helm upgrades - For practice purpose
```t
# Helm Upgrade to 3.0.0
helm upgrade myapp1 kalyan-repo/myapp1 --set "image.tag=3.0.0"

# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231

# Helm Upgrade to 4.0.0
helm upgrade myapp1 kalyan-repo/myapp1 --set "image.tag=4.0.0"

# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231
```

Step-07: Helm History
- History prints historical revisions for a given release.
```t
# helm history
helm history RELEASE_NAME
helm history myapp1
```

Step-08: Helm Status
- This command shows the status of a named release. 
```t
# Helm Status
helm status RELEASE_NAME
helm status myapp1

The command helm status is used to check the current status of a specific Helm release. It provides detailed information about the release, including its state, version, and resources, helping you understand how the release is running in the cluster.

# Helm Status - Show Description (display the description message of the named release)
helm status myapp1 --show-desc    

# Helm Status - Show Resources (display the resources of the named release)
helm status myapp1  --show-resources   

# Helm Status - revision (display the status of the named release with revision)
helm status RELEASE_NAME --revision int
helm status myapp1 --revision 2
```

Step-09: Uninstall Helm Release
```t
# Uninstall Helm Release
helm uninstall myapp1
```






## 12-Helm-Dev-Basics

### Template Actions `{{ }}`

In Helm, Template Actions are commands or functions that tell Helm what to do within a template file. 

They use Go template syntax and are placed inside {{ ... }} braces. 

These actions help control the content, structure, and behavior of the generated Kubernetes manifests by adding dynamic values, conditions, loops, and more.

Here’s a breakdown of common template actions in Helm, explained in simplified terms:

- Anything in between Template Action `{{ .Chart.Name }}` is called Action Element
- Anything in between Template Action `{{ .Chart.Name }}` will be rendered by helm template engine and replace necessary values
- Anything outside of the template action will be printed as it is.

### Action Elements `{{ .Release.Name }}`

In Helm, Action Elements are special commands in templates that perform specific actions, like defining templates, including other templates, or running conditional logic. 

They are written inside {{ ... }} or {{- ... -}} tags in Helm templates and help control how Kubernetes manifests are generated.

Here’s a breakdown of the main types of Action Elements:

1. Define (define)

    Used to create reusable templates that can be called in other parts of the chart.

    You define a section of code with define and give it a name, which you can refer to later.

    Example:
```t    
{{- define "my-chart.labels" -}}
app: {{ .Chart.Name }}
version: {{ .Chart.Version }}
{{- end }}
```
2. Include (include)

    Used to call or "include" a template that you defined elsewhere (like in _helpers.tpl).

    You use include to insert the output of a defined template into another part of your template.

    Example:    
```t
labels:
  {{ include "my-chart.labels" . | indent 4 }}
```
3. If/Else (if, else, else if)

    Used to apply conditional logic in templates.

    You can create different sections of configuration depending on values from values.yaml or other variables.

    Example:
```t
{{- if .Values.production }}
replicas: 3
{{- else }}
replicas: 1
{{- end }}
```
4. With (with)

    Sets a specific scope to simplify access to values.

    Useful when working with nested values or a specific part of the template to avoid long variable paths.

    Example:
```t
{{- with .Values.image }}
image: {{ .repository }}:{{ .tag }}
{{- end }}
```
5. Range (range)

    Used to loop through lists or maps (like arrays of data in values.yaml).

    Allows you to repeat parts of a template for each item in a list or dictionary.

    Example:
```t    
env:
  {{- range .Values.env }}
  - name: {{ .name }}
    value: {{ .value }}
  {{- end }}
```

# Helm Template Command
helm template myapp101 .
1. helm template command helps us to check the output of the chart in fully rendered Kubernetes resource templates. 
2. This will be very helpful when we are developing a new chart, making changes to the chart templates, for debugging etc.

### Quote Function
In Helm, the quote and dequote functions are used to manage how values are handled as strings, which can be helpful for ensuring the correct data format in YAML or when dealing with complex strings. Here’s a simple explanation of each:
1. quote Function

The quote function in Helm adds double quotes around a value, ensuring that it’s treated as a string. This is useful for handling values that might look like other types (e.g., numbers or booleans) but need to be read as strings.

Example: Using quote to Ensure a Value is a String

Let’s say you have a values.yaml file with a setting that looks numeric but should be treated as a string:
```t
apiVersion: 2
```
In your template, you might want to ensure apiVersion is treated as a string:
```t
apiVersion: {{ .Values.apiVersion | quote }}
```
This results in:
```t
apiVersion: "2"
```
If you didn’t use quote, apiVersion could be interpreted as a number instead of a string, which can sometimes cause issues in YAML files.

2. dequote Function

The dequote function removes any leading or trailing double quotes from a value. 

This is useful if you have a value that comes with quotes but you need it to be unquoted in the output.

Example: Using dequote to Remove Quotes from a String

Suppose your values.yaml file has:
```t
namespace: '"default"'
```
Without dequote, it would appear with double quotes in your template:
```t
namespace: {{ .Values.namespace }}
```
Output:
```t
namespace: "\"default\""
```
Using dequote removes the unnecessary quotes:
```t
namespace: {{ .Values.namespace | dequote }}
```
Output:
```t
namespace: default
```
Summary
- quote: Adds double quotes around a value, ensuring it’s treated as a string.
- dequote: Removes double quotes from a value, making it appear without quotes.

These functions are especially helpful when working with mixed data types or when values in values.yaml could be misinterpreted by YAML or Kubernetes manifests.

### Pipeline

In Helm, a Pipeline is a way to chain multiple template functions together to process data step-by-step. 

It’s represented by the | symbol (pipe) and works similarly to Unix/Linux pipelines, where the output of one function is passed as input to the next function.

Why Use Pipelines?

Pipelines make it easier to format, modify, or control the output of data in Helm templates. 

By combining functions, you can create clean, readable code and apply multiple operations in a single line.

Basic Structure of a Pipeline
```t
{{ <input> | <function1> | <function2> | ... }}
```
Each function processes the input and passes the result to the next function in the chain.

Common Pipeline Functions in Helm

Let’s look at some examples of pipelines and how they are commonly used in Helm templates.

Example 1: Formatting Text with quote and upper

Suppose you have an environment variable, and you want to make its value uppercase and wrap it in quotes.
```t
env:
  - name: ENVIRONMENT
    value: {{ .Values.environment | upper | quote }}
```    
Here’s what’s happening:
    .Values.environment is fetched from values.yaml.
    upper converts it to uppercase.
    quote wraps it in double quotes.
Result: If environment is dev, the output would be:
```t
env:
  - name: ENVIRONMENT
    value: "DEV"
```

Example 2: Indenting Text with nindent

Indentation is important in YAML, so nindent helps to format text with both a newline and specific indentation.
```t
labels:
  {{ include "my-chart.labels" . | nindent 4 }}
```
Here:
    include "my-chart.labels" . calls a reusable template.
    nindent 4 adds a newline and indents the output by 4 spaces.

Example 3: Using default with Pipelines

Suppose you want to use a default value if a variable isn’t set. The default function provides a fallback value.
```t
replicas: {{ .Values.replicas | default 1 }}
```
Here:
    If .Values.replicas is defined, it uses that value.
    If not, it defaults to 1.
Result: If replicas isn’t set in values.yaml, the output would be:
```t
replicas: 1
```
Example 4: Combining include, trim, and default

This example demonstrates chaining multiple functions for custom formatting.
```t
name: {{ include "my-chart.fullname" . | trim | default "default-name" }}
```
Here:
    include "my-chart.fullname" . generates a full name from a defined template.
    trim removes any extra whitespace.
    default "default-name" sets a fallback if the result is empty.
Result: If fullname is empty, the output will be:
```t
name: default-name
```
Summary
In Helm, pipelines are a powerful way to simplify template code by chaining functions together:
    You can transform data, add default values, or clean up the output with a readable, single line.
    Common functions used in pipelines include quote, default, upper, trim, nindent, and include.
Pipelines help keep Helm templates clean, flexible, and easy to read.

### default Function

In Helm, the default function is used to provide a fallback value if a variable is missing or undefined. 

It’s especially helpful when you want to make sure a value is always set, even if it isn’t provided in values.yaml.
```t
{{ default "fallback_value" .Values.someKey }}
```
- fallback_value: The value to use if .Values.someKey is not set or is empty.
- .Values.someKey: The variable you’re checking from values.yaml.

If .Values.someKey is defined, Helm will use its value. If not, Helm will use "fallback_value".

Examples of default in Action

Example 1: Setting a Default Value for a Variable

Suppose you have a variable replicaCount in values.yaml to define the number of replicas for a deployment. If it is not set, you want to default to 1.

Template (deployment.yaml):
```t
replicas: {{ default 1 .Values.replicaCount }}
```
- If replicaCount is defined in values.yaml (e.g., replicaCount: 3), Helm uses that value.
- If replicaCount is missing or empty, Helm will use 1 as the default.

Example 2: Providing a Default Image Tag

Suppose you have an image tag that should default to latest if not set in values.yaml.

Template (deployment.yaml):
```t
image:
  repository: my-app
  tag: {{ default "latest" .Values.image.tag }}
```

values.yaml:
```t
image:
  repository: my-app
```
- Here, if image.tag is not specified in values.yaml, Helm will use "latest" as the tag.
- If you later add image.tag: "1.0.0" in values.yaml, Helm will use "1.0.0" instead.

Example 3: Setting a Default Label

If you want to add an environment label that defaults to "dev" when not specified:

Template (deployment.yaml):
```t
metadata:
  labels:
    environment: {{ default "dev" .Values.environment }}
```    
values.yaml:
```t
# environment: "production"
```
  - If environment is commented out or missing in values.yaml, Helm will set the label as environment: dev.
  - If environment is defined as "production", Helm will use that value.

Summary

The default function ensures that Helm charts have fallback values, making templates more robust and flexible. This function helps avoid errors and ensures that necessary values are always present.

### Controlling White Spaces `{{-  -}}`

In Helm templates, whitespace control is important for creating clean and readable YAML output. 

Helm uses {{- and -}} to trim spaces from template expressions, allowing you to control how much whitespace appears in the final output.

Here’s a simplified breakdown of how to use {{- ... -}} to control whitespace.
1. Basic Syntax of Whitespace Control
    {{ ... }}: Includes spaces around the template expression.

    {{- ... }}: Trims spaces before the expression.

    {{ ... -}}: Trims spaces after the expression.

    {{- ... -}}: Trims both before and after the expression.

2. Examples of Whitespace Control

Example 1: Removing Extra Blank Lines

Without whitespace control, you may end up with unwanted blank lines in your output.
Template without Whitespace Control:
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  key: {{ .Values.someKey }}
  {{ if .Values.optionalKey }}
  optionalKey: {{ .Values.optionalKey }}
  {{ end }}
```
If optionalKey is not defined, this produces extra blank lines in the output:
Output:
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  key: value
```
Template with Whitespace Control:
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  key: {{ .Values.someKey }}
  {{- if .Values.optionalKey }}
  optionalKey: {{ .Values.optionalKey }}
  {{- end }}
```  
Output (no extra lines):
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  key: value
```  
Using {{- before if and end removes any unnecessary blank lines.

Example 2: Controlling Indentation in Loops

When using loops, whitespace control helps prevent unintended line breaks.

Template without Whitespace Control:
```t
env:
  {{ range .Values.env }}
  - name: {{ .name }}
    value: {{ .value }}
  {{ end }}
```  
If .Values.env has multiple items, this could add blank spaces between items.

Template with Whitespace Control:
```t
env:
  {{- range .Values.env }}
  - name: {{ .name }}
    value: {{ .value }}
  {{- end }}
```  
Output:
```t
env:
  - name: KEY1
    value: VALUE1
  - name: KEY2
    value: VALUE2
```    
By using {{- and -}} around range and end, we get a clean list with no extra spaces.

Summary
-  Use {{- and -}} to remove unwanted blank spaces and lines in Helm templates.
-  Helps in keeping YAML files neat and readable, especially when handling optional fields or loops.

This ensures that your final YAML output is well-formatted and free of unnecessary blank lines or spaces.

### indent function
In Helm, the indent function is used to add spaces at the beginning of each line in a block of text. This is especially helpful in YAML, where indentation is critical for structuring data correctly.

The indent function takes two arguments:
- Number of spaces to add.
- The content to be indented.

Here’s how it works with examples.

Example 1: Basic Usage of indent

Suppose you want to add labels in your deployment.yaml template, and you need them indented by 4 spaces to align with YAML structure.
```t
metadata:
  labels:
    {{- include "my-app.labels" . | indent 4 }}
```
Here, indent 4 adds 4 spaces to the beginning of each line in the my-app.labels template output, ensuring it’s correctly aligned under labels:.

Example 2: Using indent with range

When using loops (range) in Helm, each line in the loop may need to be indented to fit properly in the YAML structure.
values.yaml:
```t
env:
  - name: APP_ENV
    value: "production"
  - name: LOG_LEVEL
    value: "info"
```    
deployment.yaml:
```t
spec:
  containers:
    - name: my-app
      env:
        {{- range .Values.env }}
        {{ .name }}: {{ .value | quote | indent 6 }}
        {{- end }}
```
Here:
    indent 6 aligns each environment variable under env:, so each variable line has the correct 6-space indentation.

Example 3: Combining indent with Multi-line Text

Sometimes, you might want to include multi-line text data like ConfigMap data or scripts in your templates. You can use indent to ensure correct alignment.
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  my-config-file: |
    {{- "key1: value1\nkey2: value2\nkey3: value3" | indent 4 }}
```
Here:
    indent 4 adds 4 spaces to each line in the multi-line string, making sure it aligns properly within the YAML structure under my-config-file:.

Summary
- indent helps maintain the correct YAML structure by adding spaces at the beginning of lines.
- It is especially useful when working with nested structures, loops, or multi-line text, keeping everything properly aligned.


### nindent function
The nindent function in Helm is used to add indentation along with a newline to blocks of text, making it easier to format YAML properly. 

It combines newline (\n) and indentation for cleaner, readable, and correctly aligned YAML structures.

How nindent Works
- Syntax: nindent N adds a newline and then indents the text by N spaces.
- Purpose: Ensures YAML elements line up correctly, especially in nested structures, without breaking YAML syntax.

Example 1: Basic Usage of nindent

Suppose you have a ConfigMap template, and you want to add multiline data from values:
values.yaml:
```t
configData:
  - key1: "value1"
  - key2: "value2"
```
configmap.yaml:
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  config: |
    {{- toYaml .Values.configData | nindent 4 }}
```
    toYaml converts configData into YAML format.
    nindent 4 adds a newline and indents each line by 4 spaces.
Rendered Output:
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  config: |
    - key1: value1
    - key2: value2
```

Example 2: Nested Structures with nindent

If you’re creating labels or environment variables, nindent helps align fields properly within nested structures.
values.yaml:
```t
env:
  - name: ENV1
    value: "production"
  - name: ENV2
    value: "debug"
```    
deployment.yaml:
```t
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-deployment
spec:
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
        - name: my-container
          image: my-image
          env:
            {{- toYaml .Values.env | nindent 12 }}
```          
Explanation:
    nindent 12 aligns the environment variables inside the containers block, keeping YAML formatting intact.
Output:
```t
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-deployment
spec:
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
        - name: my-container
          image: my-image
          env:
            - name: ENV1
              value: "production"
            - name: ENV2
              value: "debug"
```              
Summary
- nindent adds both a newline and indentation, making nested YAML elements align correctly.
- It’s especially useful for multi-line or nested values in templates, ensuring proper formatting without extra line breaks.

### toYaml
In Helm, toYaml is a function that converts a value (like a map or list) into properly formatted YAML. 

It’s useful for handling complex structures like lists or nested values in values.yaml, making them easier to add to templates without worrying about indentation or formatting.

Here’s how toYaml works and some simple examples:

Basic Usage of toYaml

When you use toYaml, Helm converts a map or list into YAML format. You can then add it to your template, where it will automatically be formatted correctly.

Syntax:
```t
{{ toYaml .Values.someVariable }}
```
Example 1: Converting a Simple Map

Suppose you have the following configuration in values.yaml:

values.yaml:
```t
config:
  appName: "my-app"
  environment: "production"
  replicas: 3
```  
In your template, you can use toYaml to add all the items in config without manually listing each one:

deployment.yaml:
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  config.yaml: |
    {{ toYaml .Values.config | indent 4 }}
```
Here’s what the output will look like after applying toYaml and indent 4 to ensure proper formatting:
Rendered YAML:
```t
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  config.yaml: |
    appName: "my-app"
    environment: "production"
    replicas: 3
```
Example 2: Converting a List of Environment Variables

Suppose you want to add environment variables to your container based on a list defined in values.yaml:
values.yaml:
```t
env:
  - name: "APP_ENV"
    value: "production"
  - name: "LOG_LEVEL"
    value: "info"
```
In your template, use toYaml to easily convert this list into YAML format:
deployment.yaml:
```t
apiVersion: apps/v1
kind: Deployment
spec:
  template:
    spec:
      containers:
        - name: my-container
          env:
            {{ toYaml .Values.env | indent 12 }}
```
Rendered YAML:
```t
apiVersion: apps/v1
kind: Deployment
spec:
  template:
    spec:
      containers:
        - name: my-container
          env:
            - name: "APP_ENV"
              value: "production"
            - name: "LOG_LEVEL"
              value: "info"
```
Example 3: Resources for testing
values.yaml
```t
resources: 
  limits:
    cpu: 100m
    memory: 128Mi
  requests:
    cpu: 100m
    memory: 128Mi
```
deployment.yaml
```t
    spec:
      containers:
      - name: nginx
        image: ghcr.io/stacksimplify/kubenginx:4.0.0
        ports:
        - containerPort: 80
        resources: 
        {{- toYaml .Values.resources | nindent 10}}
```
Rendered YAML:
```t
apiVersion: apps/v1
kind: Deployment
spec:
  template:
    spec:
      containers:
      - name: nginx
        image: ghcr.io/stacksimplify/kubenginx:4.0.0
        ports:
        - containerPort: 80
        resources:
          limits:
            cpu: 100m
            memory: 128Mi
          requests:
            cpu: 100m
            memory: 128Mi
```
Explanation of indent
- | indent N adds N spaces to each line produced by toYaml, which is essential for keeping the YAML structure correct within the larger file.

Summary
- toYaml is used to convert lists or maps to YAML in a clean, readable way.
- It’s especially helpful when you have nested structures or dynamic configurations in values.yaml.
- Using toYaml with indent helps keep templates organized and properly formatted.


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