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
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    // MemoHelperクラスを定義
    MemoHelper helper = null;

    //アダプターの設定
    ArrayList<ListItem> memoList = new ArrayList<>();
    MyListAdapter adapter = new MyListAdapter(this, memoList, R.layout.list_item);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onResume() {
        super.onResume();

        ListView list = findViewById(R.id.memoList);
        list.setAdapter(adapter);

        ListItem data = new ListItem();

        //ヘルパーを準備する
        if(helper == null){
            helper = new MemoHelper(this);
        }


        //データベースを取得する
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            //rawQueryというSELECT専用メソッドを使用してデータを取得する
            Cursor c = db.rawQuery("select uuid, body from MEMO_TABLE order by id", null);

            c.moveToFirst();
            //Cursorが存在するのかの確認
            while(c.moveTo()){

                //取得された絡むと型を指定してデータを取得する

                data.setBody(c.getString(0));
                data.setTitle(c.getString(1));
                data.setUuid(c.getString(2));
                data.setDate(c.getString(3));
                data.setDate2(c.getString(4));
                data.setDate3(c.getString(5));

                memoList.add(data);
            }

        }finally {
            db.close();
        }



        //メモをタップしたときの処理
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 選択されたビューを取得 TwoLineListItemを取得した後、text2の値を取得する
                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = (TextView)two.getText2();
                String idStr = (String) idTextView.getText();

                // 長押しした項目をデータベースから削除
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    db.execSQL("DELETE FROM MEMO_TABLE WHERE uuid = '"+ idStr +"'");
                    db.execSQL("DELETE FROM DATE_TABLE WHERE uuid = '" + idStr + "'");
                } finally {
                    db.close();
                }
                // 長押しした項目を画面から削除
                memoList.remove(position);
                adapter.notifyDataSetChanged();

                // trueにすることで通常のクリックイベントを発生させないです
                return true;
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
