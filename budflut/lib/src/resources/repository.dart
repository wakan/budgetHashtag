import '../models/budget_model.dart';
import 'budgets_api_provider.dart';

class Repository {
  final budgetsApiProvider = new BudgetsApiProvider();

  Future<List<BudgetModel>> fetchAllBudgets() => budgetsApiProvider.fetchBudgetList();
}
