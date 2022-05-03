package com.example.telephonenumbersproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UpdateDelete extends DataBaseActivity {
    protected EditText editTelephoneNumber, editName, AdditionalInformation;
    protected Button btnUpdate, btnDelete;
    protected Spinner spinner;
    protected String ID = "";
    protected void BackToMain(){
        finishActivity(200);
        Intent i=new Intent(UpdateDelete.this,
                MainActivity.class
        );
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editTelephoneNumber = findViewById(R.id.editTelephoneNumber);
        editName = findViewById(R.id.editName);
        AdditionalInformation = findViewById(R.id.AdditionalInformation);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        if( editName.getText().toString().length() == 0 )
            editName.setError( "Задължително поле!" );
        editTelephoneNumber.setText("359");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            editName.setText(b.getString("Name"));
            editTelephoneNumber.setText(b.getString("Tel"));
            AdditionalInformation.setText(b.getString("AdditionalInformation"));

           b.putString("key5", spinner.getSelectedItem().toString());

        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (TextUtils.isEmpty(editName.getText().toString())) {
                                                 Toast.makeText(UpdateDelete.this, "Попълнтете полето 'Име'",
                                                         Toast.LENGTH_SHORT).show();
                                             } else {
                                                 btnUpdate.setOnClickListener((view -> {
                                                     try {
                                                         ExecSQL(
                                                                 "UPDATE NUMBERS SET " +
                                                                         "Name = ?, " +
                                                                         "Tel = ?, " +
                                                                         "AdditionalInformation = ?, " +
                                                                         "spinner = ?" +
                                                                         "WHERE ID = ?",
                                                                 new Object[]{
                                                                         editName.getText().toString(),
                                                                         editTelephoneNumber.getText().toString(),
                                                                         AdditionalInformation.getText().toString(),
                                                                         spinner.getSelectedItem().toString(),
                                                                         ID
                                                                 },
                                                                 () -> Toast.makeText(getApplicationContext(), "Update Succesfull", Toast.LENGTH_LONG).show()
                                                         );

                                                     } catch (Exception e) {
                                                         Toast.makeText(getApplicationContext(), "UPDATE ERROR: " + e.getLocalizedMessage(),
                                                                 Toast.LENGTH_LONG).show();
                                                     }
                                                     BackToMain();
                                                 }));
                                             }
                                         }
                                     });

        btnDelete.setOnClickListener((view -> {
            try {
                ExecSQL(
                        "DELETE FROM NUMBERS "+

                                "WHERE ID = ?",
                        new Object[]{
                                ID
                        },
                        ()-> Toast.makeText(getApplicationContext(), "Delete Succesfull", Toast.LENGTH_LONG).show()
                );

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"DELETE ERROR: "+e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
            BackToMain();
        }));
    }
}