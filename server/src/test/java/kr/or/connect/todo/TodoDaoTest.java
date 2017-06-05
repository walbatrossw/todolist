package kr.or.connect.todo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.todo.domain.Todo;
import kr.or.connect.todo.persistence.TodoDao;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TodoDaoTest {
	
	@Autowired
	private TodoDao dao;
	
	// 1. 할 일 등록하기 테스트
	@Test
	public void shouldInsertAndSelect() {
		// given
		Todo todo = new Todo("자바공부하기");
		
		// when
		Integer id = dao.insert(todo);
		
		// then
		Todo selected = dao.selectById(id);
		System.out.println("Insert selected : " + selected);
		assertThat(selected.getTodo(), is("자바공부하기"));
		
	}
	
	// 2. 할 일 리스트 테스트
	@Test
	public void shouldSelectAll() {
		// given
		for (int i = 0; i < 3; i++) {
			Todo todo = new Todo("스프링 공부하기"+i);
			dao.insert(todo);
		}
		// when
		List<Todo> allTodos = dao.selectAll();
		
		// then
		System.out.println("Select allTodos : " + allTodos);
		assertThat(allTodos, is(notNullValue()));
	}
	
	// 3. 할 일 완료하기 테스트
	@Test
	public void shouldUpdate() {
		// given
		Todo todo = new Todo("자바스크립트 공부하기");
		Integer id = dao.insert(todo);
		
		// when
		todo.setId(id);
		todo.setCompleted(1);
		int affected = dao.update(todo);
		
		// then
		assertThat(affected, is(1));
		Todo updated = dao.selectById(id);
		System.out.println("Update todo : " + updated);
		assertThat(updated.getCompleted(), is(1));
	}
	
	// 4. 할 일 삭제하기 테스트
	@Test
	public void shouldDelete() {
		// given
		Todo todo = new Todo("자료구조 공부하기");
		Integer id = dao.insert(todo);
		
		// when
		int affected = dao.deleteById(id);
		
		// then
		System.out.println("Delete todo: " + affected);
		assertThat(affected, is(1));
	}
	
	// 5. 할 일 전체 갯수 표시 테스트
	@Test
	public void shouldActiveCount() {
		// given
		for (int i = 0; i < 3; i++){
			Todo todo = new Todo("운영체제 공부하기"+i);
			dao.insert(todo);
		}
		
		// when
		int count = dao.countActive();
		
		// then
		System.out.println("Count : " + count);
		assertThat(count, is(notNullValue()));
	}
	
	
	// 6-1. 할 일 리스트 필터링 테스트 - active
	@Test
	public void shouldSelectActive() {
		// given
		for (int i = 0; i < 3; i++){
			Todo todo = new Todo("알고리즘 공부하기"+i);
			dao.insert(todo);
		}
		
		// when
		List<Todo> activeTodos = dao.selectActive();
		
		// then
		System.out.println("Select activeTodos : " + activeTodos);
		assertThat(activeTodos, is(notNullValue()));
	}
	
	// 6-2. 할 일 리스트 필터링 테스트 - completed
	@Test
	public void shouldSelectCompleted() {
		// given
		for (int i = 0; i < 3; i++){
			Todo todo = new Todo("데이터베이스 공부하기");
			Integer id = dao.insert(todo);
			todo.setId(id);
			todo.setCompleted(1);
			dao.update(todo);
		}
		
		// when
		List<Todo> completedTodos = dao.selectCompleted();
		
		// then
		System.out.println("Select completedTodos : " + completedTodos);
		assertThat(completedTodos, is(notNullValue()));
	}
	
	// 7. 완료한 일 삭제 테스트
	@Test
	public void shouldDeleteCompleted() {
		// given
		for (int i = 0; i < 3; i++){
			Todo todo = new Todo("네트워크 공부하기"+i);
			Integer id = dao.insert(todo);
			todo.setId(id);
			todo.setCompleted(1);
			dao.update(todo);
		}
		
		// when
		int affected = dao.deleteByCompleted();
		
		// then
		System.out.println("Delete completedTodos : " + affected);
		assertThat(affected, is(3));
	}
	
}
