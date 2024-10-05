package test;

import java.io.IOException;
import java.util.HashMap;

import org.testng.annotations.BeforeTest;

import constants.FileConstants;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.DataUtils;
import utils.RestUtils;

public class BaseTest {
	String token=null;
	
	
	@BeforeTest
	public void setup() throws IOException {
		RestAssured.baseURI = DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH, "$.prod.uri").toString();;
	}
	public String genarateToken() throws IOException {
//		Object  payload=DataUtils.getTestData(FileConstants.USERACCOUNTS_TD_FILE_PATH, "$.prod.valid");
		String payload="{\n"
				+ "  \"username\": \"july2024.usha@tekarch.com\",\n"
				+ "  \"password\": \"Admin123\"\n"
				+ "}";
		HashMap<String,String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		String uri=DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH,"$.prod.uri").toString();
		String endpoint=DataUtils.getTestData(FileConstants.ENV_URI_FILE_PATH,"$.prod.endpoint.login").toString();
		if (token==null) {
			Response loginResponse=RestUtils.taPost(uri+endpoint, headers, payload);
			token=loginResponse.jsonPath().get("[0].token");
		}
		else {
			System.out.println("token is generated");
		}
		return token;
		
	}
	
	

}
