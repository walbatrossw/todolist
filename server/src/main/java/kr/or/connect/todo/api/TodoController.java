package kr.or.connect.todo.api;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.or.connect.todo.domain.Todo;
import kr.or.connect.todo.service.TodoService;


@RestController
@RequestMapping("api/todos")
public class TodoController {
	
	private final Logger log = LoggerFactory.getLogger(TodoController.class);
	
	private final TodoService service;
	
	@Autowired
	public TodoController(TodoService service) {
		this.service = service;
	}

	// 1. 할 일 등록하기
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Todo create(@RequestBody Todo todo) {
		Todo newTodo = service.create(todo);
		log.info("Todo created : {}", newTodo);
		return todo;
	}

	// 2. 할 일 리스트
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	Collection<Todo> readList() {
		return service.findAll();
	}

	// 3. 할 일 완료하기
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void update(@PathVariable Integer id, @RequestBody Todo todo) {
		todo.setId(id);
		service.update(todo);
		log.info("Todo Updated : {}", todo);
		
	}

	// 4. 할 일 삭제하기 (x button click)
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void delete(@PathVariable Integer id) {
		service.delete(id);
		log.info("Todo Deleted : {}", id);
	}

	// 5. 할 일 전체 갯수 표시
	@GetMapping("/count")
	@ResponseStatus(HttpStatus.OK)
	int countActive() {
		int count = service.countActive();
		log.info("Todo CountActive : {}", count);
		return count;
	}
	
	// 6-1. 할 일 리스트 필터링 (active click)
	@GetMapping("/active")
	@ResponseStatus(HttpStatus.OK)
	Collection<Todo> readListActive() {
		Collection<Todo> active = service.findActive();
		log.info("Todo active : {}", active);
		return active;
	}
	
	// 6-2. 할 일 리스트 필터링 (completed click)
	@GetMapping("/completed")
	@ResponseStatus(HttpStatus.OK)
	Collection<Todo> readListCompleted() {
		Collection<Todo> completed = service.findCompleted();
		log.info("Todo completed : {}", completed);
		return completed;
	}
	
	// 7. 완료한 일 삭제 (clear completed click)
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteByCompleted() {
		boolean deletedTodos = service.deleteByCompleted();
		log.info("Todo deletedTodos : {}", deletedTodos);
	}
}
