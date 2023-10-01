console.log("token.js 진입");

var token = searchParam("token");

console.log("token.js 진입 token -> "+ token );

if(token){
localStorage.setItem("access_token", token);
}

function searchParam(key){
console.log("token.js searchParam() 함수진입 key -> "+ key );
 return new URLSearchParams(location.search).get(key);

}