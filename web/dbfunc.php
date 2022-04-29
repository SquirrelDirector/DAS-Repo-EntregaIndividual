<?php
//error_reporting(E_ALL);
//ini_set("display_errors", 1);
/**
 * Módulo de gestión de datos en la BD
 */

// USUARIO (DNI (FK CLIENTE), CLAVE)
// CLIENTE (DNI, NOMBRE, APELLIDOS, DIRECCION, FECHA_NACIMIENTO, TELEFONO)
// CUENTABANCARIA (ID_CUENTA, DNI (FK CLIENTE), DESCRIPCION)
// TRANSACCION (ID_CUENTA_ORIGEN, @NULLABLE ID_CUENTA_DESTINO, CANTIDAD, FECHA, CONCEPTO)

/**
 * Realiza una conexión a la base de datos
 * Devuelve True si no se han producido fallos. False en caso contrario.
 */
function conectar(){
    $con = mysqli_connect("", "", "", "");
    if (mysqli_connect_errno($con)) {
        //echo 'Error de conexion: ' . mysqli_connect_error();
        return false;
    }else{
        return $con;
    }
}

/**
 * Inserta un usuario nuevo en la base de datos
 * Devuelve True si la inserción ha sido correcta. False si se han producido fallos.
 */
function insertarUsuario($nombre, $apellido, $fecha_nacimiento, $ubicacion, $tel, $identificador, $clave){
    $con=conectar();
    if ($con!=false){
        //echo "Conexion correcta";
        $imagen=subirImagen($identificador);
		//echo "Imagen: $imagen";
        if($imagen!=false || $imagen==null){
            
            $query1="INSERT INTO CLIENTE (DNI, NOMBRE, APELLIDOS, DIRECCION, FECHA_NACIMIENTO, TELEFONO, IMAGEN) VALUES ('".trim($identificador)."', '".trim($nombre)."', '".trim($apellido)."', '".trim($ubicacion)."', '".trim($fecha_nacimiento)."', '".trim($tel)."', '$imagen')";
            $query2="INSERT INTO USUARIO (DNI, CLAVE) VALUES ('".trim($identificador)."', '".trim($clave)."')";
            $execQuery1=$con->query($query1);
            $execQuery2=$con->query($query2);
			//echo "Query1: $query1";
			//echo "Query2: $query2";
            cerrarConexion($con);
            if($execQuery1 && $execQuery2){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }else{
        return false;
    }
}

/**
 * Inserta una cuenta nueva en la base de datos.
 * Devuelve True si la inserción ha sido correcta. False si se han producido fallos.
 */
/*function crearCuenta($identificadorCuenta, $nombreCuenta, $idUsuario){
    $query="INSERT INTO CUENTABANCARIA (ID_CUENTA, DNI, DESCRIPCION) VALUES ('$identificadorCuenta', '$idUsuario', '$nombreCuenta')";

}*/

/**
 * Obtiene las cuentas bancarias de un usuario específico.
 * Devuelve un listado de cuentas
 */
/*function getCuentasUsuario($identificadorUsuario){
    $query="SELECT ID_CUENTA, DNI, DESCRIPCION FROM CUENTABANCARIA WHERE DNI='$identificadorUsuario'";
}*/

/**
 * Obtiene los datos de la cuenta a consultar
 * Devuelve un conjunto de datos de una cuenta
 */
/*function getDatosCuenta($identificadorCuenta){
    $query="SELECT ID_CUENTA, DNI, DESCRIPCION FROM CUENTABANCARIA WHERE ID_CUENTA='$identificadorCuenta'";
}*/

/**
 * Obtiene los datos de un usuario concreto.
 * Devuelve un conjunto de datos de un usuario
 */
function getDatosUsuario($idUsuario){
    $con=conectar();
    if($con!=false){
        $query="SELECT DNI, NOMBRE, APELLIDOS, DIRECCION, FECHA_NACIMIENTO, TELEFONO, IMAGEN FROM CLIENTE WHERE DNI='".trim($idUsuario)."'";
		$query2="SELECT CLAVE FROM USUARIO WHERE DNI='".trim($idUsuario)."'";
        $resultado=$con->query($query);
		$resultado2=$con->query($query2);
        $fila=$resultado->fetch_assoc();
		$fila2=$resultado2->fetch_assoc();
		//echo "QUERY: $query";
		//echo "QUERY2: $query2";
        $dato=array(
            "dni"=>$fila['DNI'],
            "nombre"=>$fila['NOMBRE'],
            "apellidos"=>$fila['APELLIDOS'],
            "direccion"=>$fila['DIRECCION'],
            "fecha_nacimiento"=>$fila['FECHA_NACIMIENTO'],
            "telefono"=>$fila['TELEFONO'],
            "imagen"=>$fila['IMAGEN'],
			"clave"=>$fila2['CLAVE']
        );

        cerrarConexion($con);
        return $dato;
    }else{
        return null;
    }
    

}

/**
 * Modifica los datos de una cuenta de usuario específica
 */
function modificarDatosUsuario($nombre, $apellido, $fecha_nacimiento, $ubicacion, $telefono, $identificador, $clave){
    
    $con=conectar();
    if($con!=false){
        $query="UPDATE CLIENTE SET NOMBRE='$nombre', APELLIDOS='$apellido', TELEFONO='$telefono', FECHA_NACIMIENTO='$fecha_nacimiento', DIRECCION='$ubicacion' WHERE DNI='$identificador'";
        $query2="UPDATE USUARIO SET CLAVE='$clave' WHERE DNI='$identificador'";
        $queryOk=$con->query($query);
        $query2Ok=$con->query($query2);
        cerrarConexion($con);
        return $queryOk && $query2Ok;
    }else{
        return false;
    }
    
}

/**
 * Modifica los datos de una cuenta de usuario específica
 */
function modificarClave($identificador, $claveAntigua, $claveNueva){
	if(validarInicioSesion($identificador, $claveAntigua)>0){
		$con=conectar();
		if($con!=false){
			$query2="UPDATE USUARIO SET CLAVE='".trim($claveNueva)."' WHERE DNI='".trim($identificador)."'";
			$query2Ok=$con->query($query2);
			cerrarConexion($con);
			if($query2Ok){
				return "OK";
			}else{
				return "ERR_QUERY";
			}
		}else{
			return "ERR_CON";
		}
    }else{
		return "ERR_INICIO_SESION";
	}
}


/**
 * Realiza una transacción entre 2 cuentas existentes en el sistema
 * Devuelve True si la transacción se ha realizado con éxito. False en caso contrario
 */
/*function realizarTransaccion($idCuentaOrigen, $idCuentaDestino, $cantidad, $fecha, $concepto){

}*/

/**
 * Se obtiene un listado de transacciones realizadas en una cuenta especificada.
 */
/*function obtenerTransaccionesDeCuenta($identificadorCuenta){

}*/
function validarInicioSesion($identificador, $clave){
    $con=conectar();
    if($con!=false){
		$identificador=trim($identificador);
		$clave=trim($clave);
        $query="SELECT DNI FROM USUARIO WHERE DNI='".trim($identificador)."' AND CLAVE='".trim($clave)."'";
		//echo "Q: $query";
        $resultado=$con->query($query);
		//echo "SQL: $query";
		cerrarConexion($con);
        return $resultado->num_rows;
    }else{
        return -1;
    }
    
}

/**
 * Cierra una conexión con la base de datos
 */
function cerrarConexion($con){
    $con->close();
}

/**
 * Función que controla la carga de imágenes.
 * Devuelve la ruta si se ha subido la imagen correctamente. False en caso contrario.
 */
function subirImagen($identificador){
	//echo "SUBIENDO FICHERO... ID: $identificador<br>";
    $fichero=$_FILES['imagen'];
	//echo "FICH: $fichero";
    if($fichero['name']!=""){
         //Por https://norfipc.com/inf/como-subir-fotos-imagenes-servidor-web.php
         $tamanofichero = $_FILES['imagen']['size'];
         //Por https://stackoverflow.com/questions/10368217/how-to-get-the-file-extension-in-php
            
         $nombreImg = $_FILES['imagen']['name'];
		 //echo "<br>NOMBRE IMG: $nombreImg";
         $ext = pathinfo($nombreImg, PATHINFO_EXTENSION);
         $dir_subida = 'imagenes';
		 $directorio="$dir_subida/$identificador.$ext";
         $nombreCompletoFichero="./$dir_subida/$identificador.$ext";
         //echo "<br>Nombre completo del fichero es $nombreCompletoFichero";
        //Por https://www.php.net/manual/es/features.file-upload.post-method.php
        $subida=move_uploaded_file($_FILES['imagen']['tmp_name'], $nombreCompletoFichero);
        if($subida){
			return $directorio;
        }else{
            return false;
        }
    }else{
        return null;
    }
}

?>
