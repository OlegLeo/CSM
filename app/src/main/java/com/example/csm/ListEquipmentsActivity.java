package com.example.csm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListEquipmentsActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference equipRef = db.collection("Equipments");
    private EquipmentsAdapter adapter;
    private FloatingActionButton bt_equipmentAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_equipments);
        bt_equipmentAdd = findViewById(R.id.bt_equipmentAdd);
        setUpRecyclerView();

        adapter.setOnItemClickListener(new EquipmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                Equipments equipments = documentSnapshot.toObject(Equipments.class);
                String path = documentSnapshot.getReference().getPath();

                Intent i = new Intent(ListEquipmentsActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });

        bt_equipmentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListEquipmentsActivity.this, newEquipmentActivity.class);
                startActivity(i);
            }
        });
    }

    private void setUpRecyclerView() {

        Query query = equipRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                .setQuery(query, Equipments.class).build();

        adapter = new EquipmentsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    ////// Search button/////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

        private void txtSearch(String str){
            Query query = equipRef.orderBy("name", Query.Direction.ASCENDING).startAt(str).endAt(str+"~");
            FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                    .setQuery(query, Equipments.class).build();

            adapter = new EquipmentsAdapter(options);
            adapter.startListening();

            RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
            recyclerView.setAdapter(adapter);

//  --------->>>>             //////////////////////////////// ADICIONAR ISTO PARA DAR ///////////////////////////////////////
            adapter.setOnItemClickListener(new EquipmentsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                    Equipments equipments = documentSnapshot.toObject(Equipments.class);
                    String path = documentSnapshot.getReference().getPath();

                    Intent i = new Intent(ListEquipmentsActivity.this, EquipmentsDataActivity.class);
                    i.putExtra("path", path);
                    startActivity(i);
                }
            });
//  --------->>>>             //////////////////////////////////// FIM ///////////////////////////////////
        }
    /////
}