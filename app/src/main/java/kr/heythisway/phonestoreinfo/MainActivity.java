package kr.heythisway.phonestoreinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<StoreInfo> datas;
    StoreInfo storeInfo;
    DbHelper helper;

    Button btnGoDetail;
    RecyclerView listView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ORMLite 사용
        helper = DbHelper.getInstance(this);
        storeInfo = new StoreInfo();
        datas = helper.readAll();

        // 어댑터 구현
        listView = (RecyclerView) findViewById(R.id.listView);
        adapter = new MyAdapter(datas);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));


        btnGoDetail = (Button) findViewById(R.id.btnGoDe);
        btnGoDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<Holder> {
        List<StoreInfo> datas = new ArrayList<>();
        StoreInfo data = new StoreInfo();

        public MyAdapter(List<StoreInfo> datas) {
            this.datas = datas;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getLayoutInflater().getContext()).inflate(R.layout.item_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            data = datas.get(position);
            holder.setTextStoreCode(data.getStoreCode());
            holder.setTextStoreName(data.getStoreName());
            holder.setTextStoreAddress(data.getAddress());

            holder.btnItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", data.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        private TextView textStoreCode, textStoreName, textStoreAddress;
        private LinearLayout btnItem;

        public Holder(View itemView) {
            super(itemView);
            textStoreCode = (TextView) itemView.findViewById(R.id.textStoreCode);
            textStoreName = (TextView) itemView.findViewById(R.id.textStoreName);
            textStoreAddress = (TextView) itemView.findViewById(R.id.textStoreAddress);
            btnItem = (LinearLayout) itemView.findViewById(R.id.btnItem);


        }

        public void setTextStoreCode(String textStoreCode) {
            this.textStoreCode.setText(textStoreCode);
        }

        public void setTextStoreName(String textStoreName) {
            this.textStoreName.setText(textStoreName);
        }

        public void setTextStoreAddress(String textStoreAddress) {
            this.textStoreAddress.setText(textStoreAddress);
        }
    }
}
