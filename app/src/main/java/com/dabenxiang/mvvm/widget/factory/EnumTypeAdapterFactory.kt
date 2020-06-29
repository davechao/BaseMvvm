package com.dabenxiang.mvvm.widget.factory

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class EnumTypeAdapterFactory : TypeAdapterFactory {

    override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (!type.rawType.isEnum) {
            return null
        }

        val maps = mutableMapOf<T, ValueType>()

        type.rawType.enumConstants?.filterNotNull()?.forEach {
            val tt: T = it as T

            val serializedName =
                tt.javaClass.getField(it.toString()).getAnnotation(SerializedName::class.java)

            if (serializedName != null) {
                maps[tt] =
                    ValueType(
                        serializedName.value,
                        BasicType.STRING
                    )
                return@forEach
            }

            val field = tt.javaClass.declaredFields.firstOrNull { it2 ->
                BasicType.isBasicType(
                    it2.type.name
                )
            }

            if (field != null) {
                field.isAccessible = true
                val basicType =
                    BasicType.get(
                        field.type.name
                    )
                val value: Any = when (basicType) {
                    BasicType.INT -> field.getInt(tt)
                    BasicType.STRING -> field.get(tt) as String
                    BasicType.LONG -> field.getLong(tt)
                    BasicType.DOUBLE -> field.getDouble(tt)
                    BasicType.BOOLEAN -> field.getBoolean(tt)
                }
                maps[tt] =
                    ValueType(
                        value,
                        basicType
                    )
            } else {
                maps[tt] =
                    ValueType(
                        tt.toString(),
                        BasicType.STRING
                    )
            }
        }

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T?) {
                if (value == null) {
                    out.nullValue()
                } else {
                    val valueType = maps[value]
                    when (valueType?.type) {
                        BasicType.INT -> out.value(valueType.value as Int)
                        BasicType.STRING -> out.value(valueType.value as String)
                        BasicType.LONG -> out.value(valueType.value as Long)
                        BasicType.DOUBLE -> out.value(valueType.value as Double)
                        BasicType.BOOLEAN -> out.value(valueType.value as Boolean)
                    }
                }
            }

            override fun read(reader: JsonReader): T? {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return null
                } else {
                    val source = reader.nextString()
                    var tt: T? = null
                    maps.forEach { value, type ->
                        if (type.value.toString() == source) {
                            tt = value
                            return@forEach
                        }
                    }
                    return tt
                }
            }
        }
    }

    data class ValueType(var value: Any, var type: BasicType)

    enum class BasicType(var value: String) {
        INT("int"),
        STRING("java.lang.String"),
        LONG("long"),
        DOUBLE("double"),
        BOOLEAN("boolean");

        companion object {
            fun isBasicType(name: String): Boolean {
                return values().any { it.value == name }
            }

            fun get(name: String) = values().first { it.value == name }
        }
    }
}
