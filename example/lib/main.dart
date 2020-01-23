import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_opengles/flutter_opengles.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _controller = OpenGLESController();
  final _width = 300.0;
  final _height = 300.0;

  @override
  void initState() {
    super.initState();
    initController();
  }

  @override
  void dispose() {
    _controller.destroy();
    super.dispose();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initController() async {
    await _controller.initialize(_width, _height);

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Container(
            width: _width,
            height: _height,
            child: _controller.isInitialized
                ? Texture(
                    textureId: _controller.textureId,
                  )
                : null,
          ),
        ),
      ),
    );
  }
}
