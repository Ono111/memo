package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.memo.R;


public class MainActivity extends AppCompatActivity{

    // MemoHelperクラスを定義
    MemoHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ヘルパーを準備する
        if(helper == null){
            helper = new MemoHelper(this);
        }

        //メモリストデータを格納する
        final ArrayList<HashMap<String, String>> memoList = new ArrayList<>();

        //データベースを取得する
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            //rawQueryというSELECT専用メソッドを使用してデータを取得する
            Cursor c = db.rawQuery("select uuid, body from MEMO_TABLE order by id", null);

            //Cursorが存在するのかの確認
            while(c.moveToNext()){
                HashMap<String, String> data = new HashMap<>();
                //取得された絡むと型を指定してデータを取得する

                String uuid = c.getString(0);
                String body = c.getString(1);

                data.put("body", body);
                data.put("id", uuid);
                memoList.add(data);
            }

        }finally {
            db.close();
        }

        //adapter生成
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                memoList,
                android.R.layout.simple_list_item_2,
                new String[]{"body", "id"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        ListView listView = (ListView) findViewById(R.id.memoList);
        listView.setAdapter(simpleAdapter);

        //メモをタップしたときの処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);

                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = (TextView)two.getText2();
                String str = (String) idTextView.getText();
                intent.putExtra("id", str);
                startActivity(intent);
            }
        });


        Button newButton = (Button) findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // CreateMemoActivityへ遷移
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("id", "");
                startActivity(intent);
            }
        });

    }
}
