package com.shashlikmap.webgpukt.webgpu

import androidx.webgpu.GPUDevice
import androidx.webgpu.GPUExtent3D
import androidx.webgpu.GPUMultisampleState
import androidx.webgpu.GPUSurfaceConfiguration
import androidx.webgpu.GPUTexture
import androidx.webgpu.GPUTextureDescriptor
import androidx.webgpu.GPUTextureView
import androidx.webgpu.TextureUsage

class MultisampleView(
    device: GPUDevice,
    gpuSurfaceConfiguration: GPUSurfaceConfiguration,
) : AutoCloseable {
    companion object {
        private const val SAMPLE_COUNT = 4
    }

    private val texture: GPUTexture
    val textureView: GPUTextureView

    val multisampleState: GPUMultisampleState = GPUMultisampleState(count = SAMPLE_COUNT)

    init {
        val extent = GPUExtent3D(
            width = gpuSurfaceConfiguration.width,
            height = gpuSurfaceConfiguration.height,
        )

        val textureDescriptor = GPUTextureDescriptor.Builder(TextureUsage.RenderAttachment, extent)
            .setFormat(gpuSurfaceConfiguration.format)
            .setSampleCount(SAMPLE_COUNT)
            .build()
        texture = device.createTexture(textureDescriptor)
        textureView = texture.createView()
    }

    override fun close() {
        textureView.close()
        texture.close()
    }
}
