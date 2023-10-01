const deleteBtn= document.getElementById('delete-Btn');
const readDeleteBtn= document.getElementById('readDelete-Btn');
const modifyBtn= document.getElementById('modify-Btn');
const registerBtn= document.getElementById('register-Btn');


console.log("article.js 진입");

if(deleteBtn){
deleteBtn.addEventListener('click', event => {
    console.log("article.js 삭제버튼 클릭 진입")

    var id = document.getElementById("id").value;

     function success(){
            console.log("article.js 삭제버튼 클릭 진입 success() 함수진입 ");
            alert("게시글 삭제가 완료되었습니다.");
            location.replace(`/articles`);
        }

        function fail(){
            console.log("article.js 삭제버튼 클릭 진입 fail() 함수진입 ");
            alert("게시글 삭제를 실패했습니다.");
            location.replace(`/articles`);
        }

        httpRequest("delete", "/api/articles/"+id, null, success, fail);


    /*fetch(`/api/articles/${id}`, {
        method:'delete'
    })
    .then( () =>{
        console.log("article.js 삭제버튼 클릭 진입 then() 진입")

        alert(id+ "번 게시글 삭제 완료되었습니다.");
        location.replace("/articles");
    });*/

});//deleteBtn
}//if


if(readDeleteBtn){
readDeleteBtn.addEventListener('click', event => {
    console.log("article.js 삭제버튼 클릭 진입")

    var id = document.getElementById("id").value;




     function success(){
            console.log("article.js 삭제버튼 클릭 진입 success() 함수진입 ");
            alert("게시글 삭제가 완료되었습니다.");
            location.replace(`/articles`);
        }

        function fail(){
            console.log("article.js 삭제버튼 클릭 진입 fail() 함수진입 ");
            alert("게시글 삭제를 실패했습니다.");
            location.replace(`/articles`);
        }

        httpRequest("delete", "/api/articles/"+id, null, success, fail);
/*

    fetch(`/api/articles/${id}`, {
        method:'delete'
    })
    .then( () =>{
        console.log("article.js 삭제버튼 클릭 진입 then() 진입")

        alert(id+ "번 게시글 삭제 완료되었습니다.");
        location.replace("/articles");
    });
*/

});//deleteBtn
}//if



if(modifyBtn){
modifyBtn.addEventListener('click', event => {
    console.log("article.js 수정버튼 클릭 진입")

    //var params = new URLSearchParams(location.search);
    //var id = params.get('id');

    var id = document.getElementById("id").value;

 /*
    var title = document.getElementById("title").value;
    var content =document.getElementById("content").value;
    console.log("article.js 수정버튼 클릭 진입 title -> "+ title);
    console.log("article.js 수정버튼 클릭 진입 content -> "+ content);
*/
     body =JSON.stringify({
         title : document.getElementById("title").value,
         content : document.getElementById("content").value

     });


     function success(){
            console.log("article.js 수정버튼 클릭 진입 success() 함수진입 ");
            alert("게시글 수정 완료되었습니다.");
            location.replace(`/articles`);
        }

        function fail(){
            console.log("article.js 수정버튼 클릭 진입 fail() 함수진입 ");
            alert("게시글 수정을 실패했습니다.");
            location.replace(`/articles`);
        }

        httpRequest("put", "/api/articles/"+id, body, success, fail);
/*


    fetch(`/api/articles/${id}`, {
        method:'put',
        headers:{
            "Content-Type":"application/json"
        },
        body: JSON.stringify({
            title:title,
            content:content
        })
    })
    .then( () =>{
        console.log("article.js 수정버튼 클릭 진입 then() 진입")

        alert(id+ "번 게시글 수정 완료되었습니다.");
        location.replace(`/articles`);
    });
*/

});//modifyBtn
}//if


if(registerBtn){
registerBtn.addEventListener('click', event => {
    console.log("article.js 등록버튼 클릭 진입");

        body= JSON.stringify({
            title:document.getElementById("title").value,
            content:document.getElementById("content").value
        });

        function success(){
            console.log("article.js 등록버튼 클릭 진입 success() 함수진입 ");
            alert("게시글 등록이 완료되었습니다.");
            location.replace(`/articles`);
        }

        function fail(){
            console.log("article.js 등록버튼 클릭 진입 fail() 함수진입 ");
            alert("게시글 등록을 실패했습니다.");
            location.replace(`/articles`);
        }

        httpRequest("post", "/api/articles", body, success, fail);



   /* fetch(`/api/articles`, {
        method:'post',
        headers:{
            "Content-Type":"application/json"
        },
        body: JSON.stringify({
            title:document.getElementById("title").value,
            content:document.getElementById("content").value
        })
    })
    .then( () =>{
        console.log("article.js 등록버튼 클릭 진입 then() 진입")

        alert("게시글 등록 완료되었습니다.");
        location.replace("/articles");
    });
*/
});//registerBtn
}//if

function getCookie(key){
    console.log("article.js getCookie() 진입 key -> "+ key);

    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item){
        item = item.replace(" ", "");

        var dic = item.split("=");
        if(key === dic[0]){
            result = dic[1];
            return true;
        }
    });

    return result;
}

function httpRequest(method, url, body, success, fail){
console.log("article.js httpRequest() 함수 진입");

    fetch(url, {
        method:method,
        headers:{
            Authorization: "Bearer "+localStorage.getItem("access_token"),
            "Content-Type": "application/json"
        },
        body:body
    })

    .then( (response) => {
            if(response.status=== 200 || response.status===201){
                return success();
            }

            var refresh_token = getCookie("refresh_token");

            if(response.status=== 401 && refresh_token){
                fetch("/api/token",{
                    method:"post",
                    headers:{
                        Authorization: "Bearer "+localStorage.getItem("access_token"),
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        refreshToken: getCookie("refresh_token")
                    }),
                })
                .then( (result) => {
                     localStorage.setItem("access_token", result.accessToken);//재발급성공시 새로운 엑세스 토큰 교체
                })
                .catch((error) => fail());
            }else{
                return fail();
            }
    });
}
