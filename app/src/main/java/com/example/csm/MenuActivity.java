package com.example.csm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.orhanobut.dialogplus.ViewHolder;

public class MenuActivity extends AppCompatActivity {
    private ViewHolder mviewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mviewHolder.bt_menuRooms = findViewById(R.id.bt_menuRooms);
        mviewHolder.bt_menuEquipments = findViewById(R.id.bt_menuEquipments);
        mviewHolder.bt_menuQRcode = findViewById(R.id.bt_menuQRcode);

        mviewHolder.bt_menuRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ListRoomsActivity.class);
                startActivity(i);
            }
        });

        mviewHolder.bt_menuEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ListEquipmentsActivity.class);
                startActivity(i);
            }
        });


        mviewHolder.bt_menuQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private class ViewHolder {
        Button bt_menuRooms, bt_menuEquipments, bt_menuQRcode;

    }

}