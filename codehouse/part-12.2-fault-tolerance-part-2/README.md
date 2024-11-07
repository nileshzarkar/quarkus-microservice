# part-12.2-fault-tolerance-part-2



IF NASA SERVER HAS INTERMITENT ISSUES< RETRY AND THEN FAIL
==========================================================
The @CircuitBreaker annotation from the MicroProfile Fault Tolerance library is used to prevent repeated calls to a failing method, helping to stabilize the system by opening a "circuit" and stopping further calls for a specified period. This mechanism is useful when a service or operation is temporarily failing and retrying it frequently may cause further degradation.

How @CircuitBreaker Works

The @CircuitBreaker annotation monitors failures in the method it annotates and manages the method's availability based on the specified conditions. If the failure rate crosses a certain threshold within a certain volume of requests, it will "open" the circuit, meaning no calls will go through until a delay period has passed. This helps prevent continuous retries to a failing service and allows time for recovery.

Breakdown of the Annotation Parameters
@CircuitBreaker(
    requestVolumeThreshold = 4,
    failureRatio = 0.5,
    delay = 10,
    delayUnit = ChronoUnit.SECONDS
)

Here’s what each attribute means:

    requestVolumeThreshold = 4: Specifies the minimum number of requests needed to start evaluating the failure ratio. In this case, the circuit breaker will not evaluate the failure ratio until at least 4 requests have been made to the annotated method.

    failureRatio = 0.5: Defines the threshold for failures as a fraction. Here, 0.5 means that if 50% or more of the requests within the threshold fail, the circuit breaker will open. For example, if 2 out of the 4 requests fail (50%), the circuit will open.

    delay = 60: This is the period that the circuit breaker will stay open before attempting to "half-open" and allow a test request. In this case, the delay is set to 60 units, which is interpreted as 60 seconds due to delayUnit.

    delayUnit = ChronoUnit.SECONDS: Specifies the unit for the delay parameter, which is 60 seconds in this case.


How It Works in Practice

    Closed State (Normal Operation): Initially, the circuit breaker is "closed," allowing all requests to pass through to the method.

    Evaluating Failures: Once the requestVolumeThreshold is reached (4 requests in this case), the circuit breaker begins to evaluate the failureRatio. If 50% or more of these requests fail, the circuit breaker "opens."

    Open State (Blocking Calls): When the circuit is "open," no further calls will be sent to the method. Instead, they will immediately fail (or return a fallback if one is configured) until the delay period has elapsed (60 seconds in this case).

    Half-Open State (Testing Recovery): After the delay period, the circuit breaker goes into a "half-open" state, where it allows a single test request to check if the method has recovered. If the test request succeeds, the circuit closes, and normal operation resumes. If it fails, the circuit goes back to the open state and waits another 60 seconds.

Example Scenario

Imagine a service that is unstable and failing often:

    If the circuit breaker sees 4 requests and at least 2 of them fail (50%), it will open the circuit.
    For the next 60 seconds, any call to this method will immediately fail without being attempted.
    After 60 seconds, it will allow one test call to check if the method is functioning again.
        If the call succeeds, the circuit closes.
        If the call fails, the circuit reopens and waits another 60 seconds.

This approach helps protect your application from frequent retries on an unstable service, giving the service time to recover before additional requests are made.

To verify this we will write a shell script 
weather.sh
while true; do sleep 1; curl http://localhost:8080/weather/dubai; echo -e '\n'; done


Output:
$ while true; do sleep 1; curl http://localhost:8080/weather/dubai; echo -e '\n'; done
NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

NASA Server is down, Please wait or try after sometime

Client is seeing this message , but server side the method got executed only once 
2024-11-07 18:09:46,102 INFO  [com.exa.IndiaWeatherResource] (executor-thread-1) Calling NasaWeatherResource::getWeatherByCountry
