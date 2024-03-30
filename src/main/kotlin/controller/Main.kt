package org.example.controller

import org.example.GREEN_BOLD
import org.example.RESET
import org.example.readInt
import org.example.readSentence
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io.File


val url = "jdbc:postgresql://surus.db.elephantsql.com/"
val usuario = "mkhwfgyy"
val password = "GHT70ECP3mC7HHFQ5zAH3yt6UnuJLyOj"
var dbConnection: Connection? = conectarBBDD(url, usuario, password)
var query: String = ""
var resultData: ResultSet? = null
var connection = DriverManager.getConnection(url, usuario, password)
var lisStament: Statement = connection.createStatement()

fun main(){
    menu()
}
fun menu() {
    var dbConnection: Connection? = conectarBBDD(url, usuario, password)
    var query: String
    var resultData: ResultSet?
    var connection = DriverManager.getConnection(url, usuario, password)
    var lisStament: Statement = connection.createStatement()

    var opcion: Int

    do {
        println("Escoja una de las siguientes opciones: \n" +
                "1. Visualizar datos\n" +
                "2. Buscar datos\n" +
                "3. Escribir consulta personalizada\n" +
                "4. Modificar datos\n" +
                "5. Salir")

        opcion = readInt("Ingrese su opción:", "Debe ser un número")

        when (opcion) {
            1 -> {
                println("Ahora puede ver los datos según estos criterios: \n" +
                        "1. Visualizar por marca\n" +
                        "2. Visualizar por precio\n" +
                        "3. Visualizar por puntuación")

                var subOpcion = readInt("Seleccione una opción:", "Debe ser un número")

                query = when (subOpcion) {
                    1 -> "SELECT * FROM laptops ORDER BY brand"
                    2 -> "SELECT * FROM laptops ORDER BY price"
                    3 -> "SELECT * FROM laptops ORDER BY rating"
                    else -> {
                        println("Opción no válida")
                        continue
                    }
                }

                visualizarDatosOrdenadosPorCampo(query)
            }
            2 -> {
                println("Ahora puede filtrar por los siguientes campos: \n" +
                        "1. Por marca\n" +
                        "2. Por precio máximo\n" +
                        "3. Por puntuación mínima\n" +
                        "4. Por número mínimo de cores\n" +
                        "5. Por cantidad mínima de memoria\n" +
                        "6. Por pulgadas de pantalla")

                var subOpcion = readInt("Seleccione una opción:", "Debe ser un número")

                query = when (subOpcion) {
                    1 -> {
                        println("Ingrese la marca:")
                        val marca = readLine()
                        "SELECT * FROM laptops WHERE brand='$marca'"
                    }
                    2 -> {
                        println("Ingrese el precio máximo:")
                        val precioMaximo = readInt("Precio máximo:", "Debe ser un número")
                        "SELECT * FROM laptops WHERE price <= $precioMaximo"
                    }
                    3 -> {
                        println("Ingrese la puntuación mínima:")
                        val puntuacionMinima = readInt("Puntuación mínima:", "Debe ser un número")
                        "SELECT * FROM laptops WHERE rating >= $puntuacionMinima"
                    }
                    4 -> {
                        println("Ingrese el número mínimo de cores:")
                        val numMinCores = readInt("Número mínimo de cores:", "Debe ser un número")
                        "SELECT * FROM laptops WHERE numcores >= $numMinCores"
                    }
                    5 -> {
                        println("Ingrese la cantidad mínima de memoria RAM:")
                        val minRAM = readInt("Cantidad mínima de RAM:", "Debe ser un número")
                        "SELECT * FROM laptops WHERE rammemory >= $minRAM"
                    }
                    6 -> {
                        println("Ingrese el tamaño de pantalla:")
                        val tamanoPantalla = readInt("Tamaño de pantalla:", "Debe ser un número")
                        "SELECT * FROM laptops WHERE displaysize = $tamanoPantalla"
                    }
                    else -> {
                        println("Opción no válida")
                        continue
                    }
                }

                buscarDatosPorConsulta(query)
            }
            3 -> {
                println("Ingrese su consulta personalizada:")
                val consulta = readLine()?.trim() ?: ""
                buscarDatosPorConsulta(consulta)
            }
            4 -> {
                println("Seleccione la operación de modificación:")
                println("1. Insertar de nuevo registro")
                println("2. Modificar todo el registro existente")
                println("3. Eliminar un registro existente")
                val subOpciom = readInt("Ingrese su opción:", "Debe ser un número")

                when (subOpciom) {
                    1 -> insertarNuevoRegistro()
                    2 -> modificarTodoRegistro()
                    3 -> eliminarRegistro()
                    else -> println("Opción no válida")
                }
            }

            5 -> println("Saliendo...")
            else -> println("Opción no válida")
        }
    } while (opcion != 5)
}

