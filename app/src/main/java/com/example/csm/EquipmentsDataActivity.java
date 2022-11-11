package com.example.csm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class EquipmentsDataActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ViewHolder viewHolder = new ViewHolder();
    private static final int PICK_REQUEST = 1;
    private StorageTask uploadTask;
    private Uri uri_image;
    Intent i;
    String  photo="";
    String id = "";
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipments_data);
        viewHolder.iv_dataPhoto = findViewById(R.id.iv_dataPhoto);

        viewHolder.et_dataName = findViewById(R.id.et_dataName);
        viewHolder.et_dataBrand = findViewById(R.id.et_dataBrand);
        viewHolder.et_dataDescription = findViewById(R.id.et_dataDescription);
        viewHolder.et_dataColor = findViewById(R.id.et_dataColor);

        viewHolder.bt_dataBack = findViewById(R.id.bt_dataBack);
        viewHolder.bt_dataEdit = findViewById(R.id.bt_dataEdit);
        viewHolder.bt_dataDelete = findViewById(R.id.bt_dataDelete);

        i = getIntent();

        String path = i.getExtras().getString("path");
        id = path.substring(path.indexOf("/") + 1);

        docRef = db.collection("Equipments").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        //Toast.makeText(EquipmentsDataActivity.this, "Dados lidos com sucesso", Toast.LENGTH_SHORT).show();
                        viewHolder.et_dataName.setText(documentSnapshot.getData().get("name").toString());
                        viewHolder.et_dataBrand.setText(documentSnapshot.getData().get("brand").toString());
                        viewHolder.et_dataDescription.setText(documentSnapshot.getData().get("description").toString());
                        viewHolder.et_dataColor.setText(documentSnapshot.getData().get("color").toString());

                        try {
                            Picasso.get().load(documentSnapshot.getData().get("photo").toString()).into(viewHolder.iv_dataPhoto);
                        } catch (Exception e) {

                        }
                    }
                    else{
                        Toast.makeText(EquipmentsDataActivity.this, "Erro ao apresentar dados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(EquipmentsDataActivity.this, "Erro ao apresentar dados", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        viewHolder.iv_dataPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, PICK_REQUEST);
            }
        });

        viewHolder.bt_dataBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewHolder.bt_dataEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
                finish();
            }
        });

        viewHolder.bt_dataDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.et_dataName.getContext());
                builder.setTitle("Tem certeza?");

                builder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    docRef.delete();
                    finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();


            }

        });

    }

    private void uploadPhoto() {

        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(EquipmentsDataActivity.this, "Imagem em upload, aguarde", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Equipments");
            if (uri_image != null) {
                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri_image));
                fileRef.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EquipmentsDataActivity.this, "Upload da imagem com sucesso", Toast.LENGTH_SHORT).show();
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        photo = uri.getResult().toString();
                        updateEquip();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EquipmentsDataActivity.this, "Erro no upload da imagem", Toast.LENGTH_SHORT).show();

                    }
                });
            }
//  --------->>>>
            /*else {
                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri_image));
                fileRef.putFile(uri_image);

                updateEquip();

                Toast.makeText(EquipmentsDataActivity.this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();
            }*/
//  --------->>>>
        }


    }





    private void updateEquip() {
        docRef.update("name", viewHolder.et_dataName.getText().toString());
        docRef.update("brand", viewHolder.et_dataBrand.getText().toString());
        docRef.update("description", viewHolder.et_dataDescription.getText().toString());
        docRef.update("color", viewHolder.et_dataColor.getText().toString());
        docRef.update("photo", photo);

    }


    private String getFileExtension(Uri uri_image) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri_image));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri_image = data.getData();
            Picasso.get().load(uri_image).into(viewHolder.iv_dataPhoto);
        }
    }

    private class ViewHolder{
        ImageView iv_dataPhoto;
        EditText et_dataName, et_dataBrand, et_dataDescription, et_dataColor;
        Button bt_dataBack, bt_dataEdit, bt_dataDelete, bt_dataCancel, bt_dataOk;
    }



}