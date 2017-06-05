package kr.or.connect.todo.persistence;

public class TodoSqls {
	
	// 0. TodoDaoTest용 쿼리
	static final String SELECT_BY_ID = "SELECT id, todo, completed, date FROM todo WHERE id = :id";
	
	// 1. 할 일 등록하기
	// simpleJdbcInsert를 통해 구현
	
	// 2. 할 일 리스트
	static final String SELECT_ALL = "SELECT id, todo, completed, date FROM todo ORDER BY id DESC";
	
	// 3. 할일 완료하기
	static final String UPDATE = "UPDATE todo SET completed = :completed WHERE id = :id";
	
	// 4. 할일 삭제하기 (x button click)
	static final String DELETE_BY_ID = "DELETE FROM todo WHERE id= :id";
	
	// 5. 할일 전체 갯수 표시
	static final String COUNT_ACTIVE = "SELECT COUNT(*) FROM todo WHERE completed = 0";
	
	// 6. 할일 리스트 (active) 
	static final String SELECT_ACTIVE = "SELECT id, todo, completed, date FROM todo WHERE completed = 0 ORDER BY id DESC";
	
	// 7. 할일 리스트 (completed)
	static final String SELECT_COMPLETED = "SELECT id, todo, completed, date FROM todo WHERE completed = 1 ORDER BY id DESC";
	
	// 8. 완료한 일 삭제 (clear completed click)
	static final String DELETE_COMPLETE_CLEAR = "DELETE FROM todo WHERE completed = 1";
	
}
