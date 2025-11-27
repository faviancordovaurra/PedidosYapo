package com.pedidosyapo.network

data class Producto(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val descripcion: String?,
    val imagenUrl: String?
)
