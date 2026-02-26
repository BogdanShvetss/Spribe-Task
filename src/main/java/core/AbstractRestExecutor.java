package core;

import config.FrameworkConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class AbstractRestExecutor {

    protected final RequestSpecification spec;

    public AbstractRestExecutor() {
        spec = new RequestSpecBuilder()
                .setBaseUri(FrameworkConfig.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .addFilter(new LoggingFilter())
                .build();
    }

    protected Response executeGet(String path) {
        return executeGetWithQueryParams(path, Collections.emptyMap(), Collections.emptyMap());
    }

    protected Response executeGetWithQueryParams(String path, Map<String, Object> queryParams, Map<String, Object> pathParams) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .queryParams(queryParams)
                .when()
                .get(path)
                .then()
                .extract().response();
    }

    protected Response executePost(String path, Object body) {
        return given()
                .spec(spec)
                .body(body)
                .when()
                .post(path)
                .then()
                .extract().response();
    }

    protected Response executeDelete(String path, Object body, Map<String, Object> pathParams) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .delete(path)
                .then()
                .extract().response();
    }

    protected Response executePatch(String path, Object body, Map<String, Object> pathParams) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .patch(path)
                .then()
                .extract().response();
    }
}