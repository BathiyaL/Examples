package testscripts;

import org.testng.annotations.Test;

public class UserLoginTest {

	@Test(groups = {"UILogin"}, description = "Login with valid username and password")
	public void verifyValidLogin() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"UILogin"}, description = "Login with valid username and invalid password")
	public void verifyInvalidLogin_password() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"UILogin"}, description = "Login with invalid username and valid password")
	public void verifyInvalidLogin_username() throws Exception {
		System.out.println("Test PASS");
	}
	
	@Test(groups = {"UILogin"}, description = "Login with invalid username and password")
	public void verifyInvalidLogin_both() throws Exception {
		System.out.println("Test PASS");
	}
}
