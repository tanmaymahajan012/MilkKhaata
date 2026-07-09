import 'package:flutter/material.dart';
import '../features/dashboard/startup_screen.dart';

class AppRoutes {
  static const String startup = '/';

  static Map<String, WidgetBuilder> get routes {
    return {
      startup: (context) => const StartupScreen(),
    };
  }
}
