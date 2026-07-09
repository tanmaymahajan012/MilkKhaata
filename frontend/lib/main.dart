import 'package:flutter/material.dart';
import 'core/theme/app_theme.dart';
import 'routes/app_routes.dart';

void main() {
  runApp(const MilkKhaataApp());
}

class MilkKhaataApp extends StatelessWidget {
  const MilkKhaataApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MilkKhaata',
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.system,
      initialRoute: AppRoutes.startup,
      routes: AppRoutes.routes,
      debugShowCheckedModeBanner: false,
    );
  }
}
