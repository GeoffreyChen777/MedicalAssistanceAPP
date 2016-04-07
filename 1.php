<?php
$input = file_get_contents("php://input"); //接收POST数据
$json = json_decode($input); //提取POST数据为simplexml对象
//var_dump($json);

try {
  $con = mysql_connect("mysql:host=localhost", 'root', 'root');
  mysql_select_db("my_db",$con)
}
catch (Exception $e) {
  echo 'connect failed';
}

try{
  if($json['type']=='logon'){
    $str=((?=[\x21-\x7e]+)[^A-Za-z0-9]);    //特殊字符串的正则表达式？不知道对不对 网上查的
    $num=strlen($json['password']);
    //不允许出现特殊字符串
    if(strstr($str,'json['name']')==null){
    //账号与密码相同
    if($json['password']!=$json['name']){
    //密码长度过短
    if($num>6){
    mysql_query("INSERT INTO Cmessage(name,pwd)VALUES('$json['name']','$json['password']'))
      }
      else json_encode(arr('code'=>'1995','value'=> 'Password too short'));
     }
     else  json_encode(arr('code'=>'1996','value'=> 'Not allow the same name and password'));
     }
     else  json_encode(arr('code'=>'1997','value'=> 'Do not use special chars'));

   }
 }
  catch(Exception $e){
  echo json_encode(arr('code'=>'1998','value'=> 'name repeat'));   //账号已存在
  }
  mysql_close($con);
