package com.example.flutter_opengles.render

import android.graphics.SurfaceTexture
import android.opengl.GLES30
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.flutter_opengles.egl.EglCore
import com.example.flutter_opengles.egl.WindowSurface
import com.example.flutter_opengles.shape.BaseShape
import com.example.flutter_opengles.shape.Circle
import com.example.flutter_opengles.shape.Cube
import com.example.flutter_opengles.shape.Sphere

class RenderHandler(looper: Looper)
    : Handler(looper) {

    private var eglCore: EglCore? = null
    private var windowSurface: WindowSurface? = null
    var shape: BaseShape = Cube()

    companion object {
        // Surface创建
        const val MSG_SURFACE_CREATED = 0x01
        // Surface改变
        const val MSG_SURFACE_CHANGED = 0x02
        // Surface销毁
        const val MSG_SURFACE_DESTROYED = 0x03
        // 渲染
        const val MSG_RENDER = 0x04
    }

    override fun handleMessage(msg: Message) {
        when (msg.what) {
            MSG_SURFACE_CREATED -> {
                when (msg.obj) {
                    is SurfaceTexture -> {
                        surfaceCreated(msg.obj as SurfaceTexture)
                    }
                }
            }
            MSG_SURFACE_CHANGED -> {
                surfaceChanged(msg.arg1, msg.arg2)
            }
            MSG_SURFACE_DESTROYED -> {
                surfaceDestroyed()
            }
            MSG_RENDER -> {
                drawFrame()
            }
        }
    }

    private fun surfaceCreated(surfaceTexture: SurfaceTexture) {
        eglCore = EglCore(null, EglCore.FLAG_RECORDABLE)
        windowSurface = WindowSurface(eglCore!!, surfaceTexture)
        windowSurface?.makeCurrent()

        GLES30.glDisable(GLES30.GL_DEPTH_TEST)
        GLES30.glDisable(GLES30.GL_CULL_FACE)

        shape?.init()
    }

    private fun surfaceChanged(width: Int, height: Int) {
        windowSurface?.makeCurrent()
        shape?.change(width, height)
    }

    private fun surfaceDestroyed() {
        windowSurface?.makeCurrent()
        shape?.destroy()
        windowSurface?.release()
        windowSurface = null
        eglCore?.release()
        eglCore = null
    }

    private fun drawFrame() {
        windowSurface?.makeCurrent()
        shape?.drawFrame()
        windowSurface?.swapBuffers()
    }

}