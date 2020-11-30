package com.example.crudmysql_18051204075_AhmadIlham;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crudmysql_18051204075_AhmadIlham.API.RetroServer;
import com.example.crudmysql_18051204075_AhmadIlham.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    private EditText inNim,inNama,inAngkatan;
    private Button btnSimpan,btnHapus;
    private String nim,nama,angkatan;
    private int idmhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        inNim = findViewById(R.id.in_nim);
        inNama = findViewById(R.id.in_nama);
        inAngkatan = findViewById(R.id.in_angkatan);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnHapus = findViewById(R.id.btn_hapus);

        Intent data = getIntent();
        String id = data.getStringExtra("id");
        if(id !=null){
            inNim.setText(data.getStringExtra("nim"));
            inNama.setText(data.getStringExtra("nama"));
            inAngkatan.setText(data.getStringExtra("angkatan"));
            idmhs = Integer.parseInt(id);
            btnHapus.setVisibility(View.VISIBLE);

        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nim = inNim.getText().toString();
                nama = inNama.getText().toString();
                angkatan = inAngkatan.getText().toString();


                if(nim.trim().equals("")){
                    inNim.setError("Nim Harus Diisi");
                }else if(nama.trim().equals("")){
                    inNama.setError("Nama Harus Diisi");
                }else if (angkatan.trim().equals("")){
                    inAngkatan.setError("Angkatan Harus Diisi");
                }else{
                    if(id !=null){
                        updateData();
                    }else{
                        createData();
                    }
                }
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertData();
            }
        });
    }


    public void createData(){
        Call<ResponseModel> simpanData = RetroServer.getMhsApi().ardCreateMahasiswa(nim,nama,Integer.parseInt(angkatan));
    simpanData.enqueue(new Callback<ResponseModel>() {
        @Override
        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
            Toast.makeText(AddActivity.this,"Berhasil", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onFailure(Call<ResponseModel> call, Throwable t) {
            Toast.makeText(AddActivity.this, "Gagal Terhubung ke server", Toast.LENGTH_SHORT).show();

        }
    });
    }

    private void updateData(){
        Call<ResponseModel> perbaruiData = RetroServer.getMhsApi().ardUpdateMahasiswa(idmhs,nim,nama,Integer.parseInt(angkatan));
        perbaruiData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(AddActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AddActivity.this, "Gagal Terhubung Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alertData(){
        AlertDialog.Builder dialogPesan = new AlertDialog.Builder(AddActivity.this);
        dialogPesan.setMessage("Apakah Ingin menghapus?");
        dialogPesan.setCancelable(true);

        dialogPesan.setPositiveButton("hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               deleteData();
               dialogInterface.dismiss();

            }
        });

        dialogPesan.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        dialogPesan.show();
    }

    private void deleteData(){
        Call<ResponseModel> hapusData = RetroServer.getMhsApi().ardDeleteMahasiswa(idmhs);
        hapusData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(AddActivity.this, "Berhasil",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AddActivity.this, "Gagal Terhubung", Toast.LENGTH_SHORT).show();

            }
        });
    }
}