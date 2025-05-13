package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    myDBHelper myHelper;
    EditText edtName, edtNumber, edtNameResult,  edtNumberResult;
    Button btnInit, btnInsert, btnSelect;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("가수 그룹 관리 DB");

        edtName=(EditText) findViewById(R.id.edtName);
        edtNumber=(EditText) findViewById(R.id.edtNum);
        edtNameResult=(EditText) findViewById(R.id.edtNameResult);
        edtNumberResult=(EditText) findViewById(R.id.edtNumResult);
        btnInit=(Button) findViewById(R.id.btnInit);
        btnInsert=(Button) findViewById(R.id.btnInput);
        btnSelect=(Button) findViewById(R.id.btnSearch);

        myHelper = new myDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB=myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB,1,2);
                sqlDB.close();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO groupTBL Values('"
                            + edtName.getText().toString() + "', "
                            + edtNumber.getText().toString() + ");");
                    Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "에러: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    if (sqlDB != null) sqlDB.close();
                }
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB=myHelper.getWritableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;",null);
                String strNames="그룹 이름"+"\r\n"+"---------"+"\r\n";
                String strNumbers="인원"+"\r\n"+"---------"+"\r\n";

                while (cursor.moveToNext()){
                    strNames+=cursor.getString(0)+"\r\n";
                    strNumbers+=cursor.getString(1)+"\r\n";
                }
                edtNameResult.setText(strNames);
                edtNumberResult.setText(strNumbers);
                cursor.close();
                sqlDB.close();
            }
        });
    }
    public class myDBHelper extends SQLiteOpenHelper{
        public myDBHelper(Context context){
            super(context,"groupDB",null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL("Create Table groupTBL(gname Char(20) Primary key, " +
                    "gNumber Integer);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("Drop Table If exists groupTBL");
            onCreate(db);
        }
    }
}
