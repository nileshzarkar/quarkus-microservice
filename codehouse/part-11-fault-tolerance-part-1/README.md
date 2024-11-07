# part-11-fault-tolerance-part-1

Dubai (Browser)  <------>  WorldWeather.com (Singapore host) <---calls NASA API---> NASA (US)

If for some unknown reason NASA API server is down, WorldWeather.com will not get any response and will throw timeout exception.

It will be a BAD experience for Dubai user if we show exception page of timeout page.

So what should we show to the Dubai user in such a scenario ? 

This is handled by fallback method basically we respond back with some default response which is experience rich instead of showing exception or timeout page.

Need to add extension for this fallback mechanism

PS D:\Experiments\quarkus-microservice\codehouse\part-11-fault-tolerance-part-1> mvn quarkus:add-extension -Dextensions="quarkus-smallrye-fault-tolerance"

   @GET
   @Fallback(fallbackMethod = "getTvSeriesByIdFallBacl")
   @Path("/{id}")
   public Response getTvSeriesById(@PathParam("id") int id) {
     return Response.ok(proxy.getTvSeriesById(id)).build();
   }

   
  public Response getTvSeriesByIdFallBacl(int id) {
    return Response.ok("Site is under maintainence").build();
  }

http://localhost:8080/tvseries/120
Will give Tv Series object with default values.