fun visualizarDatosOrdenadosPorCampo(query: String) {
    val resultadoOrdenado = extraerDatos(lisStament, query)
    verDatosPasarArchivo(resultadoOrdenado)
}

fun buscarDatosPorConsulta(query: String) {
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatosPasarArchivo(resultadoBusqueda)
}

fun conectarBBDD(url: String, usuario: String, password: String): Connection? {
    try {
        Class.forName("org.postgresql.Driver")
    } catch (e: Exception) {
        println(e)
    }

    var db: Connection? = null

    try {
        db = DriverManager.getConnection(url, usuario, password)
        println(GREEN_BOLD + "Connection successful" + RESET)
    } catch (e: Exception) {
        println(e)
    }
    return db ?: throw IllegalStateException("Failed to establish database connection")
}
fun extraerDatos(listStatement: Statement, query: String):ResultSet{
    var resultado:ResultSet=listStatement.executeQuery(query)
    return resultado
}

fun verDatosPasarArchivo(resultado: ResultSet) {
    val informeFile = File("src/main/kotlin/files/Informe.txt")

    informeFile.bufferedWriter().use { out ->
        while (resultado.next()) {
            val id = resultado.getInt("id")
            val marca = resultado.getString("brand")
            val modelo = resultado.getString("model")
            val precio = resultado.getInt("price")
            val rating = resultado.getInt("rating")
            val marcaProcesador = resultado.getString("processorbrand")
            val tipoProcesador = resultado.getString("processortier")
            val numeroCores = resultado.getInt("numcores")
            val memoriaRAM = resultado.getInt("rammemory")
            val tipoAlmacenamiento = resultado.getString("storagetype")
            val capacidadAlmacenamiento = resultado.getInt("storagecapacity")
            val tipoGPU = resultado.getString("gputype")
            val pulgadasPantalla = resultado.getInt("displaysize")
            val resolucionAncho = resultado.getInt("resolutionwidth")
            val resolucionAlto = resultado.getInt("resolutionheight")

            val linea = "$id - $marca - $modelo - $precio- $rating - $marcaProcesador - $tipoProcesador - $numeroCores" +
                    " - $memoriaRAM - $tipoAlmacenamiento - $capacidadAlmacenamiento - $tipoGPU - $pulgadasPantalla " +
                    "- $resolucionAncho - $resolucionAlto"
            out.write(linea)
            out.newLine()

            println(linea)
        }
    }
    println("Informe generado correctamente en la ruta $informeFile")
}
fun insertarNuevoRegistro() {
    println("Ingrese los valores para el nuevo registro:")
    val marca = readSentence("Ingrese la marca","Ha de ser letras")
    val modelo = readSentence("Ingrese el modelo","Ha de ser letras")
    val precio = readInt("Precio:", "Debe ser un número")
    val rating = readInt("Puntuación:", "Debe ser un número")
    val marcaProcesador = readSentence("INgrese marca del procesador", "Han de ser letras")
    val tipoProcesador = readSentence("Ingrese el tipo de procesador","Han de ser letras")
    val numeroCores = readInt("Número de cores:", "Debe ser un número")
    val memoriaRAM = readInt("Cantidad de RAM:", "Debe ser un número")
    val tipoAlmacenamiento = readSentence("Ingrese el tipo de almacenamiento","Han de ser letras")
    val capacidadAlmacenamiento = readInt("Capacidad de almacenamiento:", "Debe ser un número")
    val tipoGPU = readSentence("Ingrese el tipo de GPU","Han de ser letras")
    val pulgadasPantalla = readInt("Tamaño de pantalla:", "Debe ser un número")
    val resolucionAncho = readInt("Resolución de pantalla (ancho):", "Debe ser un número")
    val resolucionAlto = readInt("Resolución de pantalla (alto):", "Debe ser un número")

    val query = """
        INSERT INTO laptops (brand, model, price, rating, processorbrand, processortier, 
        numcores, rammemory, storagetype, storagecapacity, gputype, displaysize, 
        resolutionwidth, resolutionheight) 
        VALUES ('$marca', '$modelo', $precio, $rating, '$marcaProcesador', '$tipoProcesador', 
        $numeroCores, $memoriaRAM, '$tipoAlmacenamiento', $capacidadAlmacenamiento, '$tipoGPU', 
        $pulgadasPantalla, $resolucionAncho, $resolucionAlto)
    """.trimIndent()

    ejecutarQuery(query)
}


