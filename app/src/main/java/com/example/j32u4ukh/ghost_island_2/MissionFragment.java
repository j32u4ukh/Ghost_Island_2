package com.example.j32u4ukh.ghost_island_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MissionFragment extends Fragment {
    TextView mission1, mission2, mission3;

    public static MissionFragment newInstance() {
        return new MissionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mission, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mission1 = view.findViewById(R.id.mission1);
        mission2 = view.findViewById(R.id.mission2);
        mission3 = view.findViewById(R.id.mission3);
    }

    @Override
    public void onResume() {
        super.onResume();
        onClick(mission1, "https://carboxyl-hatchet.000webhostapp.com/webGhostIsland2/Questionnaire.php");  // 完成
        onClick(mission2, "https://www.surveycake.com/s/yPDVR"); // 完成
        onClick(mission3, "https://www.facebook.com/tzaywxwx"); // 完成
    }

    public void onClick(TextView textView, final String URL){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
