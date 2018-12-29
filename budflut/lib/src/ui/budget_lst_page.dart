import 'package:flutter/material.dart';
import '../models/budget_model.dart';
import '../blocs/budget_lst_bloc.dart';

class BudgetListPage extends StatefulWidget {
  @override
  _BudgetListPageState createState() {
    return _BudgetListPageState();
  }
}

class _BudgetListPageState extends State<BudgetListPage> {
  @override
  void initState() {
    super.initState();
    bloc.fetchAllBudgets();
  }

  @override
  void dispose() {
    bloc.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Budgets')),
      body: _buildBody(context),
      floatingActionButton: FloatingActionButton(
        onPressed: () => print('hello'),
        tooltip: 'Add budget',
        child: Icon(Icons.add),
      ),
    );
  }

  Widget _buildBody(BuildContext context) {
    return StreamBuilder(
      stream: bloc.allBudgets, // Firestore.instance.collection('baby').snapshots(),
      builder: (context, snapshot) {
        switch(snapshot.connectionState) {
          case ConnectionState.none:
          case ConnectionState.waiting:
            return LinearProgressIndicator();
          default:
            if(snapshot.hasError) {
              return new Text('Error : ${snapshot.error}');
            } else {
              return ListView.builder(
                itemBuilder: (context, index){
                  _buildListItem(context, snapshot.data);
                } 
              );
            }
        }
      },
    );
  }

  Widget _buildListItem(BuildContext context, BudgetModel data) {
    return Padding(
      key: ValueKey(data.libelle),
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Container(
        decoration: BoxDecoration(
          border: Border.all(color: Colors.grey),
          borderRadius: BorderRadius.circular(5.0),
        ),
        child: ListTile(
          title: Text(data.libelle),
          trailing: Text(data.color),
          onTap: () => print("taped"),
        ),
      ),
    );
  }
}
