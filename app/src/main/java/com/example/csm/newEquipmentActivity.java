package com.example.csm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class newEquipmentActivity extends AppCompatActivity {

    private ViewHolder viewHolder = new ViewHolder();
    private static final int PICK_REQUEST = 1;
    private StorageTask uploadTask;
    private Uri uri_image;
    private String name = "", brand = "", description = "", color = "", photo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_equipment);

        viewHolder.iv_newPhoto = findViewById(R.id.iv_newPhoto);
        viewHolder.et_newName = findViewById(R.id.et_newName);
        viewHolder.et_newBrand = findViewById(R.id.et_newBrand);
        viewHolder.et_newDescription = findViewById(R.id.et_newDescription);
        viewHolder.et_newColor = findViewById(R.id.et_newColor);
        viewHolder.bt_newAdd = findViewById(R.id.bt_newAdd);
        viewHolder.bt_newBack = findViewById(R.id.bt_newBack);


        viewHolder.bt_newAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = viewHolder.et_newName.getText().toString();
                brand = viewHolder.et_newBrand.getText().toString();
                description = viewHolder.et_newDescription.getText().toString();
                color = viewHolder.et_newColor.getText().toString();
                photo = "";

//  --------->>>>           // VERIFY IF THE INPUTS ARE EMPTY
                if (name.trim().isEmpty() || brand.trim().isEmpty() || description.trim().isEmpty() || color.trim().isEmpty()) {
                    Toast.makeText(newEquipmentActivity.this, "Preencher todos os campos", Toast.LENGTH_SHORT).show();

                } else {
                //////// END
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Equipments");
                    if (uri_image != null) {
                        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri_image));
                        fileRef.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(newEquipmentActivity.this, "Upload da imagem com sucesso", Toast.LENGTH_SHORT).show();
                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete()) ;
                                photo = uri.getResult().toString();


                                CollectionReference reference = FirebaseFirestore.getInstance().collection("Equipments");
                                reference.add(new Equipments(name, brand, description, color, photo));
                                Toast.makeText(newEquipmentActivity.this, "Novo equipamento adicionado", Toast.LENGTH_SHORT).show();
                                clearAll();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(newEquipmentActivity.this, "Erro no upload da imagem", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
//  --------->>>>            // IF NO IMG/PHOTO IS SELECTED/UPLOADED BY USER
                        photo = "";
                        CollectionReference reference = FirebaseFirestore.getInstance().collection("Equipments");
                        reference.add(new Equipments(name, brand, description, color, photo));
                        Toast.makeText(newEquipmentActivity.this, "Novo equipamento adicionado", Toast.LENGTH_SHORT).show();
                        clearAll();
                        finish();
                        /// END
                    }

                    /*else{
                        Toast.makeText(newEquipmentActivity.this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();

                    }*/

                    //
                }
            }
        });

        viewHolder.iv_newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, PICK_REQUEST);
            }
        });


        viewHolder.bt_newBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri_image = data.getData();
            Picasso.get().load(uri_image).into(viewHolder.iv_newPhoto);
        }
    }

    private String getFileExtension(Uri uri_image) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri_image));
    }


    private class ViewHolder{
        ImageView iv_newPhoto;
        EditText et_newName, et_newBrand, et_newDescription, et_newColor;
        Button bt_newBack, bt_newAdd;
    }
    private void clearAll() {
        viewHolder.et_newName.setText("");
        viewHolder.et_newBrand.setText("");
        viewHolder.et_newDescription.setText("");
        viewHolder.et_newColor.setText("");
    }

}