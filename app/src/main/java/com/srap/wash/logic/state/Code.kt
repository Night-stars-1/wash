package com.srap.wash.logic.state

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

enum class Code(val value: Int) {
    SUCCESS(0),
    ERROR(1),
    NOT_LOGIN(2),
    CODE_ERROR(1001);

    companion object {
        fun fromValue(value: Int): Code {
            return entries.first { it.value == value }
        }
    }
}

class CodeAdapter : TypeAdapter<Code>() {
    override fun write(out: JsonWriter, value: Code) {
        out.value(value.value)
    }

    override fun read(reader: JsonReader): Code {
        return Code.fromValue(reader.nextInt())
    }
}
