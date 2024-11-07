# part-10-enable-cors-filter

When we call the endpoint using a UI frontend we will get this error 

From our application we are calling a external API called /api.tvmaze

postman <---->  our application <----->  /api/tvmaze
All is fine here


frontend (UI) <--Here we will see the CORS error-->  our application <----->  /api/tvmaze

Why we get this error ?
We have a website called www.abc.com

From abc we are calling an API hosted on www.xyz.com
www.abc.com  <------>   www.xyz.com
abc is hosted on some different host
xyz is hosted on some different host

Here the question is does the code on abc domain have the permission to invoke or call API on xyz domain ?
Means is resource sharing allowed ?

www.abc.com  <------>   www.xyz.com
  [origin]             [cross origin]
     ^                      ^
     ---do we have permission---

How to mimick the frontend ?
https://www.w3schools.com/jquery/jquery_get_started.asp

--> 

https://www.w3schools.com/jquery/tryit.asp?filename=tryjquery_lib_google

Create a index.html and paste this code 
<!DOCTYPE html>
<html>
<head>
</head>
<body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
  
});
</script>
</body>
</html>


--> 

https://www.w3schools.com/jquery/jquery_ajax_get_post.asp
Copy this code 
  $.get("demo_test.asp", function(data, status){
    alert("Data: " + data + "\nStatus: " + status);
  });

and paste it in index.html and then modify as below
<script>
$(document).ready(function(){
  $.get("http://localhost:8080/tvseries/120", function(data, status){
    alert("Data: " + data + "\nStatus: " + status);
  });
});
</script>

Open this html file in browser and refresh the page . You will get errro in network and console tab of the browser
Response body is not available to scripts (Reason: CORS Missing Allow Origin)
Cross-Origin Request Blocked: The Same Origin Policy disallows reading the remote resource at http://localhost:8080/tvseries/120. (Reason: CORS header ‘Access-Control-Allow-Origin’ missing). Status code: 200.

Add below properties to the application.properties
quarkus.http.cors=true
quarkus.http.cors.origins=*

again refresh the page.



