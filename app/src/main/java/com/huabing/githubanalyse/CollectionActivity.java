package com.huabing.githubanalyse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huabing.githubanalyse.Adapter.CollectionAdapter;
import com.huabing.githubanalyse.Bean.Collection;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    private ImageView ivBack;
    private TextView tvData;
    private List<Collection> collectionList;
    private RecyclerView rvCollection;
    private CollectionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        ivBack=(ImageView)findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvData=(TextView)findViewById(R.id.tv_data);

        collectionList=new ArrayList<>();
        rvCollection=(RecyclerView)findViewById(R.id.rv_collection);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        rvCollection.setLayoutManager(manager);
        adapter=new CollectionAdapter(collectionList);
        rvCollection.setAdapter(adapter);

        List<Collection> tempList= DataSupport.findAll(Collection.class);
        int length=tempList.size();
        if(length>0)
        {
            collectionList.clear();
            tvData.setVisibility(View.GONE);
            rvCollection.setVisibility(View.VISIBLE);
            for(int i=0;i<length;i++)
                collectionList.add(tempList.get(i));
            adapter.notifyDataSetChanged();
        }

    }
}
