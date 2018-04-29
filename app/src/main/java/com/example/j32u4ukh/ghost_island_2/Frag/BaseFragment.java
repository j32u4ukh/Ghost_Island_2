package com.example.j32u4ukh.ghost_island_2.Frag;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.j32u4ukh.ghost_island_2.CardActivity;
import com.example.j32u4ukh.ghost_island_2.R;

import java.util.HashMap;
import java.util.Map;

public class BaseFragment extends Fragment {
    private String title = "";
    private static final String DATA_NAME = "name";
    private static final String KEYS = "keys";
    TableLayout tableLayout;
    public static Map<String, Integer> map = new HashMap<>();
    String[] keys = new String[9];
    View fragmentView;

    public static BaseFragment newInstance(String title, String[] keys) {
        BaseFragment fragment = new BaseFragment();
        fragment.setTitle(title);

        //pass data
        Bundle args = new Bundle();
        args.putString(DATA_NAME, title);
        args.putStringArray(KEYS, keys);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data：DATA_NAME當作KEY，取得後面的VALUE
        title = getArguments().getString(DATA_NAME);
        keys = getArguments().getStringArray(KEYS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tableLayout = view.findViewById(R.id.tableLayout);
        tableLayout.setStretchAllColumns(true);
        map.put("001", R.drawable.card001);
        map.put("002", R.drawable.card002);
        map.put("003", R.drawable.card003);
        map.put("004", R.drawable.card004);
        map.put("005", R.drawable.card005);
        map.put("006", R.drawable.card006);
        map.put("007", R.drawable.card007);
        map.put("008", R.drawable.card008);
        map.put("009", R.drawable.card009);
        map.put("1001", R.drawable.card1001);
        map.put("1002", R.drawable.card1002);
        fragmentView = view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int imageWidth = displayMetrics.widthPixels / 3;
        int fragmentHeight = displayMetrics.heightPixels - dpToPixel(56 + 71, fragmentView);
        int imageHeight = fragmentHeight / 3;

        // 清除原本的表格內容
        tableLayout.removeAllViewsInLayout();

        for(int i = 0; i < 9; i +=3){
            TableRow tablerow = new TableRow(getActivity());
            tablerow.setBackgroundColor(getResources().getColor(R.color.missionList_content));
            for(int n = i; n < i + 3; n++){
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                if(keys[n] != null){
                    imageView.setImageDrawable(getResources().getDrawable(map.get(keys[n])));
                    final String key = keys[n];
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), CardActivity.class);
                            intent.putExtra("key", key); //可放所有基本類別，Bundle基本相同，但還可以傳物件
                            startActivity(intent);
                        }
                    });
                }else {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.card000));
                }
                imageView.setLayoutParams(new TableRow.LayoutParams(imageWidth, imageHeight, 1.0f));

                tablerow.addView(imageView);
            }
            tableLayout.addView(tablerow, layoutParams);
        }
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getIndicatorColor() {
        return Color.BLUE;
    }
    public int getDividerColor() {
        return Color.GRAY;
    }

    public static float getDensity(View view){
        DisplayMetrics metrics = view.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static int dpToPixel(float dp, View view){
        return (int) (dp * getDensity(view));
    }

    /*public static int pixelToDp(float px, View view){
        return (int) (px / getDensity(view));
    }*/
}

