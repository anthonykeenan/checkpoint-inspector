package net.corda

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.KryoException
import com.esotericsoftware.kryo.io.Input
import java.io.ByteArrayInputStream
import java.io.File
import org.objenesis.strategy.StdInstantiatorStrategy
import com.esotericsoftware.kryo.Kryo.DefaultInstantiatorStrategy
import org.objenesis.strategy.SerializingInstantiatorStrategy


class thingy {

    companion object {
        const val NAME: Byte = -1
        val KryoHeaderV0_1: OpaqueBytes = OpaqueBytes("corda\u0000\u0000\u0001".toByteArray(Charsets.UTF_8))
    }


    fun deserialise() {

        val byteSequence = OpaqueBytes(File("D:\\Downloads\\large-checkpoint\\large-checkpoint.dat").readBytes())

        val headerSize = KryoHeaderV0_1.size
        val header = byteSequence.take(headerSize)
        if (header != KryoHeaderV0_1) {
            throw KryoException("Serialized bytes header does not match expected format.")
        }

        Input(byteSequence.bytes, byteSequence.offset + headerSize, byteSequence.size - headerSize).use { input ->
            val kryo = Kryo()
            kryo.isRegistrationRequired = false
            kryo.isWarnUnregisteredClasses = false
            kryo.instantiatorStrategy = DefaultInstantiatorStrategy(SerializingInstantiatorStrategy())

            //val obj = kryo.readClass(input)

            //print(obj)

            for(i in 1..1000) {
                val classId = input.readVarInt(true)

                when (classId) {
                    Kryo.NULL.toInt() -> println("class type 0")
                    NAME + 2 -> {
                        println("Class Name")

                        val nameId = input.readVarInt(true)
                        val name = input.readString()
                        println("Name Id: $nameId, Name: $name")
                    }
                    else -> {

                        println("class type $classId")
                    }

                }
//                println("Next varint ${input.readVarInt(true)}")
//                println("Next varint ${input.readVarInt(true)}")
//                println("Next varint ${input.readVarInt(true)}")
//                println("Next varint ${input.readString()}")
            }
        }
    }
}

fun main(args: Array<String>) {
    thingy().deserialise()
//    File("D:\\Downloads\\large-checkpoint\\large-checkpoint.dat").readBytes()

    //val obj = kryo.readClass(input)

    //val obj = kryo.readClassAndObject(input)
    //print(obj.javaClass.name)
}