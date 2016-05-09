<?php
$input = file_get_contents("php://input");
$str=explode("&",$input);
$type=explode("=",$str[0]);
$pwd =explode("=",$str[1]);
$name=explode("=",$str[2]);

try{
  $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
  $pdo =new PDO('mysql:host=localhost;dbname=Userinfo','root','root',$pdo_options);
}
catch(Exception $e){
    echo("databaseerror");
}

try{
  if($type[1]=="signup"){
    if($pwd[1]!=$name[1]){

      if($pdo->exec("INSERT INTO userinfo (account,pwd) VALUES(".$name[1].",".$pwd[1].");")){
           echo json_encode(array('code'=>'200','value'=>'signup success'));
      }
      else {
          echo json_encode(array('code'=>'1998','value'=> 'name repeat'));
      }
    }
     else{
        echo json_encode(array('code'=>'1996','value'=> 'Not allow the same name and password'));
    }
  }
  else{
   $sql = "select * from userinfo where (account='".$name[1]."' and pwd='".$pwd[1]."');";
   $result = $pdo->query($sql);
   $rows = $result->fetchAll();
   $rowCount = count($rows);

   if($rowCount != 0){
     echo json_encode(array('code'=>'200','value'=>'login success'));
   }
   else {
       echo json_encode(array('code'=>'400','value'=>'pwd not match'));
   }
 }
}catch(Exception $e){
  print "error".$e->getMessage()."</br>";
  die();
}
