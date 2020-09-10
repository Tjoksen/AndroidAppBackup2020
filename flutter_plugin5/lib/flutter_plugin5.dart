import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPlugin5 {
  static const MethodChannel _channel =
      const MethodChannel('flutter_plugin5');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
