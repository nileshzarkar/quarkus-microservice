# part-12.1-fault-tolerance-part-1

Dubai (Browser)  <------>  WorldWeather.com (Singapore host) <---calls NASA API---> NASA (US)

If for some unknown reason NASA API server is facing network issues, WorldWeather.com will not get any response in leats time of is intermitent.

It will be a BAD experience for Dubai user if we show exception page of timeout page.

So what should we show to the Dubai user in such a scenario ? 

This is handled by fallback method basically we respond back with some default response which is experience rich instead of showing exception or timeout page.

In this scenario we should retry the request with some intervals for x times.

Need to add extension for this fallback mechanism

PS D:\Experiments\quarkus-microservice\codehouse\part-11-fault-tolerance-part-1> mvn quarkus:add-extension -Dextensions="quarkus-smallrye-fault-tolerance"


Checkout the project nasa-weather-resource

@RegisterRestClient(baseUri = "http://localhost:8081")
@Path("/api")
public interface NasaWeatherRestClient {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("weather_by_country/{country}")
    Response weatherByCountry(@PathParam("country") String country);
}

NOTE: To test below scenarios shutdown the nasa-weather-resource server

IF NASA SERVER IS DOWN 
======================
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class IndiaWeatherResource {
...
    @GET
    @Path("weather/{country}")
    @Fallback(fallbackMethod = "getWeatherByCountryFallback")
    public Response getWeatherByCountry(@PathParam("country") String country) {
        LOGGER.info("Calling NasaWeatherResource::getWeatherByCountry");
        return weatherRestClient.weatherByCountry(country);
    }

    public Response getWeatherByCountryFallback(String country) {
        return Response.ok("NASA Server is down, Please wait or try after sometime").build();
    }
}

IF NASA SERVER HAS INTERMITENT ISSUES< RETRY AND THEN FAIL
==========================================================
The @Retry annotation from the MicroProfile Fault Tolerance specification is used to automatically retry a method call if it fails. In this case, it will retry the method up to 3 times with a 10-second delay between each retry attempt. Here’s how each attribute works:

maxRetries = 3: Specifies the maximum number of retry attempts. In this case, if the method fails, it will be retried up to 3 additional times (a total of 4 attempts: the initial attempt + 3 retries). If the method still fails after 3 retries, the failure is propagated.

delay = 10: Defines the delay time before each retry. Here, the delay is set to 10 units (interpreted as seconds in this case due to delayUnit).

delayUnit = ChronoUnit.SECONDS: Specifies the unit for the delay. With ChronoUnit.SECONDS, the delay is set to 10 seconds between retries.

How It Works in Practice

When you annotate a method with @Retry, MicroProfile Fault Tolerance intercepts method calls. If an exception is thrown during the method execution, it will automatically retry based on the specified settings:

    The first call to the method is made.
    If the method throws an exception, it waits for 10 seconds, then retries.
    If the retry also fails, it continues this retry pattern until it either:
        Succeeds within 3 retry attempts, or
        Fails all attempts, at which point the exception is propagated.

This approach is useful in scenarios where transient issues may occur, such as network timeouts or temporary service unavailability, allowing the application to attempt recovery without immediate failure.

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class IndiaWeatherResource {
...
    @GET
    @Path("weather/{country}")
    @Fallback(fallbackMethod = "getWeatherByCountryFallback")
    @Retry(
            maxRetries = 3,
            delay = 10,
            delayUnit = ChronoUnit.SECONDS
    )
    public Response getWeatherByCountry(@PathParam("country") String country) {
        LOGGER.info("Calling NasaWeatherResource::getWeatherByCountry");
        return weatherRestClient.weatherByCountry(country);
    }

    public Response getWeatherByCountryFallback(String country) {
        return Response.ok("NASA Server is down, Please wait or try after sometime").build();
    }
}

Output:
2024-11-07 17:46:28,851 INFO  [com.exa.IndiaWeatherResource] (executor-thread-1) Calling NasaWeatherResource::getWeatherByCountry                                                                                                        
2024-11-07 17:46:38,976 INFO  [com.exa.IndiaWeatherResource] (executor-thread-1) Calling NasaWeatherResource::getWeatherByCountry - Retry - 1
2024-11-07 17:46:49,010 INFO  [com.exa.IndiaWeatherResource] (executor-thread-1) Calling NasaWeatherResource::getWeatherByCountry - Retry - 2
2024-11-07 17:46:58,906 INFO  [com.exa.IndiaWeatherResource] (executor-thread-1) Calling NasaWeatherResource::getWeatherByCountry - Retry - 3
After this it will show the fallback response
NASA Server is down, Please wait or try after sometime


