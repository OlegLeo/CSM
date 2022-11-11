package com.example.csm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoomsDataActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ViewHolder viewHolder = new ViewHolder();
    Intent i;
    String id = "";
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_data);

        viewHolder.et_dataRoomsName = findViewById(R.id.et_dataRoomsName);
        viewHolder.et_dataDesignation = findViewById(R.id.et_dataDesignation);

        viewHolder.bt_dataRoomEdit = findViewById(R.id.bt_dataRoomEdit);
        viewHolder.bt_dataRoomDelete = findViewById(R.id.bt_dataRoomDelete);
        viewHolder.bt_dataRoomBack = findViewById(R.id.bt_dataRoomBack);

        i = getIntent();
        String path = i.getExtras().getString("path");
        id = path.substring(path.indexOf("/") + 1);

        docRef = db.collection("Rooms").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        //Toast.makeText(EquipmentsDataActivity.this, "Dados lidos com sucesso", Toast.LENGTH_SHORT).show();
                        viewHolder.et_dataRoomsName.setText(documentSnapshot.getData().get("roomName").toString());
                        viewHolder.et_dataDesignation.setText(documentSnapshot.getData().get("designation").toString());
                    }
                    else{
                        Toast.makeText(RoomsDataActivity.this, "Erro ao apresentar dados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(RoomsDataActivity.this, "Erro ao apresentar dados", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        viewHolder.bt_dataRoomBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewHolder.bt_dataRoomEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.update("roomName", viewHolder.et_dataRoomsName.getText().toString());
                docRef.update("designation", viewHolder.et_dataDesignation.getText().toString());
                finish();
            }
        });

        viewHolder.bt_dataRoomDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.delete();
                finish();
            }
        });

    }

    private class ViewHolder{
        EditText et_dataRoomsName, et_dataDesignation;
        Button bt_dataRoomEdit, bt_dataRoomDelete, bt_dataRoomBack;
    }
}