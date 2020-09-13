package posmy.interview.boot.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import posmy.interview.boot.controller.user.UserController;
import posmy.interview.boot.entity.account.UserBean;
import posmy.interview.boot.service.account.UserManageService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@WithMockUser
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	UserManageService userManageService;
	
	UserBean mockUser1 = new UserBean(1, "John Doe", "MEMBER", "active", getDate("12-Aug-2018"));
	UserBean mockUser2 = new UserBean(2, "Ali Abu", "MEMBER", "active", getDate("02-Aug-2018"));
	UserBean mockUser3 = new UserBean(3, "Karen Maria", "LIBRARIAN", "active", getDate("25-May-2017"));
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void readUser() throws Exception {
		String expectedResult = "{\"userId\":1,\"accountName\":\"John Doe\",\"role\":\"MEMBER\",\"status\":\"active\",\"joinDate\":\"2018-08-11T16:00:00.000+00:00\"}";
		Mockito.when(userManageService.getUser(Mockito.anyInt())).thenReturn(mockUser1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/read").accept(MediaType.APPLICATION_JSON).param("userId", "1");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Test readUser=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void deleteUserByMember() throws Exception {
		Mockito.when(userManageService.getUser(Mockito.anyInt())).thenReturn(mockUser1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/delete").accept(MediaType.APPLICATION_JSON)
				.param("userId", "1").param("targetDeleteUserId", "1");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test deleteUserByMember=" + result.getResponse());
		JSONAssert.assertEquals(String.valueOf(HttpStatus.OK.value()), String.valueOf(result.getResponse().getStatus()),
				false);
	}
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void deleteUserByNonLibrarian() throws Exception {
		Mockito.when(userManageService.getUser(Mockito.anyInt())).thenReturn(mockUser2);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/delete").accept(MediaType.APPLICATION_JSON)
				.param("userId", "1").param("targetDeleteUserId", "2");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test deleteUserByNonLibrarian=" + result.getResponse());
		JSONAssert.assertNotEquals(String.valueOf(HttpStatus.OK.value()), String.valueOf(result.getResponse().getStatus()),
				false);
	}
	
	@Test
	@WithMockUser(username = "librarian", password = "password", roles = "LIBRARIAN")
	public void deleteUserByLibrarian() throws Exception {
		Mockito.when(userManageService.getUser(Mockito.anyInt())).thenReturn(mockUser3);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/delete").accept(MediaType.APPLICATION_JSON)
				.param("userId", "3").param("targetDeleteUserId", "2");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test deleteUserByLibrarian=" + result.getResponse());
		JSONAssert.assertEquals(String.valueOf(HttpStatus.OK.value()), String.valueOf(result.getResponse().getStatus()),
				false);
	}
	
	private static Date getDate(String dateString) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
