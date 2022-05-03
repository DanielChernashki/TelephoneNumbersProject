package com.example.telephonenumbersproject;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class DataBaseActivity extends AppCompatActivity {




    protected void SelectSQL(String SelectQ, String[] args,
                             OnSelectSuccess success)
            throws Exception {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(
                        getFilesDir().getPath() + "/Numbers.db", null
                );
        Cursor cursor = db.rawQuery(SelectQ, args);

        while (cursor.moveToNext()) {
            @SuppressLint("Range" ) String ID = cursor.getString(cursor.getColumnIndex("ID" ));
            @SuppressLint("Range" ) String Name = cursor.getString(cursor.getColumnIndex("Name" ));
            @SuppressLint("Range" ) String Tel = cursor.getString(cursor.getColumnIndex("Tel" ));
            @SuppressLint("Range" ) String AdditionalInformation = cursor.getString(cursor.getColumnIndex("AdditionalInformation" ));
            @SuppressLint("Range") String spinner = cursor.getString(cursor.getColumnIndex("spinner"));
            success.OnElementSelected(ID, Name, Tel ,AdditionalInformation, spinner);
        }
        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception {
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(
                        getFilesDir().getPath() + "/Numbers.db", null
                );
        if (args != null)
            db.execSQL(SQL, args);
        else
            db.execSQL(SQL);


        db.close();
        success.OnSuccess();
    }

    protected void initDB() throws Exception {
        ExecSQL(
                "CREATE TABLE if not exists Numbers ( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null," +
                        "Tel text not null, " +

                        "AdditionalInformation not null,"+
                        "spinner not null," +
                        "unique(Name, Tel)"
                        + ");",
                null,
                () -> Toast.makeText(getApplicationContext(), "DB Init Successfull",
                        Toast.LENGTH_SHORT)
                        .show()
        );
    }

    protected interface OnQuerySuccess {
        public void OnSuccess();
    }

    protected interface OnSelectSuccess {
        public void OnElementSelected(String ID, String Name, String Tel
                                      , String AdditionalInformation,String spinner);
    }

}
