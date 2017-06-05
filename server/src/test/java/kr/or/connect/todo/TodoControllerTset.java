package kr.or.connect.todo;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoControllerTset {

	@Autowired
	WebApplicationContext wac;
	MockMvc mvc;
	
	@Before
	public void setUp() {
		this.mvc = webAppContextSetup(this.wac)
				.alwaysDo(print(System.out))
				.build();
	}
	
	
	// 1. 할 일 등록하기 테스트
	@Test
	public void shouldCreate() throws Exception {
		String requestBody = "{\"todo\":\"자바공부\"}";
		
		mvc.perform(
				post("/api/todos/")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.todo").value("자바공부"));
	}
	
	// 2. 할 일 리스트 테스트
	@Test
	public void shouldSelectAll() throws Exception {
		mvc.perform(
				get("/api/todos/")
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}
	
	// 3. 할 일 완료하기 테스트
	@Test
	public void shouldUpdate() throws Exception {
		String requestBody = "{\"todo\":\"자바공부\", \"completed\": 1}";
		
		mvc.perform(
				put("/api/todos/1")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestBody)
				)
				.andExpect(status().isNoContent());
	}
	
	// 4. 할 일 삭제하기 테스트
	@Test
	public void shouldDelete() throws Exception {
		mvc.perform(
				delete("/api/todos/1")
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isNoContent());
	}
	
	// 5. 할 일 전체 갯수 표시 테스트
	@Test
	public void shouldCountActive() throws Exception {
		mvc.perform(
				get("/api/todos/count")
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}
	
	// 6-1. 할 일 리스트 필터링 테스트
	@Test
	public void shouldSelectActive() throws Exception {
		mvc.perform(
				get("/api/todos/active")
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}
	
	// 6-2. 할 일 리스트 필터링 테스트
	@Test
	public void shouldSelectCompleted() throws Exception {
		mvc.perform(
				get("/api/todos/completed")
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk());
	}
	
	// 7. 완료한 일 삭제 테스트
	@Test
	public void shouldDeleteAllCompleted() throws Exception {
		mvc.perform(
				delete("/api/todos/")
					.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isNoContent());
	}
	
}
