package com.jackchen.day_22;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/8 14:07
 * Version 1.0
 * Params:
 * Description:  直接在主页面引用自定义View即可
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListDataScrrenView list_data_screen_view = (ListDataScrrenView) findViewById(R.id.list_data_screen_view);
        list_data_screen_view.setAdapter(new ListScreenMenuAdapter(this));
    }


    public void clickText(View view){
        Toast.makeText(MainActivity.this , "点击了 textView了" , Toast.LENGTH_SHORT).show();

    }
}
