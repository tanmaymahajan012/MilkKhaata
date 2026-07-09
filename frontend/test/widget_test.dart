import 'package:flutter_test/flutter_test.dart';
import 'package:milk_khaata/main.dart';

void main() {
  testWidgets('App startup smoke test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const MilkKhaataApp());

    // Verify that the title text is present.
    expect(find.text('MilkKhaata'), findsOneWidget);
    
    // Verify the foundational message is present
    expect(find.text('MilkKhaata Foundation Ready!'), findsOneWidget);
  });
}
