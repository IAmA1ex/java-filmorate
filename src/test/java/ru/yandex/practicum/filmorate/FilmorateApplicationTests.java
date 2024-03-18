package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FilmController filmController;

	@Autowired
	private UserController userController;

	@BeforeEach
	public void setUp() {
		filmController.counterId = 1;
		filmController.films.clear();

		userController.counterId = 1;
		userController.users.clear();
	}

	@Test
	void userCreateTest() throws Exception {

		String json1 = "{\n" +
				"  \"login\": \"dolore\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"mail@mail.ru\",\n" +
				"  \"birthday\": \"1946-08-20\"\n" +
				"}";

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json1)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string((containsString("\"login\":\"dolore\""))))
				.andExpect(content().string((containsString("\"name\":\"Nick Name\""))))
				.andExpect(content().string((containsString("\"email\":\"mail@mail.ru\""))))
				.andExpect(content().string((containsString("\"birthday\":\"1946-08-20\""))))
				.andExpect(content().string((containsString("\"id\":1"))));
	}

	@Test
	void userValidationLoginTest() throws Exception {

		String jsonMissingLogin = "{\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"mail@mail.ru\",\n" +
				"  \"birthday\": \"1946-08-20\"\n" +
				"}";

		String jsonEmptyLogin = "{\n" +
				"  \"login\": \"\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"mail@mail.ru\",\n" +
				"  \"birthday\": \"1946-08-20\"\n" +
				"}";

		String jsonSpaceLogin = "{\n" +
				"  \"login\": \"Probel Tut\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"mail@mail.ru\",\n" +
				"  \"birthday\": \"1946-08-20\"\n" +
				"}";

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonMissingLogin)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonEmptyLogin)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonSpaceLogin)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	void userValidationEmailTest() throws Exception {

		String jsonMissingEmail = "{\n" +
				"  \"login\": \"dolore\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"birthday\": \"1946-08-20\"\n" +
				"}";

		String jsonEmptyEmail = "{\n" +
				"  \"login\": \"dolore\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"\",\n" +
				"  \"birthday\": \"1946-08-20\"\n" +
				"}";

		String jsonAEmail = "{\n" +
				"  \"login\": \"dolore\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"mail.ru\",\n" +
				"  \"birthday\": \"1946-08-20\"\n" +
				"}";

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonMissingEmail)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonEmptyEmail)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonAEmail)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	void userValidationBirthdayTest() throws Exception {

		String jsonMissingBirthday = "{\n" +
				"  \"login\": \"dolore\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"mail@mail.ru\",\n" +
				"}";

		String jsonPresentBirthday = String.format("{\n" +
				"  \"login\": \"dolore\",\n" +
				"  \"name\": \"Nick Name\",\n" +
				"  \"email\": \"mail@mail.ru\",\n" +
				"  \"birthday\": \"%s\"\n" +
				"}", LocalDate.now().plusDays(1).toString());

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonMissingBirthday)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonPresentBirthday)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	void filmCreateTest() throws Exception {

		String json1 = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": 100\n" +
				"}";

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json1)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string((containsString("\"name\":\"nisi eiusmod\""))))
				.andExpect(content().string((containsString("\"description\":\"adipisicing\""))))
				.andExpect(content().string((containsString("\"releaseDate\":\"1967-03-25\""))))
				.andExpect(content().string((containsString("\"duration\":100"))))
				.andExpect(content().string((containsString("\"id\":1"))));
	}

	@Test
	void filmValidationNameTest() throws Exception {

		String jsonMissingName = "{\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": 100\n" +
				"}";

		String jsonEmptyName = "{\n" +
				"  \"name\": \"\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": 100\n" +
				"}";

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonMissingName)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonEmptyName)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	void filmValidationDescriptionTest() throws Exception {

		StringBuilder testString = new StringBuilder();
        testString.append("a".repeat(200));

		String jsonMissingDescription = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": 100\n" +
				"}";

		String jsonShortDescription = String.format("{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"%s\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": 100\n" +
				"}", testString.toString());

		testString.append("1");
		String jsonLongDescription = String.format("{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"%s\",\n" +
				"  \"releaseDate\": \"1968-03-25\",\n" +
				"  \"duration\": 100\n" +
				"}", testString.toString());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonMissingDescription)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonShortDescription)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonLongDescription)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	void filmValidationReleaseDateTest() throws Exception {

		String jsonReleaseDateMissing = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"duration\": 100\n" +
				"}";

		String jsonReleaseDateOk = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1895-12-28\",\n" +
				"  \"duration\": 100\n" +
				"}";

		String jsonReleaseDateOld = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1895-12-27\",\n" +
				"  \"duration\": 100\n" +
				"}";

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonReleaseDateMissing)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonReleaseDateOk)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonReleaseDateOld)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

	@Test
	void filmValidationDurationTest() throws Exception {

		String jsonMissingDuration = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"}";

		String jsonOkDuration = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": 100\n" +
				"}";

		String jsonEmptyDuration = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": 0\n" +
				"}";

		String jsonNegativeDuration = "{\n" +
				"  \"name\": \"nisi eiusmod\",\n" +
				"  \"description\": \"adipisicing\",\n" +
				"  \"releaseDate\": \"1967-03-25\",\n" +
				"  \"duration\": -1\n" +
				"}";

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonMissingDuration)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonOkDuration)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonEmptyDuration)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonNegativeDuration)
						.characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}

}
