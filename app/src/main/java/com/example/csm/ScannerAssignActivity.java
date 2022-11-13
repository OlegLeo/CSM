package com.example.csm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;

public class ScannerAssignActivity extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_CODE = 100;

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView scannedText;
    private BarcodeDetector barcodeDetector;
    private Button bt_assign_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_assign);

        surfaceView = findViewById(R.id.sv_assign_camera);
        scannedText = findViewById(R.id.tv_assign_text_scanned);
        bt_assign_scan = findViewById(R.id.bt_assign_scan);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String docId = extras.getString("docId");


        ////////////////// BOTAO PARA ASSIGNAR O EQUIPAMENTO A OUTRO EQUIPAMENTO //////////////////
        bt_assign_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference equipRef = db.collection("Equipments").document(docId);

                String assign = scannedText.getText().toString();

                DocumentReference assignRef = db.collection("Equipments").document(assign);
                assignRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Equipments room = documentSnapshot.toObject(Equipments.class);
                        String roomId = room.getRoomId();
                        equipRef.update("roomId",roomId);
                    }
                });

                equipRef.update("assign", assign);

                Intent i = new Intent(ScannerAssignActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", docId);
                startActivity(i);
            }
        });
        ////////////////// END  BOTAO PARA ASSIGNAR O EQUIPAMENTO A OUTRO EQUIPAMENTO //////////////////

        //INICIA O DETECTOR DE QR E A CAMARA
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector).setRequestedPreviewSize(640, 480).build();

        ////////////////////////////ASSOCIA A CAMARA AO SURFACE VIEW, CASO TENHA AUTORIZAÇÃO/////////////////////
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        ///////////////////////   END   ////////////////////////////////////

        ////////////////// DETETOR DO QR ////////////////////////////
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if (qrcode.size() != 0) {
                    scannedText.post(new Runnable() {
                        @Override
                        public void run() {
                            scannedText.setText(qrcode.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
        ////////////////// END DETETOR DO QR ////////////////////////////
    }

    ///////////////////////////////// VERIFICA PERMISSÃO DA APP À CAMARA ///////////////////////////
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ScannerAssignActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ScannerAssignActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ///////////////////////////////// END VERIFICA PERMISSÃO DA APP À CAMARA ///////////////////////////

    @Override
    protected void onStart() {
        super.onStart();

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }
}