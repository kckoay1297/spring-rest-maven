package posmy.interview.boot.book;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import posmy.interview.boot.controller.book.BookController;
import posmy.interview.boot.entity.book.BookBean;
import posmy.interview.boot.entity.book.BookBorrowRecordBean;
import posmy.interview.boot.service.book.BookBorrowService;
import posmy.interview.boot.service.book.BookManageService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class)
@WithMockUser
public class BookControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	BookManageService bookManageService;

	@MockBean
	BookBorrowService bookBorrowService;

	BookBean mockBook1 = new BookBean(1, "World War Z", "Fiction", "AVAILABLE", "Global zomibe pandamic",
			getDate("12-Jan-2020"));
	BookBean mockBook2 = new BookBean(2, "Harry Potter and the Philosopher(s) Stone", "Fiction", "AVAILABLE",
			"Magical adventure with Harry Potter", getDate("18-Dec-2019"));
	BookBean mockBook3 = new BookBean(3, "Unbroken", "Non-Fiction", "AVAILABLE", "WW2 Survivor",
			getDate("25-Feb-2019"));
	BookBean mockBook4 = new BookBean(4, "English-Malay Dictionary", "Education", "BORROWED",
			"2018 Edition from Cambridge", getDate("25-Mar-2019"));
	List<BookBean> mockBookList = new ArrayList<>();

	BookBorrowRecordBean mockBookBorrowRecord1 = new BookBorrowRecordBean(1, 1, "RETURNED", getDate("18-Jun-2020"), getDate("18-Jul-2020"), 1);
	BookBorrowRecordBean mockBookBorrowRecord2 = new BookBorrowRecordBean(2, 4, "BORROWED", getDate("18-Aug-2020"), null, 1);

	@Test
	public void readBook() throws Exception {
		String expectedResult = "{\"bookId\":1,\"bookName\":\"World War Z\",\"type\":\"Fiction\",\"status\":\"AVAILABLE\",\"description\":\"Global zomibe pandamic\",\"createdDate\":\"2020-01-11T16:00:00.000+00:00\"}";

		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(mockBook1);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/read/1").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Test readBook=" + result.getResponse());

		JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), false);
	}

	@Test
	public void findAllBook() throws Exception {
		String expectedResult = "[{\"bookId\":1,\"bookName\":\"World War Z\",\"type\":\"Fiction\",\"status\":\"AVAILABLE\",\"description\":\"Global zomibe pandamic\",\"createdDate\":\"2020-01-11T16:00:00.000+00:00\"},{\"bookId\":2,\"bookName\":\"Harry Potter and the Philosopher(s) Stone\",\"type\":\"Fiction\",\"status\":\"AVAILABLE\",\"description\":\"Magical adventure with Harry Potter\",\"createdDate\":\"2019-12-17T16:00:00.000+00:00\"},{\"bookId\":3,\"bookName\":\"Unbroken\",\"type\":\"Non-Fiction\",\"status\":\"AVAILABLE\",\"description\":\"WW2 Survivor\",\"createdDate\":\"2019-02-24T16:00:00.000+00:00\"},{\"bookId\":4,\"bookName\":\"English-Malay Dictionary\",\"type\":\"Education\",\"status\":\"BORROWED\",\"description\":\"2018 Edition from Cambridge\",\"createdDate\":\"2019-03-24T16:00:00.000+00:00\"}]";
		mockBookList.add(mockBook1);
		mockBookList.add(mockBook2);
		mockBookList.add(mockBook3);
		mockBookList.add(mockBook4);

		Mockito.when(bookManageService.findAllBook()).thenReturn(mockBookList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/find-all").accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), false);
	}

	@Test
	@WithMockUser(username = "librarian", password = "password", roles = "LIBRARIAN")
	public void librarianCreateBook() throws Exception {
		BookBean bookCreation = new BookBean(8, "Detective Conan Vol 58", "Comic", "AVAILABLE",
				"Genius child detective trying to solve murder case.", getDate("01-Jan-2019"));
		String exampleBookJson = "{\"bookName\": \"Detective Conan Vol 58\"," + "\"type\": \"Comic\","
				+ "\"status\": \"AVAILABLE\","
				+ "\"description\": \"Genius child detective trying to solve murder case.\"}";

		String expectedResult = "{\"bookId\":8,\"bookName\":\"Detective Conan Vol 58\",\"type\":\"Comic\",\"status\":\"AVAILABLE\",\"description\":\"Genius child detective trying to solve murder case.\",\"createdDate\":\"2018-12-31T16:00:00.000+00:00\"}";
		Mockito.when(bookManageService.createBook(Mockito.any(BookBean.class))).thenReturn(bookCreation);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/create").accept(MediaType.APPLICATION_JSON)
				.content(exampleBookJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		System.out.println("Test librarianCreateBook=" + response);
		JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), false);
	}

	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void memberCreateBook() throws Exception {
		BookBean bookCreation = new BookBean(8, "Detective Conan Vol 58", "Comic", "AVAILABLE",
				"Genius child detective trying to solve murder case.", getDate("01-Jan-2019"));
		String exampleBookJson = "{\"bookName\": \"Detective Conan Vol 58\"," + "\"type\": \"Comic\","
				+ "\"status\": \"AVAILABLE\","
				+ "\"description\": \"Genius child detective trying to solve murder case.\"}";

		Mockito.when(bookManageService.createBook(Mockito.any(BookBean.class))).thenReturn(bookCreation);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/create").accept(MediaType.APPLICATION_JSON)
				.content(exampleBookJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		System.out.println("Test memberCreateBook=" + response.getContentAsString());
		JSONAssert.assertEquals(String.valueOf(HttpStatus.FORBIDDEN.value()), String.valueOf(response.getStatus()),
				false);
	}

	@Test
	@WithMockUser(username = "librarian", password = "password", roles = "LIBRARIAN")
	public void librarianDeleteBook() throws Exception {
		Mockito.when(bookManageService.deleteBook(Mockito.anyInt())).thenReturn(mockBook1);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/book/delete").accept(MediaType.APPLICATION_JSON)
				.param("bookId", "1");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Test librarianDeleteBook=" + result.getResponse());

		JSONAssert.assertEquals(String.valueOf(HttpStatus.OK.value()), String.valueOf(result.getResponse().getStatus()),
				false);
	}

	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void memberDeleteBook() throws Exception {
		Mockito.when(bookManageService.deleteBook(Mockito.anyInt())).thenReturn(mockBook1);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/book/delete").accept(MediaType.APPLICATION_JSON)
				.param("bookId", "1");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		System.out.println("Test memberDeleteBook=" + result.getResponse());

		JSONAssert.assertNotEquals(String.valueOf(HttpStatus.OK.value()),
				String.valueOf(result.getResponse().getStatus()), false);
	}

	@Test
	@WithMockUser(username = "librarian", password = "password", roles = "LIBRARIAN")
	public void librarianUpdateBook() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/update").accept(MediaType.APPLICATION_JSON)
				.param("bookId", "1").param("bookName", "World War Zombie");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test librarianUpdateBook=" + result.getResponse());

		JSONAssert.assertEquals(String.valueOf(HttpStatus.OK.value()), String.valueOf(result.getResponse().getStatus()),
				false);
	}

	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void borrowBookFail() throws Exception {
		borrowBookNotFound();
		borrowBookIsNotAvailable();
	}

	private void borrowBookNotFound() throws Exception {
		String bookId = "14";
		String customerId = "1";
		String expectedResult = "{\"fail\":\"Book 14 not found.\"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(null);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/borrow").accept(MediaType.APPLICATION_JSON)
				.param("customerId", customerId).param("bookId", bookId);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test borrowBookFail=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);
	}
	
	private void borrowBookIsNotAvailable() throws Exception {
		String bookId = "4";
		String customerId = "1";
		String expectedResult = "{\"fail\":\"Book 4 is already borrowed.\"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(mockBook4);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/borrow").accept(MediaType.APPLICATION_JSON)
				.param("customerId", customerId).param("bookId", bookId);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test borrowBookFail=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void borrowBookSuccess() throws Exception {
		String bookId = "1";
		String customerId = "1";
		String expectedResult = "{\"success\":\"Book borrow record 1 created.\"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(mockBook1);
		Mockito.when(bookBorrowService.isBookAvailable(Mockito.any(BookBean.class))).thenReturn(true);
		Mockito.when(bookBorrowService.borrowBookUpdateStatus(Mockito.any(BookBean.class), Mockito.anyInt())).thenReturn(1);
				
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/borrow").accept(MediaType.APPLICATION_JSON)
				.param("customerId", customerId).param("bookId", bookId);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test borrowBookSuccess=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);
	}
	

	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void bookNotFoundReturn() throws Exception {
		String bookId = "14";
		String customerId = "1";
		String bookBorrowRecordId = "1";
		String expectedResult = "{\"fail\":\"Book 14 not found.\"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(null);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/return").accept(MediaType.APPLICATION_JSON)
				.param("bookBorrowRecordId", bookBorrowRecordId).param("customerId", customerId).param("bookId", bookId);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test bookNotFoundReturn=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void bookRecordNotFound() throws Exception {
		String bookId = "1";
		String customerId = "1";
		String bookBorrowRecordId = "1";
		String expectedResult = "{\"fail\":\"Book borrow record 1 not found.\"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(mockBook1);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/return").accept(MediaType.APPLICATION_JSON)
				.param("bookBorrowRecordId", bookBorrowRecordId).param("customerId", customerId).param("bookId", bookId);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test bookRecordNotFound=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void bookIsAlreadyReturned() throws Exception {
		String bookId = "1";
		String customerId = "1";
		String bookBorrowRecordId = "1";
		String expectedResult = "{\"fail\":\"Book borrow record 1 not found.\"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(mockBook1);
		Mockito.when(bookBorrowService.isBookBorrowRecordActive(Mockito.anyInt())).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/return").accept(MediaType.APPLICATION_JSON)
				.param("bookBorrowRecordId", bookBorrowRecordId).param("customerId", customerId).param("bookId", bookId);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test bookIsAlreadyReturned=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);

	}
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void returnBookCustomerIdNotMatched() throws Exception {
		String bookId = "4";
		String customerId = "3";
		String bookBorrowRecordId = "2";
		String expectedResult = "{\"fail\":\"Invalid customer id 3. \"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(mockBook1);
		Mockito.when(bookBorrowService.isBookBorrowRecordActive(Mockito.anyInt())).thenReturn(true);
		Mockito.when(bookBorrowService.updateBookRecordOnReturn(Mockito.any(BookBean.class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/return").accept(MediaType.APPLICATION_JSON)
				.param("bookBorrowRecordId", bookBorrowRecordId).param("customerId", customerId).param("bookId", bookId);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test returnBookCustomerIdNotMatched=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);
	}
	
	@Test
	@WithMockUser(username = "member", password = "password", roles = "MEMBER")
	public void bookReturnSuccess() throws Exception {
		String bookId = "4";
		String customerId = "1";
		String bookBorrowRecordId = "2";
		String expectedResult = "{\"success\":\"Book 4 is returned, record 2 is updated.\"}";
		Mockito.when(bookManageService.readBook(Mockito.anyInt())).thenReturn(mockBook1);
		Mockito.when(bookBorrowService.isBookBorrowRecordActive(Mockito.anyInt())).thenReturn(true);
		Mockito.when(bookBorrowService.updateBookRecordOnReturn(Mockito.any(BookBean.class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(mockBookBorrowRecord2);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/return").accept(MediaType.APPLICATION_JSON)
				.param("bookBorrowRecordId", bookBorrowRecordId).param("customerId", customerId).param("bookId", bookId);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		System.out.println("Test bookReturnSuccess=" + result.getResponse());
		JSONAssert.assertEquals(expectedResult,result.getResponse().getContentAsString(), false);
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
