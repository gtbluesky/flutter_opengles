import 'dart:async';

import 'package:flutter/services.dart';

class OpenGLESController {
  int textureId;

  static const MethodChannel _channel = const MethodChannel('flutter_opengles');

  Future<int> initialize(double width, double height) async {
    textureId = await _channel.invokeMethod('create', {
      'width': width,
      'height': height,
    });
    return textureId;
  }

  Future<Null> destroy() => _channel.invokeMethod('destroy');

  bool get isInitialized => textureId != null;
}
