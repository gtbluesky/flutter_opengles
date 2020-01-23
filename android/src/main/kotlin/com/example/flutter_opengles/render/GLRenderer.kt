package com.example.flutter_opengles.render

import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.HandlerThread

class GLRenderer {
    private var renderThread: HandlerThread
    var renderHandler: Handler

    init {
        renderThread = HandlerThread("Java Render Thread")
            .apply {
                start()
                renderHandler = RenderHandler(looper)
            }
    }

    fun bindSurface(surfaceTexture: SurfaceTexture) {
        renderHandler.apply {
            sendMessage(obtainMessage(
                RenderHandler.MSG_SURFACE_CREATED,
                surfaceTexture
            ))
        }
    }

    fun unBindSurface() {
        renderHandler.apply {
            sendMessage(obtainMessage(
                RenderHandler.MSG_SURFACE_DESTROYED
            ))
        }
    }

    fun changePreviewSize(width: Int, height: Int) {
        renderHandler.apply {
            sendMessage(obtainMessage(
                RenderHandler.MSG_SURFACE_CHANGED,
                width, height
            ))
        }
    }

    fun drawFrame() {
        renderHandler.apply {
            sendMessage(obtainMessage(
                    RenderHandler.MSG_RENDER
            ))
        }
    }
}