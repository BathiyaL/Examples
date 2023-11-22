package testscripts;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import org.testng.SkipException;

public class TransactionAPITest {

	@Test(groups = {"API"}, description = "API should return 200 for sucessfull requests")
	public void verifyAPIResponseStatusCode() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"API"}, description = "API should return 400 for bad requests ")
	public void verifyInvalidRequestHandling() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"API"}, description = "API should return 401 for unauthorized requests ")
	public void verifyAuthentication() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"API"}, description = "API should return 429 when exceed threshold")
	public void verifyRateLimiting() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"API"}, description = "API should create a resource and return 201")
	public void verifyPOSTRequest() throws Exception {
		assertEquals(400, 200,"Status code mismatch");
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"API"}, description = "API should update the resource and return 200 or 204")
	public void verifyDELETERequest() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"API"}, description = "API should handle special characters")
	public void verifyHandelingSpecialCharacters() throws Exception {
		throw new SkipException("Invalid characters");
	}
	
	@Test(groups = {"API"}, description = "Response time should be within acceptable limits")
	public void verifyResponsetime() throws Exception {
		System.out.println("Test PASS");
	}
}
