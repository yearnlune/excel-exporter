package io.github.yearnlune.excel.domain

import java.util.*

data class Student(

    val id: String = UUID.randomUUID().toString(),

    val name: String,

    val email: String,

    val birth: Date
)