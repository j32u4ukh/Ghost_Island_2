package com.example.j32u4ukh.ghost_island_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText enterName, enterPassword;
    Button sendRegister;
    String FILENAME = "Member.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(getMember().length() > 0){
            Toast.makeText(this,"歡迎回來" + getMember(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "歡迎加入Ghost Island 2，註冊後將會隨機送您一張卡片喔~", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enterName = findViewById(R.id.enterName);
        enterPassword = findViewById(R.id.enterPassword);
        sendRegister = findViewById(R.id.sendRegister);
        sendRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getName(enterName).length() > 0){
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    RegisterRequest registerRequest = new RegisterRequest(getName(enterName), getPassword(enterPassword), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success){
                                    String card = jsonObject.getString("card");
                                    try {
                                        Toast.makeText(RegisterActivity.this, "獲得No." + card, Toast.LENGTH_SHORT).show();
                                        FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_PRIVATE);
                                        fileOutputStream.write(getName(enterName).getBytes());
                                        fileOutputStream.close();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }catch (IOException ioe){
                                        // 123
                                    }
                                }else {
                                    Toast.makeText(RegisterActivity.this, "此帳號已有人註冊", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                Toast.makeText(RegisterActivity.this, "Something Wrong!\t" + getMember(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    queue.add(registerRequest);
                }else{
                    Toast.makeText(RegisterActivity.this, "member：" + getName(enterName), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getName(EditText enterName){
        return enterName.getText().toString().trim();
    }
    public String getPassword(EditText enterPassword){
        return enterPassword.getText().toString().trim();
    }

    //TODO：新增password，尚未測試
    class RegisterRequest extends StringRequest {
        private static final String URL = "https://carboxyl-hatchet.000webhostapp.com/GhostIsland2/Register.php";
        private Map<String, String> params;

        private RegisterRequest(String member, String password, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);
            params = new HashMap<>();
            params.put("member", member);
            params.put("password", password);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    public String getMember(){
        String member;
        try{
            FileInputStream fileInputStream = openFileInput(FILENAME);
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
