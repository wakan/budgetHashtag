class BudgetModel {
  String _portefeuille;
  String _libelle;
  String _color;
  double _previsionnel;

  BudgetModel(String portefeuille, String libelle, String color, double previ) {
    _portefeuille = portefeuille;
    _libelle = libelle;
    _color = color;
    _previsionnel = previ;
  }

  String get porteuille => _portefeuille;
  String get libelle => _libelle;
  String get color => _color;
  double get previsionnel => _previsionnel;

  @override
  String toString() {
    return porteuille + libelle + color + previsionnel.toString();
  }
}