fun modificarTodoRegistro() {
    val id = readInt("Ingrese el ID del registro a modificar:", "Debe ser un número")
    println("Ingrese los nuevos valores para el registro:")
    val marca = readSentence("Ingrese la marca","Ha de ser letras")
    val modelo = readSentence("Ingrese el modelo","Ha de ser letras")
    val precio = readInt("Precio:", "Debe ser un número")
    val rating = readInt("Puntuación:", "Debe ser un número")
    val marcaProcesador = readSentence("INgrese marca del procesador", "Han de ser letras")
    val tipoProcesador = readSentence("Ingrese el tipo de procesador","Han de ser letras")
    val numeroCores = readInt("Número de cores:", "Debe ser un número")
    val memoriaRAM = readInt("Cantidad de RAM:", "Debe ser un número")
    val tipoAlmacenamiento = readSentence("Ingrese el tipo de almacenamiento","Han de ser letras")
    val capacidadAlmacenamiento = readInt("Capacidad de almacenamiento:", "Debe ser un número")
    val tipoGPU = readSentence("Ingrese el tipo de GPU","Han de ser letras")
    val pulgadasPantalla = readInt("Tamaño de pantalla:", "Debe ser un número")
    val resolucionAncho = readInt("Resolución de pantalla (ancho):", "Debe ser un número")
    val resolucionAlto = readInt("Resolución de pantalla (alto):", "Debe ser un número")

    val query = """
        UPDATE laptops SET 
        brand='$marca', 
        model='$modelo', 
        price=$precio, 
        rating=$rating, 
        processorbrand='$marcaProcesador', 
        processortier='$tipoProcesador', 
        numcores=$numeroCores, 
        rammemory=$memoriaRAM, 
        storagetype='$tipoAlmacenamiento', 
        storagecapacity=$capacidadAlmacenamiento, 
        gputype='$tipoGPU', 
        displaysize=$pulgadasPantalla, 
        resolutionwidth=$resolucionAncho, 
        resolutionheight=$resolucionAlto 
        WHERE id=$id
    """.trimIndent()

    ejecutarQuery(query)
}

fun eliminarRegistro() {
    val id = readInt("Ingrese el ID del registro a eliminar:", "Debe ser un número")

    val confirmacion = readSentence("¿Está seguro que desea eliminar este registro? (Si/No): ","Debe ser una palabra")
    if (confirmacion.equals("Si", ignoreCase = true)) {
        val query = "DELETE FROM laptops WHERE id=$id"
        ejecutarQuery(query)
    } else {
        println("Operación de eliminación cancelada.")
    }
}


fun ejecutarQuery(query: String) {
    try {
        val resultado = lisStament.executeUpdate(query)
        if (resultado > 0) {
            println("Registro actualizado correctamente.")
        } else {
            println("No se encontró ningún registro para actualizar.")
        }
    } catch (e: Exception) {
        println("Error al ejecutar la consulta: ${e.message}")
    }
}


