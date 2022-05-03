package com.example.telephonenumbersproject;

import androidx.annotation.CallSuper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends DataBaseActivity {



    protected EditText editTelephoneNumber, editName, AdditionalInformation;
    protected Button btnInsert;

    protected ListView listOfItems;

    protected void listView() throws Exception {
        final ArrayList<String> listResult = new ArrayList<>();
        SelectSQL("SELECT * FROM NUMBERS ORDER BY Name", null,
                new OnSelectSuccess() {
                    @Override
                    public void OnElementSelected(String ID, String Name, String Tel, String AdditionalInformation, String spinner) {
                        listResult.add(ID + "\t" + Name + "\t" + Tel + "\t" + AdditionalInformation + "\t"+ spinner + "\t" );
                    }
                }
        );
        listOfItems.clearChoices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_list_view,
                R.id.textView, listResult
        );
        listOfItems.setAdapter(arrayAdapter);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        try {
            listView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        editTelephoneNumber=findViewById(R.id.editTelephoneNumber);
        editTelephoneNumber.setText("359");
        editName=findViewById(R.id.editName);
        AdditionalInformation=findViewById(R.id.AdditionalInformation);

        btnInsert=findViewById(R.id.btnInsert);

        listOfItems=findViewById(R.id.listOfItems);
        if( editName.getText().toString().length() == 0 )
            editName.setError( "Задължително поле!" );
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Попълнтете полето 'Име' ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    btnInsert.setOnClickListener(view -> {
                        try {
                            ExecSQL("INSERT INTO NUMBERS(Name,Tel,AdditionalInformation, spinner)" +
                                            "VALUES(?, ?, ?, ?)", new Object[]{
                                            editName.getText().toString(),
                                            editTelephoneNumber.getText().toString(),
                                            AdditionalInformation.getText().toString(),
                                            spinner.getSelectedItem().toString()

                                    },
                                    ()-> {
                                        Toast.makeText(getApplicationContext(),
                                                "Insert Successful", Toast.LENGTH_SHORT).show();
                                        try {
                                            listView();
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Select error:" + e.getLocalizedMessage()
                                                    , Toast.LENGTH_LONG).show();
                                            ;
                                        }
                                    }
                            );
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Insert Database error:"+ e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });



        listOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selected="";
                TextView clickedText=view.findViewById(R.id.textView);
                selected=clickedText.getText().toString();
                String category = spinner.getSelectedItem().toString();
                String[] elements = selected.split("\t");
                String ID=elements[0];
                String Name=elements[1];
                String Tel= elements[2];

                String AdditionalInformation = elements[3];
                category = elements[4].trim();

                Intent intent = new Intent(MainActivity.this,
                        UpdateDelete.class);
                Bundle b= new Bundle();
                b.putString("ID", ID);
                b.putString("Name", Name);
                b.putString("Tel", Tel);

                b.putString("AdditionalInformation", AdditionalInformation);
                b.putString("Category", category);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);
            }
        });
        try {
            initDB();
            listView();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Exception: " +
                    e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        }
    }
}
