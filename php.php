<?php
$input = file_get_contents("php://input"); //接收POST数据
$str=explode("&",$input);
$type=explode("=",$str[0]);
$pwd =explode("=",$str[1]);
$name=explode("=",$str[2]);
//var_dump($input);

try {
  $con = mysql_connect("mysql:host=localhost", 'root', 'root');
  mysql_select_db("Userinfo",$con);
}
catch (Exception $e) {
  echo json_encode(arr('code'=>'1994','value'=> 'connect failed'));
}

try{
  if($type[1]=="signup"){
    //账号与密码相同
    if($pwd[1]!=$name[1]){
      $pdo =new PDO("mysql:host=localhost;dbname=Userinfo",'root','root');
      if($pdo->exec("INSERT INTO userinfo (account,pwd) VALUES(".$name[1].",".$pwd[1].");")){
           echo json_encode(arr('code'=>'200','value'=>'signup success'));
      }
      else
        echo json_encode(arr('code'=>'1996','value'=> 'Not allow the same name and password'));
    }

 }
 else{
   $pdo =new PDO("mysql:host=localhost;dbname=Userinfo","root","root");
   $result = $pdo->query("select * from userinfo where (account = ".$name[1]." and pwd = ".$pwd[1].");");
   if($result->rowCount()!=0){
     echo json_encode(arr('code'=>'200','value'=>'login success'));
   }
 }
}catch(Exception $e){
  echo json_encode(arr('code'=>'1998','value'=> 'name repeat'));   //账号已存在
}
  mysql_close($con);
