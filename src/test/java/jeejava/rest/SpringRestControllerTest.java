package jeejava.rest;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.model.User;
import com.rest.SpringRestController;
import com.sevice.UserService;

@RunWith(MockitoJUnitRunner.class)
public class SpringRestControllerTest {
	@Mock
	private UserService service;

	private MockMvc mockMvc;

	@Spy
	@InjectMocks
	private SpringRestController controller = new SpringRestController();
	
	private static AtomicInteger counter = new AtomicInteger();

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testGetUserById() throws Exception {
		User user = new User();
		user.setId(1);
		user.setName("Juan");
		user.setEmail("Juan@marmol.com");
		user.setAddress("Peru 2464");
		user.setPhone("+5491154762312");
		user.setUserType("Normal");
		user.setMoney(1234.00);
		Mockito.when(service.findUserById(Mockito.anyInt())).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/get-user-by-id/1")).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
	}

	@Test
	public void testSaveUser() throws Exception {
		User user = new User();
		user.setId(1);
		user.setName("Juan");
		user.setEmail("Juan@marmol.com");
		user.setAddress("Peru 2464");
		user.setPhone("+5491154762312");
		user.setUserType("Normal");
		user.setMoney(1234.00);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(user);
		Mockito.when(service.isUserAvailable(Mockito.any(User.class))).thenReturn(false);
		Mockito.doNothing().when(service).saveUser(Mockito.any(User.class));
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create-user").content(jsonString)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
		Assert.assertEquals(201, result.getResponse().getStatus());
	}

	@Test
	public void testUpdateUser() throws Exception {
		User user = new User();
		user.setId(1);
		user.setName("Juan");
		user.setEmail("Juan@marmol.com");
		user.setAddress("Peru 2464");
		user.setPhone("+5491154762312");
		user.setUserType("Normal");
		user.setMoney(1234.00);
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(user);
		Mockito.when(service.findUserById(Mockito.anyInt())).thenReturn(user);
		Mockito.doNothing().when(service).updateUser(Mockito.any(User.class));
		mockMvc.perform(
				MockMvcRequestBuilders.put("/api/v1/update-user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(200));
	}

	@Test
	public void testDeleteUserByid() throws Exception {
		User user = new User();
		user.setId(1);
		user.setName("Juan");
		user.setEmail("Juan@marmol.com");
		user.setAddress("Peru 2464");
		user.setPhone("+5491154762312");
		user.setUserType("Normal");
		user.setMoney(1234.00);
		Mockito.when(service.findUserById(Mockito.anyInt())).thenReturn(user);
		Mockito.doNothing().when(service).deleteUserById(Mockito.anyInt());
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete-user/1")).andExpect(MockMvcResultMatchers.status().is(200));
	}
}