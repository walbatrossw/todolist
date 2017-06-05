(function (window) {
	'use strict';

	// Your starting point. Enjoy the ride!
	
	$(document).ready(function() {
		
		listAllTodo();
		
		// 1. 할 일 등록하기
		$(".new-todo").keydown(function(e) {
			if(e.keyCode == 13) {	// enter키 입력
				var todo = $(".new-todo").val().trim();
				if(todo == ""){
					alert("공백은 입력할 수 없습니다.");
				} else {
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
				        success: function(){
				            listAllTodo();
				        }
					})
				}
			}
		});
		
		// 2. 할 일 리스트
		function listAllTodo() {
			$.ajax({
				type: "get",
				url: "api/todos",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
		        	list(e);		
		        	activeCount();
		        }
			});
		}
		
		// 3. 할 일 완료하기 (active, completed)
		$(".todo-list").on("click", ".toggle", function() {
			var id = $(this).parent().parent().attr("id");
			var completed = $(this).parent().parent().attr("class");
			if(completed == null) {
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
					success: function(result) {
						listAllTodo();
						activeCount();
					}
				});
			} else {
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
					success: function(result) {
						listAllTodo();
						activeCount();
					}
				});
			}
		});
		
		// 4. 할 일 삭제하기 
		$(".todo-list").on("click", ".destroy", function() {
			var id = $(this).parent().parent().attr("id");
			$.ajax({
				type: "delete",
				url: "api/todos/"+id,
				success: function(result) {
					listAllTodo();
					activeCount();
					renewfilterBtn();
				}
			});
			
		});
		
		// 5. 할일 전체 갯수 표시
		function activeCount() {
			$.ajax({
				type: "get",
				url: "api/todos/count",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
		        	var count = e;
		        	$(".todo-count").children().html(count);
		        }
			});
		}
		
		// 6-1. 할 일 리스트를 필터링 (active)
		$(".filters").on("click", ".active", function() {
			event.preventDefault();
			$(".selected").removeClass("selected");
			$(".active").addClass("selected");
			$.ajax({
				type: "get",
				url: "api/todos/active",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
		        	list(e);
		        }
			})
		});
		
		// 6-2. 할 일 리스트를 필터링 (completed)
		$(".filters").on("click", ".completed", function() {
			event.preventDefault();
			$(".selected").removeClass("selected");
			$(".completed").addClass("selected");
			$.ajax({
				type: "get",
				url: "api/todos/completed",
				dateType: "json",
				headers: {
		            "Content-Type" : "application/json"
		        },
		        success: function(e) {
					list(e);
				}
			})
		});
		
		// 7. 완료한 일 삭제 (일괄삭제)
		$(".clear-completed").click(function() {
			$.ajax({
				type: "delete",
				url: "api/todos",
				success: function() {
					listAllTodo(); // 전체 리스트
					renewfilterBtn(); // 필터링 상태에서 일괄삭제시 버튼상태 초기화
				}
			});
		})
		
		// 리스트 출력 함수
		function list(e) {
			var output = "";
			for(var i in e) {
				// 완료한 일과 할 일로 조건 분기하여 리스트 출력  
				if(e[i].completed === 1) { // 완료한 일
					var checked = 'checked';
					var completed = 'class="completed"';
					output += "<li "+completed+" id='"+e[i].id+"'>";
					output += 	"<div class='view'>";
					output += 		"<input class='toggle' type='checkbox'"+checked+"><label>"+e[i].todo+"</label><button class='destroy'></button>";
					output += 	"</div>";
					output += "</li>";
				} else { // 할 일
					output += "<li id='"+e[i].id+"'>";
					output += 	"<div class='view'>";
					output += 		"<input class='toggle' type='checkbox'><label>"+e[i].todo+"</label><button class='destroy'></button>";
					output += 	"</div>";
					output += "</li>";
				}
			}
			$(".todo-list").html(output);
		}
		
		// 필터링 버튼 초기화 함수
		function renewfilterBtn() {
			$(".active").removeClass("selected");
			$(".completed").removeClass("selected");
			$(".all").addClass("selected");
		}
	});
})(window);



