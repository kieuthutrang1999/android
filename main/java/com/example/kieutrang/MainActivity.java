package com.example.kieutrang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edta, edtb;
    Button button;
    TextView result;
    ListView listView;
    ArrayList<String>  arrayList;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList.remove(position);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edta.getText().toString().isEmpty() && edtb.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "xin moi nhap du", Toast.LENGTH_SHORT).show();
                }
                else {
                    double a = Integer.parseInt(edta.getText().toString());
                    double b = Integer.parseInt(edtb.getText().toString());
                    double c = a/b;
                    result.setText(a+"/"+b+"="+c);
                    arrayList.add(a+"/"+b+"="+c);
                    arrayAdapter.notifyDataSetChanged();
                    edta.setText("");
                    edtb.setText("");
                }

            }
        });


    }

    private void Init() {
        arrayList = new ArrayList<>();
        edta = (EditText)findViewById(R.id.edta);
        edtb = (EditText)findViewById(R.id.edtb);
        button = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listview);
        result = (TextView)findViewById(R.id.result);
    }
}