import 'package:flutter/material.dart';
import 'ui/budget_lst_page.dart';

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'budget hashtag',
      theme: ThemeData.dark(),
      home: BudgetListPage(),
    );
  }
}
