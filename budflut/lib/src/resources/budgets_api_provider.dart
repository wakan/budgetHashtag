import 'package:cloud_firestore/cloud_firestore.dart';
import '../models/budget_model.dart';

class BudgetsApiProvider {
  Future<List<BudgetModel>> fetchBudgetList() async {
    final stream = Firestore.instance
        .collection('budget')
        .where('portefeuille', isEqualTo: 'wakan-portefeuille1')
        .snapshots();
    final results = new List<BudgetModel>();
    await for (var value in stream) {
      results.addAll(budgetQuerySnapshotToListBudgetModel(value));
    }
    return results;
  }

  List<BudgetModel> budgetQuerySnapshotToListBudgetModel(QuerySnapshot snap) {
    return snap.documents.map((d) => fromDocumentToBudgetModel(d)).toList();
  }

  BudgetModel fromDocumentToBudgetModel(DocumentSnapshot d) {
    assert(null != d.data['portefeuille']);
    assert(null != d.data['libelle']);
    assert(null != d.data['color']);
    final res = new BudgetModel(
        d.data['portefeuille'],
        d.data['libelle'],
        d.data['color'],
        d.data.containsKey('previsionnel')
            ? double.parse(d.data['previsionnel'])
            : 0.0);
    return res;
  }
}
