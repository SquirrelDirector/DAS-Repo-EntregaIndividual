<?php
$cuenta_origen=$_POST["cuenta_origen"];
$cuenta_destino=$_POST["cuenta_destino"];
$identificador=$_POST["identificador"];
$cantidad=$_POST["cantidad"];
$concepto=$_POST["concepto"];

$cabecera= array(
'Authorization: key=',
'Content-Type: application/json'
);

$msg = array (
'data' => array (
		"cuenta_origen"=>$cuenta_origen,
		"cuenta_destino"=>$cuenta_destino,
		"identificador"=>$identificador,
		"cantidad"=>$cantidad,
		"concepto"=>$concepto
	),
'to'=>"/topics/$identificador"
);
$ch = curl_init(); #inicializar el handler de curl
#indicar el destino de la petición, el servicio FCM de google
curl_setopt( $ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
#indicar que la conexión es de tipo POST
curl_setopt( $ch, CURLOPT_POST, true );
#agregar las cabeceras
curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera);
#Indicar que se desea recibir la respuesta a la conexión en forma de string
curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
#agregar los datos de la petición en formato JSON
curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode($msg) );
#ejecutar la llamada
$resultado= curl_exec( $ch );
#cerrar el handler de curl
curl_close( $ch );

echo $resultado;

?>
