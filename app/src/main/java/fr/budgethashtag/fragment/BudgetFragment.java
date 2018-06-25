package fr.budgethashtag.fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.budgethashtag.R;
import fr.budgethashtag.activity.AddBudgetActivity;
import fr.budgethashtag.interfacecallbackasynctask.LoadBudgetsByPortefeuilleIdCallback;

public class BudgetFragment extends Fragment
    implements LoadBudgetsByPortefeuilleIdCallback {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        view.findViewById(R.id.btn_add_budget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBudgetActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onLoadBudgetsByPortefeuilleIdCallback(List<ContentValues> contentValuesList) {

    }
}
