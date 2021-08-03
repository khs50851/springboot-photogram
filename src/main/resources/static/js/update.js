// (1) 회원정보 수정
function update(userId,event) {
	event.preventDefault(); // 폼태그 액션 막음(method가 비면 submit시 자동으로 자기 자신한테 돌아오는 성질)

	let data=$("#profileUpdate").serialize();
	console.log(data);
	
	$.ajax({
		type:"put",
		url:`/api/user/${userId}`,
		data:data,
		contentType:"application/x-www-form-urlencoded; charset=utf-8",
		dataType:"json"
	}).done(res=>{ // HttpStatus 상태코드 200번대
		alert('회원정보가 변경 되었습니다!');
		console.log("성공",res);
		location.href=`/user/${userId}`;
		
	}).fail(error=>{ // HttpStatus 상태코드 200이 아닐 때
		if(error.data==null){
			alert(error.responseJSON.message);
		}else{
			console(error);
			alert(JSON.stringify(error.responseJSON.data));
		}
		
	});
}