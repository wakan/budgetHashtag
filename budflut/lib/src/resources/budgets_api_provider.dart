import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/budget_model.dart';

class BudgetsApiProvider {
  Future<BudgetModel> fetchBudgetList() async {
    final results = Firestore.instance.collection('wakan-portefeuille1');
    
  }
}
