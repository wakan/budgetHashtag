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
import fr.budgethashtag.activity.AddTransactionActivity;
import fr.budgethashtag.interfacecallbackasynctask.LoadTransactionsByPortefeuilleIdCallback;

public class TransactionFragment extends Fragment
    implements LoadTransactionsByPortefeuilleIdCallback{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        view.findViewById(R.id.btn_add_transaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onLoadTransactionsByPortefeuilleId(List<ContentValues> contentValuesList) {

    }
}
