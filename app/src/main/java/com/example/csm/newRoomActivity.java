package com.example.csm;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class newRoomActivity extends AppCompatActivity {
    private ViewHolder viewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

        viewHolder.et_newRoomName = findViewById(R.id.et_newRoomName);
        viewHolder.et_newRoomDesignation = findViewById(R.id.et_newRoomDesignation);

        viewHolder.bt_newRoomAdd = findViewById(R.id.bt_newRoomAdd);
        viewHolder.bt_newRoomBack = findViewById(R.id.bt_newRoomBack);


        viewHolder.bt_newRoomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = viewHolder.et_newRoomName.getText().toString();
                String designation = viewHolder.et_newRoomDesignation.getText().toString();

                if(roomName.trim().isEmpty() || designation.trim().isEmpty()){
                    Toast.makeText(newRoomActivity.this, "Preencher todos os campos.", Toast.LENGTH_SHORT).show();
                }

                CollectionReference reference = FirebaseFirestore.getInstance().collection("Rooms");
                reference.add(new Rooms(roomName, designation));
                Toast.makeText(newRoomActivity.this, "Nova sala adicionada.", Toast.LENGTH_SHORT).show();
                clearAll();
                finish();
            }
        });

        viewHolder.bt_newRoomBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class ViewHolder{
        EditText et_newRoomName, et_newRoomDesignation;
        Button bt_newRoomAdd, bt_newRoomBack;
    }
    private void clearAll() {
        viewHolder.et_newRoomName.setText("");
        viewHolder.et_newRoomDesignation.setText("");
    }

}