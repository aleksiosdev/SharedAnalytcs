package com.kurt.jokes.mobile
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope

public class Event {
    val id: String
    val name: String

    public constructor(id: String, name: String) {
        this.id = id
        this.name = name
    }
}

public enum class EventType {
    LOGIN;

    public fun event(): Event {
        return when (this) {
            LOGIN -> Event("1", "LOGIN")
        }
    }
}
