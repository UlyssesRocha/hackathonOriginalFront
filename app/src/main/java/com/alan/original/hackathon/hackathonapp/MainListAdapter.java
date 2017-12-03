package com.alan.original.hackathon.hackathonapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alanfortlink on 12/3/17.
 */

public class MainListAdapter extends BaseAdapter {

    Context context;
    List<Transaction> transactions;

    public MainListAdapter(Context context, List<Transaction> transactions){
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int i) {
        return transactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rootView = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        TextView textView;
        TextView sign;

        Transaction transaction = (Transaction) getItem(i);

        if(transaction.valor > 0){
            rootView = inflater.inflate(R.layout.income_layout, null);
            textView = rootView.findViewById(R.id.income_value);
            sign = rootView.findViewById(R.id.income_plus);
            sign.setText("+ " + transaction.nome);
        }else{
            rootView = inflater.inflate(R.layout.out_come, null);
            textView = rootView.findViewById(R.id.outcome_value);
            sign = rootView.findViewById(R.id.outcome_plus);
            sign.setText("- " + transaction.nome);
        }

        textView.setText("" + transaction.valor);

        return rootView;
    }
}
