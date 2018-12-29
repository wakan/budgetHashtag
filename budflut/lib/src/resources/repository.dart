import '../models/budget_model.dart';
import 'budgets_api_provider.dart';

class Repository {
  final budgetsApiProvider = new BudgetsApiProvider();

  Future<BudgetModel> fetchAllBudgets() => budgetsApiProvider.fetchBudgetList();
}
