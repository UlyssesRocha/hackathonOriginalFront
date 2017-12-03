package com.alan.original.hackathon.hackathonapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Poupanca extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poupanca);

        ((Button) findViewById(R.id.confirmarPoup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i < 10) i = 10;
                seekBar.setProgress(i);
                ((TextView) findViewById(R.id.seekbarvalue)).setText("R$ " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        WindowManager.LayoutParams layout = new WindowManager.LayoutParams();
        layout.copyFrom(getWindow().getAttributes());
        layout.height = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes(layout);
    }
}
