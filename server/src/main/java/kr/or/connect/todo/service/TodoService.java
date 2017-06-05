package kr.or.connect.todo.service;

import java.util.Collection;


import org.springframework.stereotype.Service;

import kr.or.connect.todo.domain.Todo;
import kr.or.connect.todo.persistence.TodoDao;

@Service
public class TodoService {
	
	private TodoDao dao;
	
	public TodoService(TodoDao dao) {
		this.dao = dao;
	}
	
	// 1. 할 일 등록하기
	public Todo create(Todo todo) {
		Integer id = dao.insert(todo);
		todo.setId(id);
		return todo;
	}

	// 2. 할 일 리스트
	public Collection<Todo> findAll() {
		return dao.selectAll();
	}

	// 3. 할 일 완료하기
	public boolean update(Todo todo) {
		int affected = dao.update(todo);
		return affected == 1;
	}

	// 4. 할 일 삭제하기 (x button click)
	public boolean delete(Integer id) {
		int affected = dao.deleteById(id);
		return affected == 1;
	}
	
	// 5. 할 일 전체 갯수 표시
	public int countActive() {
		return dao.countActive();
	}
	
	// 6-1. 할 일 리스트 필터링 (active click)
	public Collection<Todo> findActive() {
		return dao.selectActive();
	}
	
	// 6-2. 할 일 리스트 필터링 (completed click)
	public Collection<Todo> findCompleted() {
		return dao.selectCompleted();
	}
	
	// 7. 완료한 일 삭제 (clear completed click)
	public boolean deleteByCompleted() {
		int affected = dao.deleteByCompleted();
		return affected == 1;
	}
}
