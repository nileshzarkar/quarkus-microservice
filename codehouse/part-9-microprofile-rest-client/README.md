# part-9-microprofile-rest-client

https://api.tvmaze.com/shows/120

// https://api.tvmaze.com/shows/120
// baseurl : https://api.tvmaze.com
// path : /shows/{id}

Create class TvSeries  with fields and setter and getter
public class TvSeries {
    int id;
    URL url;
    String name;
    String type;
    String language;
}

Create a resource class TvResource
@Path("/tvseries")
public class TvSeriesResource {
   // localhost:8080/tvseries/120
   @GET
   @Path("/{id}")
   public TvSeries getTvSeriesById(@PathParam("id") int id) {
   }
}


Create a proxy  TvSeriesIdProxy
@Path("/")
@RegisterRestClient(baseUri = "https://api.tvmaze.com")
public interface TvSeriesIdProxy {
    //https://api.tvmaze.com/shows/120
    @GET
    @Path("shows/{id}")
    TvSeries getTvSeriesById(@PathParam("id") int id);
}

Update the resource class 
@Path("/tvseries")
public class TvSeriesResource {
   // localhost:8080/tvseries/120
   @RestClient
   TvSeriesIdProxy proxy;
   @GET
   @Path("/{id}")
   public TvSeries getTvSeriesById(@PathParam("id") int id) {
     return proxy.getTvSeriesById(id);
   }
}


add swagger-ui extension
PS D:\Experiments\quarkus-microservice\codehouse\part-9-microprofile-rest-client> mvn quarkus:add-extension -Dextensions="quarkus-smallrye-openapi"

Try it using swagger-ui 
http://localhost:8080/q/swagger-ui

===================

https://api.tvmaze.com/search/people?q=lauren