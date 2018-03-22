package ren.qinc.markdowneditors.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.ArrayList;
import java.util.List;

import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.activities.ThingDetailActivity;
import ren.qinc.markdowneditors.adapter.BaseRecyclerAdapter;
import ren.qinc.markdowneditors.adapter.ThingRecyclerAdapter;
import ren.qinc.markdowneditors.model.Thing;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment implements IOnSearchClickListener, Toolbar.OnMenuItemClickListener {
    private SearchFragment searchFragment;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ThingRecyclerAdapter recyclerAdapter;

    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragemnt_book, container, false);
        initBookView(view);
        return view;
    }

    //toolbar的搜索栏目
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    public void initBookView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.booktoolbar);
        toolbar.setTitle("学习助手");//标题
        toolbar.inflateMenu(R.menu.menu_main);
        setHasOptionsMenu(true);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        searchFragment = SearchFragment.newInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        toolbar.setOnMenuItemClickListener(this);
        searchFragment.setOnSearchClickListener(this);
        recyclerAdapter = new ThingRecyclerAdapter();
        recyclerAdapter.updateList(getThings());
        recyclerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<Thing>() {
            @Override
            public void onItemClick(View view, Thing item, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(getActivity(), "这是长按测试", Toast.LENGTH_SHORT).show();
//  BaseActivity.of(getActivity()).animateHomeIcon(MaterialMenuDrawable.IconState.X);
                } else {
                    Intent in = new Intent(getActivity(), ThingDetailActivity.class);
                    startActivity(in);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);
//点击添加打开一个新的界面。
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    //    之后从服务器端获取数据，目前只是测试。
//获取数据的方法
    public List<Thing> getThings() {
        ArrayList<Thing> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Thing("Thing " + i, null));
        }
        return list;

    }

    //实现了搜索文本的实现
    @Override
    public void OnSearchClick(String keyword) {
        Toast.makeText(getActivity(), keyword, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search://点击搜索
                searchFragment.show(getFragmentManager(), SearchFragment.TAG);
                break;
        }
        return true;
    }


}
