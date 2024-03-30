package org.example.controller

import org.example.GREEN_BOLD
import org.example.RESET
import org.example.readInt
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

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
                // Lógica para modificar datos
            }
            5 -> println("Saliendo...")
            else -> println("Opción no válida")
        }
    } while (opcion != 5)
}

fun visualizarDatosOrdenadosPorCampo(query: String) {
    val resultadoOrdenado = extraerDatos(lisStament, query)
    verDatos(resultadoOrdenado)
}

fun buscarDatosPorConsulta(query: String) {
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatos(resultadoBusqueda)
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
fun verDatos(resultado: ResultSet){
    while (resultado.next()) {
        var id = resultado.getInt("id")
        var marca = resultado.getString("brand")
        var modelo = resultado.getString("model")
        var precio = resultado.getInt("price")
        var rating=resultado.getInt("rating")
        var marcaProcesador=resultado.getString("processorbrand")
        var tipoProcesador=resultado.getString("processortier")
        var numeroCores=resultado.getInt("numcores")
        var memoriaRAM=resultado.getInt("rammemory")
        var tipoAlmacenamiento=resultado.getString("storagetype")
        var capacidadAlmacenamiento=resultado.getInt("storagecapacity")
        var tipoGPU=resultado.getString("gputype")
        var pulgadasPantalla=resultado.getInt("displaysize")
        var resolucionAncho=resultado.getInt("resolutionwidth")
        var resolucionAlto=resultado.getInt("resolutionheight")
        println("$id - $marca - $modelo - $precio- $rating - $marcaProcesador - $tipoProcesador - $numeroCores" +
                " - $memoriaRAM - $tipoAlmacenamiento - $capacidadAlmacenamiento - $tipoGPU - $pulgadasPantalla " +
                "- $resolucionAncho - $resolucionAlto")
    }
}
fun visualizarDatosOrdenadosPorCampo(campo: String, orden: String) {
    val query = "SELECT * FROM laptops ORDER BY $campo $orden"
    val resultadoOrdenado = extraerDatos(lisStament, query)
    verDatos(resultadoOrdenado)
}

fun buscarPorMarca(marca: String) {
    val query = "SELECT * FROM laptops WHERE brand='$marca'"
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatos(resultadoBusqueda)
}

fun buscarPorPrecioMaximo(precioMaximo: Int) {
    val query = "SELECT * FROM laptops WHERE price <= $precioMaximo"
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatos(resultadoBusqueda)
}

fun buscarPorPuntuacionMinima(puntuacionMinima: Int) {
    val query = "SELECT * FROM laptops WHERE rating >= $puntuacionMinima"
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatos(resultadoBusqueda)
}

fun buscarPorNumeroMinimoNucleos(numMinNucleos: Int) {
    val query = "SELECT * FROM laptops WHERE numcores >= $numMinNucleos"
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatos(resultadoBusqueda)
}

fun buscarPorCantidadMinimaMemoriaRAM(minMemoriaRAM: Int) {
    val query = "SELECT * FROM laptops WHERE rammemory >= $minMemoriaRAM"
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatos(resultadoBusqueda)
}

fun buscarPorTamanoPantalla(tamanoPantalla: Int) {
    val query = "SELECT * FROM laptops WHERE displaysize = $tamanoPantalla"
    val resultadoBusqueda = extraerDatos(lisStament, query)
    verDatos(resultadoBusqueda)
}

