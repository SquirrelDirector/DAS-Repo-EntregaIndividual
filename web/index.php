<?php

/*foreach ($_SERVER as $key => $value) {
    if (strpos($key, 'HTTP_') === 0) {
        $chunks = explode('_', $key);
        $header = '';
        for ($i = 1; $y = sizeof($chunks) - 1, $i < $y; $i++) {
            $header .= ucfirst(strtolower($chunks[$i])).'-';
        }
        $header .= ucfirst(strtolower($chunks[$i])).': '.$value;
        echo $header.'\n';
    }
}

foreach (getallheaders() as $nombre => $valor) {
    echo "$nombre: $valor\n";
}*/


$token=$_POST['token'];

$accion=$_POST["action"];
//la base de datos a la que hay que conectarse
//Se establece la conexión:
//$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);
//$con = mysqli_connect("db", "admin", "test", "database");
//Comprobamos conexión
/*if (mysqli_connect_errno($con)) {
	echo 'Error de conexion: ' . mysqli_connect_error();
	exit();
}*/
include("dbfunc.php");
switch($accion){
		case "seleccionar_usuario":
			//conectar();
			$id = $_POST["identificador"];
			$datosUsuario=getDatosUsuario($id);
			echo json_encode($datosUsuario);
			//cerrarConexion();
		break;
		case "validar_inicio_sesion":
			//conectar();
			$id=$_POST['identificador'];
			$clave=$_POST['clave'];
			$datos=validarInicioSesion($id, $clave);
			if($datos>0){
				$resultado=array(
					"inicio_sesion"=>"true"
				);
				echo json_encode($resultado);
			}else{
				$resultado=array(
					"inicio_sesion"=>"false"
				);
				echo json_encode($resultado);
			}
		break;
		case "insert":
			//echo "Entrando en insert";
			$nombreUsuario=$_POST['nombre'];
			$apellidos=$_POST['apellidos'];
			$fechaNacimiento=$_POST['fecha_nacimiento'];
			$direccion=$_POST['direccion'];
			$telefono=$_POST['telefono'];
			$identificador=$_POST['identificador'];
			$clave=$_POST['clave'];
			//conectar();
			//echo "Datos. Nombre: $nombreUsuario. Apellidos: $apellidos. Fecha Nacimiento: $fechaNacimiento. Dirección: $direccion. Teléfono: $telefono. Identificador: $identificador. Clave: $clave.";
			$dato=insertarUsuario($nombreUsuario, $apellidos, $fechaNacimiento, $direccion, $telefono, $identificador, $clave);
			//echo "USUARIO INSERTADO";
			$resultado=array(
				"accion_realizada"=>$dato
			);
			echo json_encode($resultado);
			//cerrarConexion();
		break;
		case "update":
			$nombreUsuario=$_POST['nombre'];
			$apellidos=$_POST['apellidos'];
			$fechaNacimiento=$_POST['fecha_nacimiento'];
			$direccion=$_POST['direccion'];
			$telefono=$_POST['telefono'];
			$identificador=$_POST['identificador'];
			$clave=$_POST['clave'];
			//conectar();
			$dato=modificarDatosUsuario($nombreUsuario, $apellidos, $fechaNacimiento, $direccion, $telefono, $identificador, $clave);
			$resultado=array(
				"accion_realizada"=>$dato
			);
			echo json_encode($resultado);
			//cerrarConexion();
		break;
		
		case "modificar_clave":
			$identificador=$_POST['identificador'];
			$clave_nueva=$_POST['clave'];
			$clave_antigua=$_POST['clave_antigua'];
			$dato=modificarClave($identificador, $clave_antigua, $clave_nueva);
			$resultado=array(
				"accion_realizada"=>$dato
			);
			echo json_encode($resultado);

}
?>
