<?php
include "Conexion.php";
if (!empty($_POST["id_cuestionario"]) || !empty($_GET["id_cuestionario"])) {
    // Obtener el id del cuestionario de $_POST o $_GET según sea necesario
    $id_cuestionario = (!empty($_POST["id_cuestionario"])) ? $_POST["id_cuestionario"] : $_GET["id_cuestionario"];

    try {
        $consulta = $base_de_datos->prepare("SELECT * FROM respuestas WHERE id_cuestionario = :id ");
        $consulta->bindParam(":id", $id_cuestionario);

        $consulta->execute();
        $datos =  $consulta->fetchAll(PDO::FETCH_ASSOC);
        $datos = mb_convert_encoding($datos, "UTF-8", "iso-8859-1");
        // Preparar la consulta para obtener las respuestas asociadas al cuestionario dado
        $consulta_respuestas = $base_de_datos->prepare("SELECT id_pregunta FROM respuestas WHERE id_cuestionario = :id_cuestionario");
        $consulta_respuestas->bindParam(":id_cuestionario", $id_cuestionario, PDO::PARAM_INT);
        $consulta_respuestas->execute();

        // Obtener los ID de pregunta de las respuestas
        $id_preguntas = $consulta_respuestas->fetchAll(PDO::FETCH_COLUMN);

        // Preparar la consulta para obtener las preguntas correspondientes a los ID
        $consulta_preguntas = $base_de_datos->prepare(" SELECT preguntas.*, respuestas.estado, respuestas.respuesta, opciones.id AS respuesta FROM preguntas  INNER JOIN respuestas ON preguntas.id = respuestas.id_pregunta INNER JOIN opciones ON respuestas.respuesta = opciones.descripcion WHERE preguntas.id = :id_pregunta");


        $respuestas_pregunta = [];
        $opciones_objetos=[];
        $datos_objetos = []; // Arreglo para almacenar objetos

        // Iterar sobre los IDs de pregunta y obtener las preguntas correspondientes
        foreach ($id_preguntas as $id_pregunta) {
            // Vincular el ID de la pregunta al marcador de posición
            $consulta_preguntas->bindParam(":id_pregunta", $id_pregunta, PDO::PARAM_INT);
            $consulta_preguntas->execute();

            // Obtener la pregunta
            $pregunta = $consulta_preguntas->fetch(PDO::FETCH_ASSOC);

            if ($pregunta) {
                // Preparar la consulta SQL para obtener las opciones asociadas a la pregunta dada
                $consulta_opciones = $base_de_datos->prepare("SELECT * FROM opciones WHERE id_pregunta = :id_pregunta2");
                $consulta_opciones->bindParam(":id_pregunta2", $id_pregunta, PDO::PARAM_INT);
                $consulta_opciones->execute();

                // Obtener los resultados de la consulta
                $opciones_resultados = $consulta_opciones->fetchAll(PDO::FETCH_ASSOC);

                // Crear un arreglo para almacenar los objetos de opciones
                $opciones_objetos = [];

                // Iterar sobre los resultados y convertir cada fila en un objeto
                foreach ($opciones_resultados as $opcion_resultado) {
                    $opcion_objeto = (object) $opcion_resultado; // Convertir la fila en un objeto
                    $opciones_objetos[] = $opcion_objeto; // Agregar el objeto al arreglo de objetos de opciones
                }
                // Agregar la pregunta al arreglo de respuestas_pregunta
                $respuestas_pregunta = [
                    "pregunta" => $pregunta,
                    "opciones" => $opciones_objetos
                ];


                // Convertir $respuestas_pregunta en un objeto y agregarlo al arreglo de objetos
                $objeto_respuesta_pregunta = (object) $respuestas_pregunta; // Convertir a objeto
                $datos_objetos[] = $objeto_respuesta_pregunta; // Agregar al arreglo de objetos
            }
        }

        // Asignar los datos convertidos a $datos
        $datos = $datos_objetos;
        // Verificar si se encontraron preguntas
        if (!empty($respuestas_pregunta)) {
            echo json_encode([
                "status" => true,
                "message" => "DATA##OK",
                "respuestas" => $datos
            ]);
        } else {
            echo json_encode([
                "status" => false,
                "message" => "No se encontraron preguntas para el cuestionario dado"
            ]);
        }
    } catch (PDOException $e) {
        // Manejar cualquier excepción de PDO
        echo json_encode([
            "status" => false,
            "message" => "Error en la consulta: " . $e->getMessage()
        ]);
    }
}else {
    $respuesta = [
                    "status" => false,
                    "message" => "ERROR##DATOS##POST"

                ];

    echo json_encode($respuesta);
}
?>