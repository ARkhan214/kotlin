package com.emranhss.practicekotlin

fun main() {

    Int
    String
    Boolean
    Float
    Double
    Long
    Char
    Byte
    Short

    var a : Int =20
    println("Hi Kotlin Lover $a")
    println("Hi Kotlin Lover " + a)


    println(String.format("Hi Kotlin Lover %d", a))
    println(a + 10.6)

    val age: Int = 25
    println(age)

    val name: String = "Rahim"
    println(name)

    val isLoggedIn: Boolean = true
    println(isLoggedIn)

    val height: Float = 5.9f
    println(height)

    val pi: Double = 3.14159
    println(pi)

    val population: Long = 8000000000L
    println(population)

    val grade: Char = 'A'
    println(grade)

    val number: Byte = 100
    println(number)

    val score: Short = 32000
    println(score)

    println("Age is Int Type : $age")
    println("Name is String Type: $name")
    println("Married is Boolean Type: $isLoggedIn")
    println("Height is Float Type: $height")
    println("Pi is Double Type: $pi")
    println("Population is Long Type : $population")
    println("Grade is Char Type: $grade")
    println("Byte is Byte type : $number")
    println("Short is Short type: $score")


}