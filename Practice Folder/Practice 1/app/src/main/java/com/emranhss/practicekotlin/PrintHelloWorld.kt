package com.emranhss.practicekotlin

class PrintHelloWorld {

    //Kotlin a static keyword nai tai JVM–a jodi amra Java style static method Banate chi, Kotlin a companion object Babohar kora hoy.
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            println("Hi Kotlin Lover")
        }
    }
//Note: Kotlin a Static Keyword nai ekhane companion object dite hoy.

//=================Java Code==============
// Java main() method
//    public static void main(String[] args) {
//        // code
//        System.out.println("Hello World from Java!");
//    }

// Note: Java has static keyword, no companion object needed
//=================Java Code==============

}