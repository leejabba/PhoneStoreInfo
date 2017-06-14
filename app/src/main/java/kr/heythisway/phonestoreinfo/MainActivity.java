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
    static List<StoreInfo> datas = new ArrayList<>();
    StoreInfo storeInfo;
    DbHelper helper;
    Button btnGoDetail;
    RecyclerView listView;
    MyAdapter adapter;
    TextView textListCount;

    String clickedItemId;
    String intendSendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ORMLite 사용
        helper = DbHelper.getInstance(this);
        storeInfo = new StoreInfo();
        datas = helper.readAll();

        // 매장갯수 표현
        textListCount = (TextView) findViewById(R.id.textListCount);
        textListCount.setText("총 " + datas.size() + "개의 매장이 있습니다.");

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
        StoreInfo data = new StoreInfo();

        public MyAdapter(List<StoreInfo> datas) {
            MainActivity.datas = datas;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getLayoutInflater().getContext()).inflate(R.layout.item_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            data = datas.get(position);
            holder.setTextStoreCode(data.getStoreCode());
            holder.setTextStoreName(data.getStoreName());
            holder.setTextStoreAddress(data.getAddress());
            holder.setTextStoreId(data.getId());

            holder.btnItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", datas.get(position).getId());
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
        private TextView textStoreCode, textStoreName, textStoreAddress, textStoreId;
        private LinearLayout btnItem;

        public Holder(View itemView) {
            super(itemView);
            textStoreCode = (TextView) itemView.findViewById(R.id.textStoreCode);
            textStoreName = (TextView) itemView.findViewById(R.id.textStoreName);
            textStoreAddress = (TextView) itemView.findViewById(R.id.textStoreAddress);

            textStoreId = (TextView) itemView.findViewById(R.id.textStoreId);
            textStoreId.setVisibility(View.GONE);

            btnItem = (LinearLayout) itemView.findViewById(R.id.btnItem);
        }

        public void setTextStoreCode(String textStoreCode) {
            this.textStoreCode.setText(textStoreCode);
        }

        public void setTextStoreId(int textStoreId) {
            this.textStoreId.setText(textStoreId + "");
        }

        public void setTextStoreName(String textStoreName) {
            this.textStoreName.setText(textStoreName);
        }

        public void setTextStoreAddress(String textStoreAddress) {
            this.textStoreAddress.setText(textStoreAddress);
        }
    }

    // 리스트뷰 변화 내용을 반영하기 위해 datas의 내용을 깨끗이 비우고 다시 데이터베이스에서 읽어와 채운뒤 알려준다.
    @Override
    protected void onResume() {
        super.onResume();
        datas.clear();
        datas = helper.readAll();
        adapter.notifyDataSetChanged();
        textListCount.setText("총 " + datas.size() + "개의 매장이 있습니다.");
    }
}
