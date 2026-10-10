package com.shashlikmap.webgpukt.webgpu

import com.shashlikmap.webgpukt.webgpu.utils.Vec3
import com.shashlikmap.webgpukt.webgpu.utils.Vec4
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class Vertex(val position: Vec3, val normal: Vec3, val color: Vec4)

fun List<Vertex>.toByteBuffer(): ByteBuffer {
    val byteBuffer = ByteBuffer.allocateDirect(size * (3 + 3 + 4) * Float.SIZE_BYTES)
    byteBuffer.order(ByteOrder.nativeOrder())
    val floatBuffer = byteBuffer.asFloatBuffer()
    for (vertex in this) {
        floatBuffer.put(vertex.position.a)
        floatBuffer.put(vertex.position.b)
        floatBuffer.put(vertex.position.c)
        floatBuffer.put(vertex.normal.a)
        floatBuffer.put(vertex.normal.b)
        floatBuffer.put(vertex.normal.c)
        floatBuffer.put(vertex.color.a)
        floatBuffer.put(vertex.color.b)
        floatBuffer.put(vertex.color.c)
        floatBuffer.put(vertex.color.w)
    }
    byteBuffer.rewind()
    return byteBuffer
}
