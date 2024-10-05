package test;

import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;

import constants.FileConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import testdata.AddUserPOJO;
import testdata.updateuserPOJO;
import utils.DataUtils;
import utils.RestUtils;

public class LoginTest extends BaseTest {

	@Test
	public void login() throws IOException {
		// RestAssured.baseURI =
		// "https://us-central1-qa01-tekarch-accmanager.cloudfunctions.net";
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		String env = DataUtils.readJsonFileToString(FileConstants.ENV_URI_FILE_PATH);
		String envUri = JsonPath.read(env, "$.prod.uri");
		String endpoint = JsonPath.read(env, "$.prod.endpoint.login");
		System.out.println("envUri+endpoint => " + envUri + endpoint);
		String creds = DataUtils.readJsonFileToString(FileConstants.USERACCOUNTS_TD_FILE_PATH);
		HashMap<String, String> credentials = new HashMap<String, String>();
		credentials.put("username", JsonPath.read(creds, "$.prod.valid.username"));
		credentials.put("password", JsonPath.read(creds, "$.prod.valid.password"));
		Response res = RestUtils.taPost(envUri + endpoint, headers, credentials);
		RestUtils.validateSchema(res, FileConstants.LOGIN_SCHEMA_FILE_PATH);
		System.out.println(res.prettyPrint());
//		Response res1 = RestUtils.taGet(envUri + endpoint, headers);
	}

	@Test
	public void invalidLogin() throws IOException {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		String env = DataUtils.readJsonFileToString(FileConstants.ENV_URI_FILE_PATH);
		String envUri = JsonPath.read(env, "$.prod.uri");
		String endpoint = JsonPath.read(env, "$.prod.endpoint.login");
		System.out.println("envUri+endpoint => " + envUri + endpoint);
		String creds = DataUtils.readJsonFileToString(FileConstants.USERACCOUNTS_TD_FILE_PATH);
		HashMap<String, String> credentials = new HashMap<String, String>();
		credentials.put("username", JsonPath.read(creds, "$.prod.invalid.username"));
		credentials.put("password", JsonPath.read(creds, "$.prod.invalid.password"));
		Response res = RestUtils.taPost(envUri + endpoint, headers, credentials);
		System.out.println(res.prettyPrint());
		System.out.println(res.getStatusLine());
}

	@Test
	public void getData() throws IOException {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", genarateToken());
		String endpoint = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.endpoint.getdata");
		Response res = RestUtils.taGet(RestAssured.baseURI + endpoint, headers);
		res.then().assertThat().statusCode(200);
		System.out.println(res.prettyPrint());
		// String env = DataUtils.readJsonFileToString(FileConstants.ENV_URI_FILE_PATH);
		// String envUri = JsonPath.read(env, "$.prod.uri");
		// String endpoint = JsonPath.read(env, "$.prod.endpoint.login");
}

	@Test
	public void addUser_TC02() throws IOException {
		AddUserPOJO user1 = new AddUserPOJO("TA-5678333", "4", "456768", "234567");
		RestAssured.baseURI = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.uri").toString();
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
		String payload = om.writeValueAsString(user1);
//		String env = DataUtils.readJsonFileToString(FileConstants.ENV_URI_FILE_PATH);
//		String endpoint = JsonPath.read(env, "$.prod.endpoint.adddata");
		String endpoint = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.endpoint.adddata");
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", genarateToken());
		Response res = RestUtils.taPost(RestAssured.baseURI + endpoint, headers, payload);
		res.prettyPrint();
		res.then().assertThat().statusCode(201);
	}

	@Test
	public void updateUserData() throws IOException {
		updateuserPOJO user1 = new updateuserPOJO("TA-0000001", "4", "45678", "234567");
		RestAssured.baseURI = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.uri").toString();

		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
		String payload = om.writeValueAsString(user1);
		// String env DataUtils.readJsonFileToString(FileConstants.ENV_URI_FILE_PATH);
		// String endpoint = JsonPath.read(env, "$.prod.endpoint.adddata");

		String endpoint = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.endpoint.updatedata")
				.toString().toString();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", genarateToken());
		Response res = RestUtils.taPut(RestAssured.baseURI + endpoint, headers, payload);
		res.prettyPrint();
		res.then().assertThat().statusCode(401);
		System.out.println(res.getStatusCode());
	}

	@Test
	public void deleteUserData() throws IOException {
		updateuserPOJO user1 = new updateuserPOJO("TA-0000001", "4", "45678", "234567");
		RestAssured.baseURI = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.uri").toString();

		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);
		String payload = om.writeValueAsString(user1);
		// String env DataUtils.readJsonFileToString(FileConstants.ENV_URI_FILE_PATH);
		// String endpoint = JsonPath.read(env, "$.prod.endpoint.adddata");

		String endpoint = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.endpoint.deletedata")
				.toString().toString();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", genarateToken());
		Response res = RestUtils.taDelete(RestAssured.baseURI + endpoint, headers, payload);

		res.prettyPrint();
		res.then().assertThat().statusCode(401);
		System.out.println(res.getStatusCode());
	}

	@Test
	public void logoutPost() throws IOException {

		ObjectMapper om = new ObjectMapper();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("token", genarateToken());
		String endpoint = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.endpoint.logout").toString()
				.toString();
		Response res = RestUtils.logout(RestAssured.baseURI + endpoint, headers);
		res.then().assertThat().statusCode(200);
		System.out.println("Logout Status = " + res.statusCode());

		// String env = DataUtils.readJsonFileToString(FileConstants.ENV_URI_FILE_PATH);
		// String envUri = JsonPath.read(env, "$.prod.uri");
		// String endpoint = JsonPath.read(env, "$.prod.endpoint.logout");
//		System.out.println("envUri+endpoint => "+envUri+endpoint);
		// Response res1 = RestUtils.taPost(envUri + endpoint, headers, " ");

	}

//	@Test
	public void test() throws IOException {
		// System.out.println(genarateToken());
	}
}
