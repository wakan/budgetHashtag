import 'package:rxdart/rxdart.dart';
import '../resources/repository.dart';
import '../models/budget_model.dart';

class BudgetLstBloc {
  final _repository = Repository();
  final _budgetFetcher = PublishSubject<BudgetModel>();

  Observable<BudgetModel> get allBudgets => _budgetFetcher.stream;

  fetchAllBudgets() async {
    for (var value in await _repository.fetchAllBudgets())
      _budgetFetcher.sink.add(value);
  }

  dispose() {
    _budgetFetcher.close();
  }
}

final bloc = BudgetLstBloc();
