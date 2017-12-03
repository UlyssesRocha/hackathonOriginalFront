package com.alan.original.hackathon.hackathonapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean isFirstTime = true;

    List<Transaction> transactions = new ArrayList<>();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for eactch
        // h of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "TODO abrir o software que eles ja tem", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = new Intent(MainActivity.this, Poupanca.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        public void mock(){

        }

        private SpannableString generateCenterSpannableText() {

            SpannableString s = new SpannableString("Custos Mensais");
            s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length(), 0);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length(), 0);
            s.setSpan(new RelativeSizeSpan(.8f), 14, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.ITALIC), s.length(), s.length(), 0);
            s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length(), s.length(), 0);
            return s;
        }

        public void load(final View rootView){
            new HttpRequestTask(
                    new HttpRequest("https://hackatonoriginalback.herokuapp.com/transaction-history"),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            try{
                                JSONArray array = new JSONArray(response.body);
                                activity.transactions.clear();
                                for(int i = 0; i < array.length(); i++){
                                    String nome = (String) array.getJSONObject(i).getString("description");
                                    String action = (String) array.getJSONObject(i).getString("transaction_type");
                                    Double valor = array.getJSONObject(i).getDouble("transaction_amount");

                                    if(action.equals("Débito")){
                                        valor = -valor;
                                    }

                                    System.out.println(nome);

                                    activity.transactions.add(new Transaction(nome, valor));
                                }
                                listView.setAdapter(new MainListAdapter(activity, activity.transactions));
                            }catch (Exception e ){
                                System.out.println(e.getMessage());
                                e.printStackTrace();

                            }
                        }
                    }).execute();

            new HttpRequestTask(
                    new HttpRequest("https://hackatonoriginalback.herokuapp.com/balance"),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            try{
                                JSONObject obj = new JSONObject(response.body);
                                Double currentBalance = obj.getDouble("current_balance");
                                Double savings = obj.getDouble("savings");

                                ((TextView) rootView.findViewById(R.id.textView3)).setText("R$  " + (currentBalance));
                                ((TextView) rootView.findViewById(R.id.savings)).setText("R$  " + (savings));

                                Double available = obj.getDouble("available_limit");
                                Double limit = obj.getDouble("current_limit");
                            }catch (Exception e ){
                                System.out.println(e.getMessage());
                                e.printStackTrace();

                            }
                        }
                    }).execute();

            if(activity.isFirstTime) {activity.isFirstTime = false; return;}
            new HttpRequestTask(
                    new HttpRequest("https://hackatonoriginalback.herokuapp.com/tips-budget-cut"),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            try{
                                JSONObject obj = new JSONObject(response.body);
                                Integer label = obj.getInt("label");

                                String myMessage = "";

                                switch (label){
                                    case 0:
                                        myMessage = "Você pode diminuir seus gastos em transporte de forma a favorecer o cresicmento do seu porquinho :D !";
                                    case 1:
                                        myMessage = "Apesar de ser muito divertido jogar diferentes títulos, você pode também dividir um pouco deste dinheiro com o seu porquinho ;D !";
                                    case 2:
                                        myMessage = "Agora que estamos com dinheiro, que tal guardar um pouco desse dinheiro no porquinho ou investí-lo?";
                                    case 3:
                                        myMessage = "Agora que estamos com dinheiro, que tal guardar um pouco desse dinheiro no porquinho ou investí-lo?";
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                builder.setTitle("Nova dica!");
                                builder.setMessage(myMessage);
                                builder.setNegativeButton("Mais tarde", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                    }
                                });
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.create().show();

                            }catch (Exception e ){
                                System.out.println(e.getMessage());
                                e.printStackTrace();

                            }
                        }
                    }).execute();

        }
        private static final String ARG_SECTION_NUMBER = "section_number";
        MainActivity activity;

        public PlaceholderFragment(MainActivity activity) {
            this.activity = activity;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, MainActivity activity) {
            PlaceholderFragment fragment = new PlaceholderFragment(activity);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        ListView listView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            load(rootView);
            mock();

            listView = (ListView) rootView.findViewById(R.id.main_list);

            return rootView;
        }
    }

    public static class Fragment2 extends Fragment {

        LineChart mLineChart;

        private void load(View rootView){

        }

        Activity activity;

        public Fragment2(Activity activity){
            this.activity = activity;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_two, container, false);

            load(rootView);

            return rootView;
        }

        public static Fragment2 newInstance(int sectionNumber, Activity activity) {
            Fragment2 fragment = new Fragment2(activity);
            Bundle args = new Bundle();

            return fragment;
        }
    }

    public static class Fragment3 extends Fragment {

        private void load(View rootView){
//            ((ViewPager) rootView.findViewById(R.id.pager)).setAdapter(new PagerAdapter() {
//
//                @Override
//                public Object instantiateItem(ViewGroup container, int position) {
//                    if(position == 0)
//                        return activity.findViewById(R.layout.fragment_tutorial);
//                    return activity.findViewById(R.layout.fragment_tutorial2);
//                }
//
//                @Override
//                public int getCount() {
//                    return 2;
//                }
//
//                @Override
//                public boolean isViewFromObject(View view, Object object) {
//                    return view == ((View) object);
//                }
//            });
        }

        Activity activity;

        public Fragment3(Activity activity){
            this.activity = activity;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_three, container, false);

            load(rootView);

            return rootView;
        }

        public static Fragment3 newInstance(int sectionNumber, Activity activity) {
            Fragment3 fragment = new Fragment3(activity);
            Bundle args = new Bundle();

            return fragment;
        }
    }
    public static class Fragment4 extends Fragment {

        private void load(View rootView){
//            ((ViewPager) rootView.findViewById(R.id.pager)).setAdapter(new PagerAdapter() {
//
//                @Override
//                public Object instantiateItem(ViewGroup container, int position) {
//                    if(position == 0)
//                        return activity.findViewById(R.layout.fragment_tutorial);
//                    return activity.findViewById(R.layout.fragment_tutorial2);
//                }
//
//                @Override
//                public int getCount() {
//                    return 2;
//                }
//
//                @Override
//                public boolean isViewFromObject(View view, Object object) {
//                    return view == ((View) object);
//                }
//            });
        }

        Activity activity;

        public Fragment4(Activity activity){
            this.activity = activity;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_four, container, false);

            load(rootView);
            new HttpRequestTask(
                    new HttpRequest("https://hackatonoriginalback.herokuapp.com/balance"),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            try{
                                JSONObject obj = new JSONObject(response.body);
                                Double currentBalance = obj.getDouble("current_balance");
                                Double savings = obj.getDouble("savings");
                                Double available = obj.getDouble("available_limit");
                                Double limit = obj.getDouble("current_limit");

                                ((TextView) rootView.findViewById(R.id.textView3)).setText("R$  " + (limit));
                                ((TextView) rootView.findViewById(R.id.emuso)).setText("R$  " + (limit - available));
                                ((TextView) rootView.findViewById(R.id.disp)).setText("R$  " + (available));
                            }catch (Exception e ){
                                System.out.println(e.getMessage());
                                e.printStackTrace();

                            }
                        }
                    }).execute();

            return rootView;
        }

        public static Fragment4 newInstance(int sectionNumber, Activity activity) {
            Fragment4 fragment = new Fragment4(activity);
            Bundle args = new Bundle();

            return fragment;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages. */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return PlaceholderFragment.newInstance(0, MainActivity.this);
            else if(position == 1)
                return Fragment2.newInstance(1, MainActivity.this);
            else if(position == 2)
                return Fragment4.newInstance(1, MainActivity.this);
            else
                return Fragment3.newInstance(1, MainActivity.this);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }
}
