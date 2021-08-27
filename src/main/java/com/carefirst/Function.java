package com.carefirst;

import java.util.Map.Entry;
import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("memberId")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS,
                route = "memberId/{id:int}")
                HttpRequestMessage<Optional<String>> request,
                @BindingName("id") int id,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        for(Entry e : request.getQueryParameters().entrySet()) {
        	context.getLogger().info(e.getKey() + " -> " + e.getValue());
        }
        // Parse query parameter
        final String query = request.getQueryParameters().get("lob");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass the lob on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("MemberId passed is " + id + " and belongs to lob: " + name).build();
        }
    }
}
