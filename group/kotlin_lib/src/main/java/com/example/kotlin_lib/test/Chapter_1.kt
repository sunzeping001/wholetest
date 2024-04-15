package com.example.kotlin_lib.test


fun add(a: Int, b: Int): Int {
    return a + b
}

fun forFun() {
    var list = listOf("1", "2", "3")
    for (item in list) {
        println("item is: $item")
    }
    for (index in list.indices) {
        println("item index is $index, and value is ${list[index]}")
    }
}

fun whileFun() {
    var list = listOf("apple", "banana", "kiwifruit")
    var index = 0
    while (index < list.size) {
        println("fruit is: ${list[index]}")
        index++
    }
}

fun whenFun(obj: Any) {
    when (obj) {
        1 -> println("1")
        2 -> println("2")
        is Int -> println("number")
        "Hello" -> println("Greeting")
        is Long -> println("is money")
        is String -> println(obj)
        else -> println("UnKnown")
    }
}

class Test {
    fun go():String {
        return "good"
    }
}


fun main() {
    println("hello world")
    println("add fun result is ${add(1, 2)}")
    forFun()
    whileFun()
    for (i in 0 until 4) {
        if (i % 4 == 0)
            whenFun(1)
        else if (i % 4 == 1)
            whenFun(3)
        else if (i % 4 == 2)
            whenFun("fuck")
        else if (i % 4 == 3)
            whenFun(234L)
    }
    var ri: Test? = null
    var nice = ri?.go()
    println(nice?.length)

}

