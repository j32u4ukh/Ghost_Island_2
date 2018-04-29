package com.example.j32u4ukh.ghost_island_2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.j32u4ukh.ghost_island_2.Frag.BaseFragment;
import com.example.j32u4ukh.ghost_island_2.Frag.SlidingTabLayout;
import com.example.j32u4ukh.ghost_island_2.Frag.TabFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;



public class TabFragment extends Fragment {
    private static final String DATA_ARRAY = "arrayList";
    ArrayList<String> arrayList = new ArrayList<>();

    public static TabFragment newInstance(ArrayList<String> arrayList) {
        TabFragment tabFragment = new TabFragment();

        //pass data
        Bundle args = new Bundle();
        args.putStringArrayList(DATA_ARRAY, arrayList);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayList = getArguments().getStringArrayList(DATA_ARRAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SlidingTabLayout tabs;
        ViewPager pager;
        FragmentPagerAdapter adapter;

        //adapter
        final LinkedList<BaseFragment> fragments = getFragments();

        // 原版(getFragmentManager)在重複呼叫時，會發生有些頁或全部頁的內容消失
        // adapter = new TabFragmentPagerAdapter(getFragmentManager(), fragments);
        adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragments);

        //pager
        pager = view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        //tabs 標籤
        tabs = view.findViewById(R.id.tabs);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return fragments.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return fragments.get(position).getDividerColor();
            }
        });

        // 設定標籤顏色
        tabs.setBackgroundResource(R.color.colorPrimary);
        tabs.setViewPager(pager);
    }

    // 產生滑動頁面&內容
    private LinkedList<BaseFragment> getFragments(){
        LinkedList<BaseFragment> fragments = new LinkedList<>();

        int page = 1;
        String[] strings = new String[9];
        for(int i = 0; i < arrayList.size(); i += 9){
            for(int number = i % 9; number < 9; number++){
                try{
                    strings[number] = arrayList.get(i + number);
                }catch (IndexOutOfBoundsException ioobe){
                    //  IndexOutOfBoundsException
                }
            }
            fragments.add(BaseFragment.newInstance("Page" + String.valueOf(page), strings));
            page += 1;
            strings = new String[9];
        }
        return fragments;
    }
}

