package com.shashlikmap.webgpukt.webgpu

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Connects [SurfaceView] with [WebGpuAPI] and drives the render loop.
 */
class WebGPUSurfaceView @JvmOverloads constructor(
    context: Context,
    private val api: WebGpuAPI,
    attrs: AttributeSet? = null,
) : SurfaceView(context, attrs) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var renderJob: Job? = null

    init {
        setWillNotDraw(true)
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val width = holder.surfaceFrame.width()
                val height = holder.surfaceFrame.height()
                renderJob = coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        api.setSurface(context, holder.surface, width, height)
                    }
                    while (isActive) {
                        awaitFrame()
                        withContext(Dispatchers.IO) {
                            api.update()
                            api.render()
                        }
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int,
            ) {
                // no-op
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                renderJob?.cancel()
                coroutineScope.launch(Dispatchers.IO) {
                    api.reset()
                }
            }
        })
    }
}
