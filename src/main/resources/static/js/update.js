// (1) 회원정보 수정
function update(userId,event) {
	

	let data=$("#profileUpdate").serialize();
	console.log(data);
	
	$.ajax({
		type:"put",
		url:`/api/user/${userId}`,
		data:data,
		contentType:"application/x-www-form-urlencoded; charset=utf-8",
		dataType:"json"
	}).done(res=>{
		alert('회원정보가 변경 되었습니다!');
		console.log("update 성공");
		console.log(res);
		location.href=`/user/${userId}`;
		
	}).fail(error=>{
		console.log("update 실패");
	});
}