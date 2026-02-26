package core;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(LoggingFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        logger.info("{} {}", requestSpec.getMethod(), requestSpec.getURI());

        Response response = ctx.next(requestSpec, responseSpec);

        logger.info("RESPONSE status={} body={}", response.getStatusCode(), response.getBody().asString());

        return response;
    }
}