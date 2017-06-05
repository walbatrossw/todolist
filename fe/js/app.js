(function (window) {
	'use strict';
	// Your starting point. Enjoy the ride!
	$(document).ready(function() {
		
		// 전체 리스트 출력
		listAllTodos();
		
		// 1. 할 일 등록하기
		$(".new-todo").keydown(function(e) {
			// enter키 입력감지
			if(e.keyCode == 13) {
				// 공백입력 금지
				var todo = $(".new-todo").val().trim();
				if(todo == "") {
					alert("공백은 입력할 수 없습니다.");
				} else {
					// POST 요청
					$.ajax({
						type: "post",
						url: "api/todos",
						headers: {
				            "Content-Type" : "application/json"
				        },
				        dateType: "json",
				        data: JSON.stringify({
				        	"todo" : todo
				        }),
				        success: function() {
				        	listAllTodos();		// 할 일 등록하기 요청 성공 후 리스트 출력
				        	renewFilterBtn()	// 필터링 상태에서 할 일 등록시 필터링 버튼상태 초기화
				        }
				         
					})
				}
			}
		});
		
		// 2. 할 일 리스트
		function listAllTodos() {
			// GET 요청
			$.ajax({
				type: "get",
				url: "api/todos",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
		        	list(e);		// 할 일 리스트 요청 성공 후 리스트 출력
		        	activeCount();	// 할 일 리스트 요청 성공 후 할 일 갯수 동기화
		        }
			});
		}
		
		// 3. 할 일 완료하기 (active -> completed, completed -> active)
		$(".todo-list").on("click", ".toggle", function() {
			var id = $(this).parent().parent().attr("id");
			var completed = $(this).parent().parent().attr("class");
			// 할 일과 완료한 일로 조건분기  
			// active-> completed
			if(completed == null) {
				// PUT 요청
				$.ajax({
					type: "put",
					url: "api/todos/"+id,
					headers: {
			            "Content-Type" : "application/json"
			        },
					dateType: "json",
					data: JSON.stringify({
						"completed": 1
					}),
					success: function(e) {
						listAllTodos();		// 할 일 완료하기 요청 성공 후 리스트 갱신
						activeCount();		// 할 일 완료하기 요청 성공 후 할 일 전체 갯수 동기화
						renewFilterBtn()	// 필터링 상태에서 할 일 완료시 필터링 버튼상태 초기화
					}
				});
			// completed -> active	
			} else {
				// PUT 요청
				$.ajax({
					type: "put",
					url: "api/todos/"+id,
					headers: {
			            "Content-Type" : "application/json"
			        },
					dateType: "json",
					data: JSON.stringify({
						"completed": 0
					}),
					success: function(e) {
						listAllTodos();		// 할 일 완료하기 후 리스트 갱신
						activeCount();		// 할 일 완료하기 후 할 일 전체 갯수 동기화
						renewFilterBtn()	// 필터링 상태에서 할 일로 변경시 필터링 버튼상태 초기화
					}
				});
			}
		});
		
		// 4. 할 일 삭제하기 
		$(".todo-list").on("click", ".destroy", function() {
			var id = $(this).parent().parent().attr("id");
			// DELETE 요청
			$.ajax({
				type: "delete",
				url: "api/todos/"+id,
				success: function() {
					listAllTodos();		// 삭제 요청 성공 후 리스트 갱신 
					activeCount();		// 삭제 요청 성공 후 할 일 전체 갯수 동기화
					renewFilterBtn();	// 필터링 상태에서 삭제시 필터링 버튼상태 초기화
				}
			});
			
		});
		
		// 5. 할일 전체 갯수 표시
		function activeCount() {
			// GET 요청
			$.ajax({
				type: "get",
				url: "api/todos/count",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
		        	var count = e;
		        	$(".todo-count").children().html(count);	// 할일 전체 갯수 요청 성공 후 count클래스에 갯수 출력
		        }
			});
		}
		
		// 6-1. 할 일 리스트를 필터링 (active)
		$(".filters").on("click", ".active", function() {
			event.preventDefault();	// a태그 기본효과 방지, click이벤트로 처리
			// GET요청
			$.ajax({
				type: "get",
				url: "api/todos/active",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
		        	list(e);	// 할 일 리스트 필터링 요청 성공 후 리스트(active) 출력
		        	// active 필터버튼으로 선택된 상태로 변경
					$(".selected").removeClass("selected");
					$(".active").addClass("selected");
		        }
			})
		});
		
		// 6-2. 할 일 리스트를 필터링 (completed)
		$(".filters").on("click", ".completed", function() {
			event.preventDefault(); // a태그 기본효과 방지, click이벤트로 처리
			// GET요청
			$.ajax({
				type: "get",
				url: "api/todos/completed",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
					list(e);// 완료한 일 리스트 필터링 요청 성공 후 리스트(completed) 출력
					// completed 필터버튼으로 선택된 상태로 변경
					$(".selected").removeClass("selected");
					$(".completed").addClass("selected");
				}
			})
		});
		
		// 7. 완료한 일 삭제 (일괄삭제)
		$(".clear-completed").click(function() {
			// DELETE 요청
			$.ajax({
				type: "delete",
				url: "api/todos",
				success: function() {
					listAllTodos();		// 완료한 일 삭제 요청 성공 후 전체 리스트 출력
					renewFilterBtn();	// 필터링 상태에서 일괄삭제시 버튼상태 초기화
				}
			});
		})
		
		// Todo 리스트 출력 함수
		function list(e) {
			var output = "";
			for(var i in e) {
				// 완료한 일과 할 일로 조건 분기하여 리스트 출력
				// 완료한 일
				if(e[i].completed === 1) { 
					var checked = 'checked';
					var completed = 'class="completed"';
					output += "<li "+completed+" id='"+e[i].id+"'>";
					output += 	"<div class='view'>";
					output += 		"<input class='toggle' type='checkbox'"+checked+"><label>"+e[i].todo+"</label><button class='destroy'></button>";
					output += 	"</div>";
					output += "</li>";
				// 할 일
				} else {
					output += "<li id='"+e[i].id+"'>";
					output += 	"<div class='view'>";
					output += 		"<input class='toggle' type='checkbox'><label>"+e[i].todo+"</label><button class='destroy'></button>";
					output += 	"</div>";
					output += "</li>";
				}
			}
			$(".todo-list").html(output);
		}
		
		// 필터버튼 초기화 함수
		function renewFilterBtn() {
			$(".active").removeClass("selected");
			$(".completed").removeClass("selected");
			$(".all").addClass("selected");
		}
	});
})(window);



