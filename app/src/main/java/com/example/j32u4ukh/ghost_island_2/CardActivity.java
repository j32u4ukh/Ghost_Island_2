package com.example.j32u4ukh.ghost_island_2;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import static com.example.j32u4ukh.ghost_island_2.Frag.BaseFragment.map;

public class CardActivity extends AppCompatActivity {
    Activity instance;
    ConstraintLayout constraintLayout;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        instance = this;
        constraintLayout = findViewById(R.id.card);
        Intent intent = this.getIntent();
        key = intent.getStringExtra("key");
    }

    @Override
    protected void onResume() {
        super.onResume();
        constraintLayout.setBackground(getResources().getDrawable(map.get(key)));
        final String[] jumons = {"1001", "1002"};
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isJumon = Arrays.asList(jumons).contains(key);
                if(isJumon){
                    Intent intent = new Intent(CardActivity.this, TargetActivity.class);
                    intent.putExtra("key", key);
                    startActivity(intent);
                }else {
                    Toast.makeText(CardActivity.this, key, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 按返回鍵退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
