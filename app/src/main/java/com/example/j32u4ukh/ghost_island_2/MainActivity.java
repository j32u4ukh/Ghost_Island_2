package com.example.j32u4ukh.ghost_island_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCards();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 底部導航按鈕
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getCards();
                        return true;
                    case R.id.navigation_scanning:
                        // 開啟QRcode掃描
                        IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                        scanIntegrator.initiateScan();
                        return true;
                    case R.id.navigation_mission:
                        transaction.replace(R.id.main_fragment, MissionFragment.newInstance()).commit();
                        return true;
                }
                return false;
            }
        });
    }

    // 取得卡片
    public void getCards(){
        String member = getMember();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // 之後讀取內部記憶的會員名稱
        ReadRequest readRequest = new ReadRequest(member, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int _001 = jsonObject.getInt("001");
                    int _002 = jsonObject.getInt("002");
                    int _003 = jsonObject.getInt("003");
                    int _004 = jsonObject.getInt("004");
                    int _005 = jsonObject.getInt("005");
                    int _006 = jsonObject.getInt("006");
                    int _007 = jsonObject.getInt("007");
                    int _008 = jsonObject.getInt("008");
                    int _009 = jsonObject.getInt("009");
                    int _1001 = jsonObject.getInt("1001");
                    int _1002 = jsonObject.getInt("1002");
                    int[] cardsAmount = {_001, _002, _003, _004, _005, _006, _007, _008, _009, _1001, _1002};
                    String[] cardsName = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "1001", "1002"};
                    ArrayList<String> arrayList = new ArrayList<>();
                    for(int i = 0; i < cardsAmount.length; i++){
                        for(int n = 0; n < cardsAmount[i]; n++){
                            arrayList.add(cardsName[i]);
                        }
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, TabFragment.newInstance(arrayList))
                            .commit();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        queue.add(readRequest);
    }

    // 讀取  後執行的動作
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult!=null){

            // 取得讀取內容 scanContent
            String scanContent = scanningResult.getContents();

            // 判斷QRcode內容後，紀錄消費行為
            try{
                int startIndex = scanContent.lastIndexOf("item=") + 5;
                int endIndex = scanContent.length();
                shohiRecord(scanContent.substring(startIndex, endIndex));
            }catch (NullPointerException npe){
                // 123
            }
        }else{
            Toast.makeText(getApplicationContext(),"無法辨識，請再試一次", Toast.LENGTH_SHORT).show();
        }
    }

    public void shohiRecord(String item){
        String member = getMember();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        CreateRequest createRequest = new CreateRequest(member, item, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String card = jsonObject.getString("card");
                    Toast.makeText(MainActivity.this, "獲得No." + card, Toast.LENGTH_SHORT).show();
                    getCards();
                }catch (Exception ex){
                    // 123
                }
            }
        });
        queue.add(createRequest);
    }

    // 按返回鍵退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出確認
    public void ConfirmExit(){
        AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("離開")
                .setMessage("確定要離開此程式嗎?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
                    public void onClick(DialogInterface dialog, int i) {
                        // MainActivity.this.finish(); 僅關閉 MainActivity 這個 Activity
                        // 新方法是關閉整個應用程式
                        android.os.Process.killProcess(android.os.Process.myPid());

                    }
                })
                .setNegativeButton("否",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        //不退出不用執行任何操作
                    }
                })
                .show();//顯示對話框
    }

    class ReadRequest extends StringRequest {
        private static final String URL = "https://carboxyl-hatchet.000webhostapp.com/GhostIsland2/getCards.php";
        private Map<String, String> params;

        private ReadRequest(String member, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("member", member);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    class CreateRequest extends StringRequest {
        private static final String URL = "https://carboxyl-hatchet.000webhostapp.com/GhostIsland2/shohiRecord.php";
        private Map<String, String> params;

        private CreateRequest(String member, String item, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("member", member);
            params.put("item", item);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    public String getMember(){
        String member;
        try{
            FileInputStream fileInputStream = openFileInput("Member.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream,"utf-8"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            member = String.valueOf(stringBuilder);
            fileInputStream.close();
        }catch (IOException ioe){
            member = "";
        }
        return member;
    }
}

