package org.example.controller

import org.example.GREEN_BOLD
import org.example.RESET
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

val url = "jdbc:postgresql://surus.db.elephantsql.com/"
val usuario = "mkhwfgyy"
val password = "GHT70ECP3mC7HHFQ5zAH3yt6UnuJLyOj"

fun main() {
    var dbConnection: Connection? = conectarBBDD(url, usuario, password)
    var query: String
    var resultData: ResultSet?
    var connection = DriverManager.getConnection(url, usuario, password)
    var lisStament: Statement = connection.createStatement()
    var resultadoBusqueda = extraerDatos (lisStament, "SELECT * FROM laptops WHERE brand='hp'")
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
