package io.github.yearnlune.excel

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfoList
import kotlin.reflect.KClass

fun KClass<out Annotation>.getDeclaredClassList(): ClassInfoList =
    ClassGraph().enableClassInfo().enableAnnotationInfo().scan().getClassesWithAnnotation(this.java)