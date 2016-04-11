<?php
$input = file_get_contents("php://input"); //接收POST数据
$json = json_decode($input); //提取POST数据为simplexml对象

//var_dump($json);

try {
  $con = mysql_connect("mysql:host=localhost", 'root', 'root');
  mysql_select_db("my_db",$con);
}
catch (Exception $e) {
  echo json_encode(arr('code'=>'1994','value'=> 'connect failed'));
}

try{
  if($json['type']=='logon'){
    //账号与密码相同
    if($json['password']!=$json['name']){
    $pdo =new PDO("mysql:host=localhost;dbname=my_db","root","");
    if($pdo->exec("INSERT INTO Cinfo(name,pwd)VALUES('$json['name']','$json['password']")){
           echo json_encode(arr('code'=>'200','value'=>'logon success'));
       }
     else  echo json_encode(arr('code'=>'1996','value'=> 'Not allow the same name and password'));
     }

 }
  //catch(Exception $e){
  //echo json_encode(arr('code'=>'1998','value'=> 'name repeat'));   //账号已存在
  //}
  mysql_close($con);
