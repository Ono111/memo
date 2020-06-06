package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.memo.R;

import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    MemoHelper helper = null;
    String id = "";
    boolean newFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if(helper == null){
            helper = new MemoHelper(Main2Activity.this);
        }

        Intent intent = this.getIntent();

        //値を取得
        id = intent.getStringExtra("id");

        if (id.equals("")) {
            newFlag = true;
        } else {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                Cursor c = db.rawQuery("select body from MEMO_TABLE where uuid = '" + id + "'", null);
                while (c.moveToNext()) {
                    String dispBody = c.getString(0);
                    EditText body = (EditText) findViewById(R.id.body);
                    body.setText(dispBody, TextView.BufferType.NORMAL);
                }
            } finally {
                db.close();
            }
        }

        //登録ボタン作成
        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText body = (EditText) findViewById(R.id.body);
                String bodyStr = body.getText().toString();

                //データベースに保存する
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    if(newFlag){
                        //新しくidを作成する
                        id = UUID.randomUUID().toString();
                        db.execSQL("insert into MEMO_TABLE(uuid, body) VALUES('" + id + "','" + bodyStr + "')");
                    }else{
                        db.execSQL("update MEMO_TABLE set body = '" + bodyStr + "'where uuid = '" + id + "'");
                    }

                } finally {
                    db.close();
                }
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //ボタンが押された時の処理
        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

