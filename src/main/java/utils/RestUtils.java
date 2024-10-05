package utils;

import java.io.File;
import java.util.HashMap;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestUtils {

	public static Response taPost(String baseUri, HashMap<String, String> headers, Object payload) {
		RestAssured.baseURI = baseUri;
		Response res = RestAssured.given().headers(headers).when().body(payload).post();
		return res;
	}

	public static Response taPost(String sBaseUri, HashMap<String, String> header, String payload) {
		RestAssured.baseURI = sBaseUri;
		System.out.println("taPost: URI :" + sBaseUri);
		
	//	RestAssured.useRelaxedHTTPSValidation();
		Response res = RestAssured.given().headers(header).when().body(payload).post();
		return res;
	}
	public static Response logout(String sBaseUri, HashMap<String, String> header) {
		RestAssured.baseURI = sBaseUri;
		System.out.println("taPost: URI :" + sBaseUri);
		
	//	RestAssured.useRelaxedHTTPSValidation();
		Response res = RestAssured.given().headers(header).when().post();
		return res;
	}

	public static Response taGet(String sBaseUri, HashMap<String, String> header) {
		RestAssured.baseURI = sBaseUri;
		Response res = RestAssured.given().headers(header).when().get();
		return res;
	}

	public static Response taPut(String sBaseUri, HashMap<String, String> header, HashMap<String, String> payload) {
		RestAssured.baseURI = sBaseUri;
		Response res = RestAssured.given().headers(header).when().body(payload).put();
		return res;
	}
	
	
	public static void validateSchema(Response actualResponse, String schemaFilePath) {
		actualResponse.then().assertThat().body(matchesJsonSchema(new File(schemaFilePath)));
	}

	public static Response taPut(String sBaseUri, HashMap<String, String> headers, String payload) {
		RestAssured.baseURI = sBaseUri;
		Response res = RestAssured.given().headers(headers).when().body(payload).put();
		return res;
	}
	public static Response taDelete(String sBaseUri, HashMap<String, String> header,String payload) {
		RestAssured.baseURI = sBaseUri;
		Response res = RestAssured.given().headers(header).when().body(payload).delete();
		return res;
	}
	
}

