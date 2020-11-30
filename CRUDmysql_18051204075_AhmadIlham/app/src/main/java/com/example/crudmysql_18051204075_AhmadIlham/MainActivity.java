package com.example.crudmysql_18051204075_AhmadIlham;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.crudmysql_18051204075_AhmadIlham.API.RetroServer;
import com.example.crudmysql_18051204075_AhmadIlham.Model.DataModel;
import com.example.crudmysql_18051204075_AhmadIlham.Model.ResponseModel;
import com.example.crudmysql_18051204075_AhmadIlham.adapter.AdapterData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModel> listData = new ArrayList<>();
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    private FloatingActionButton fabTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
        fabTambah = findViewById(R.id.fab_tambah);
        rvData = findViewById(R.id.rv_data);
        lmData = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);
        getData();

        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                getData();
                srlData.setRefreshing(false);
            }
        });
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData(){
        pbData.setVisibility(View.VISIBLE);
        Call<ResponseModel> tampilData = RetroServer.getMhsApi().ardGetMahasiswa();

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
              // Log.e("Succes", response.body().getMessage());
               listData = response.body().getData();
               adData = new AdapterData(MainActivity.this, listData);
               rvData.setAdapter(adData);
               adData.notifyDataSetChanged();
               pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Tidak Terhubung", Toast.LENGTH_SHORT).show();
                // Log.e("Failure",t.getLocalizedMessage());
                pbData.setVisibility(View.INVISIBLE);
            }
        });

    }

}