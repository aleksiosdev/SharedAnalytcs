package com.kurt.jokes.mobile
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope

class Foo {
    fun foo() : String {
        GlobalScope.launch {}
        return "Test"
    } 
}