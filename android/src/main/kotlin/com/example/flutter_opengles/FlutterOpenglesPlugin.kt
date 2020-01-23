package com.example.flutter_opengles

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.example.flutter_opengles.render.GLRenderer
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.view.TextureRegistry

/** FlutterOpenglesPlugin */
class FlutterOpenglesPlugin : FlutterPlugin, MethodCallHandler {

    private lateinit var textureRegistry: TextureRegistry
    private lateinit var glRenderer : GLRenderer
    private lateinit var methodChannel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        textureRegistry = flutterPluginBinding.textureRegistry
        onAttachedToEngine(flutterPluginBinding.applicationContext, flutterPluginBinding.binaryMessenger)
    }

    fun onAttachedToEngine(applicationContext: Context, messenger: BinaryMessenger) {
        context = applicationContext
        methodChannel = MethodChannel(messenger, "flutter_opengles")
        methodChannel.setMethodCallHandler(this)
    }
    
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            FlutterOpenglesPlugin().also {
                it.textureRegistry = registrar.textures()
                it.onAttachedToEngine(registrar.context(), registrar.messenger())
            }
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "create" -> {
                val arguments = call.arguments as Map<String, Number>
                Log.e("FlutterOpenglesPlugin", "${call.method} ${call.arguments}")
                val entry = textureRegistry.createSurfaceTexture()
                val surfaceTexture = entry.surfaceTexture()
                val width = dp2px(context, arguments["width"]?.toFloat() ?: 0f)
                val height = dp2px(context, arguments["height"]?.toFloat() ?: 0f)
                surfaceTexture.setDefaultBufferSize(width, height)
                glRenderer = GLRenderer()
                glRenderer.bindSurface(surfaceTexture)
                glRenderer.changePreviewSize(width, height)
                glRenderer.drawFrame()
                result.success(entry.id())
            }
            "destroy" -> {
                glRenderer.unBindSurface()
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
    }

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}
