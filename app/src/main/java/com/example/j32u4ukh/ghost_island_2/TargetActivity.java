package com.example.j32u4ukh.ghost_island_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TargetActivity extends AppCompatActivity {
    ListView listView;
    static String key;
    Map<String, String> cardName = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        Intent intent = this.getIntent();
        key = intent.getStringExtra("key");
        listView = findViewById(R.id.listView);
        cardName.put("1001", "掠奪");
        cardName.put("1002", "竊盜");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTargets();
    }

    // 取得攻擊目標
    public void getTargets(){
        String URL = "https://carboxyl-hatchet.000webhostapp.com/GhostIsland2/getTargets.php";
        final List<Map> targets = new ArrayList<>();

        // 建立請求
        RequestQueue queue = Volley.newRequestQueue(TargetActivity.this);

        // 執行請求
        queue.add(new StringRequest(URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for(int m = 0; m < jsonArray.length(); m++){
                        Map<String, Object> cardsNumber = new HashMap<>();
                        cardsNumber.put("member", jsonArray.getJSONObject(m).getString("member"));
                        cardsNumber.put("001", jsonArray.getJSONObject(m).getInt("001"));
                        cardsNumber.put("002", jsonArray.getJSONObject(m).getInt("002"));
                        cardsNumber.put("003", jsonArray.getJSONObject(m).getInt("003"));
                        cardsNumber.put("004", jsonArray.getJSONObject(m).getInt("004"));
                        cardsNumber.put("005", jsonArray.getJSONObject(m).getInt("005"));
                        cardsNumber.put("006", jsonArray.getJSONObject(m).getInt("006"));
                        cardsNumber.put("007", jsonArray.getJSONObject(m).getInt("007"));
                        cardsNumber.put("008", jsonArray.getJSONObject(m).getInt("008"));
                        cardsNumber.put("009", jsonArray.getJSONObject(m).getInt("009"));
                        targets.add(cardsNumber);
                    }
                    listView.setAdapter(new TargetAdapter(TargetActivity.this, targets));
                }catch (Exception e){
                    Toast.makeText(TargetActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyError
            }
        }));
    }

    // ListView適配器
    private class TargetAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<Map> targets;
        String member = getMember();

        private TargetAdapter(Context context, List<Map> targets) {
            layoutInflater = LayoutInflater.from(context);
            this.targets = targets;
        }

        @Override
        public int getCount() {
            return targets.size();
        }

        @Override
        public Object getItem(int position) {
            return targets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 將內容填入事先準備的框架中
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.target_adapter, parent, false);
            }
            TextView targetName = convertView.findViewById(R.id.targetName);
            TableLayout targetInfo = convertView.findViewById(R.id.targetInfo);
            TextView info1 = convertView.findViewById(R.id.info1);
            TextView info2 = convertView.findViewById(R.id.info2);
            TextView info3 = convertView.findViewById(R.id.info3);
            TextView info4 = convertView.findViewById(R.id.info4);
            TextView info5 = convertView.findViewById(R.id.info5);
            TextView info6 = convertView.findViewById(R.id.info6);
            TextView info7 = convertView.findViewById(R.id.info7);
            TextView info8 = convertView.findViewById(R.id.info8);
            TextView info9 = convertView.findViewById(R.id.info9);
            Map target = targets.get(position);
            int _001 = (int) target.get("001");
            int _002 = (int) target.get("002");
            int _003 = (int) target.get("003");
            int _004 = (int) target.get("004");
            int _005 = (int) target.get("005");
            int _006 = (int) target.get("006");
            int _007= (int) target.get("007");
            int _008 = (int) target.get("008");
            int _009 = (int) target.get("009");
            final String getTarget = (String) target.get("member");
            int[] cardInfomation = {_001, _002, _003, _004, _005, _006, _007, _008, _009};

            switch (key){
                case "1001":
                    targetName.setText(String.format(Locale.getDefault(),"名稱：%s", getTarget));
                    info1.setText(String.format(Locale.getDefault(),"%d張", _001));
                    info2.setText(String.format(Locale.getDefault(),"%d張", _002));
                    info3.setText(String.format(Locale.getDefault(),"%d張", _003));
                    info4.setText(String.format(Locale.getDefault(),"%d張", _004));
                    info5.setText(String.format(Locale.getDefault(),"%d張", _005));
                    info6.setText(String.format(Locale.getDefault(),"%d張", _006));
                    info7.setText(String.format(Locale.getDefault(),"%d張", _007));
                    info8.setText(String.format(Locale.getDefault(),"%d張", _008));
                    info9.setText(String.format(Locale.getDefault(),"%d張", _009));
                    break;
                case "1002":
                    targetName.setText(String.format(Locale.getDefault(),"名稱：%s\n\n持有卡片種類：%s\n", getTarget, targetInfomation(cardInfomation)));
                    targetInfo.setVisibility(View.GONE);
                    break;
            }

            // 點擊清單事件
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // 建立請求
                    RequestQueue queue = Volley.newRequestQueue(TargetActivity.this);

                    Toast.makeText(TargetActivity.this, "使用\t" + cardName.get(key) + "\t攻擊\t" + getTarget, Toast.LENGTH_SHORT).show();

                    // 執行請求
                    queue.add(new UpdateRequest(member, getTarget, key, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String card = jsonObject.getString("card");
                                Toast.makeText(TargetActivity.this, "獲得No." + card, Toast.LENGTH_SHORT).show();
                                // 處理完，回到 MainActivity
                                Intent intent = new Intent(TargetActivity.this, MainActivity.class);
                                startActivity(intent);
                            }catch (Exception ex){
                                // 123
                            }
                        }
                    }));
                }
            });
            return convertView;
        }
    }

    // 對伺服器端的處理
    class UpdateRequest extends StringRequest {
        private static final String URL = "https://carboxyl-hatchet.000webhostapp.com/GhostIsland2/updateRequest.php";
        private Map<String, String> params;

        private UpdateRequest(String member, String target, String jumon, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("member", member);
            params.put("target", target);
            params.put("jumon", jumon);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
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

    public String targetInfomation(int[] cardInfomation){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < cardInfomation.length; i++){
            if(cardInfomation[i] > 0){
                String num = String.valueOf(i + 1) + " ";
                stringBuilder.append(num);
            }
        }
        return String.valueOf(stringBuilder);
    }
}
