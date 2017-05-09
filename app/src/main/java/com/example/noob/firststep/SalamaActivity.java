package com.example.noob.firststep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SalamaActivity extends AppCompatActivity {
    private Uri uri1;
    private Button retriveImages;
    private EditText user;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;

//private DatabaseReference firebaseDatabase;

private EditText chompersNo;
private EditText hint;
private EditText area;

    private Button submit;
    private StorageReference storageReference;
    private static final int GALARY_INTENT=2;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salama);
        storageReference= FirebaseStorage.getInstance().getReference();
        retriveImages=(Button) findViewById(R.id.upload);

        user =(EditText) findViewById(R.id.user);
        retriveImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetriveData(user.getText().toString(), imageView1, 1);
                onRetriveData(user.getText().toString(), imageView2, 2);
                onRetriveData(user.getText().toString(), imageView3, 3);
            }
        });
        imageView1 = (ImageView) findViewById(R.id.img1);
        imageView2 = (ImageView) findViewById(R.id.img2);
        imageView3 = (ImageView) findViewById(R.id.img3);

        chompersNo=(EditText)findViewById(R.id.chompersNo);
        hint=(EditText)findViewById(R.id.hint);
        area=(EditText)findViewById(R.id.area);
submit=(Button) findViewById(R.id.submit);


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALARY_INTENT);
i=1;
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALARY_INTENT);
i=2;
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALARY_INTENT);
i=3;
            }
        });


    }
    //retrive users flat data
    private void onRetriveData(String userName, final ImageView img,int x){
        StorageReference pathReference = storageReference.child("flats").child(userName+x);


        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide
                        .with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .crossFade()
                        .into(img);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();            }
        });
submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        LayoutInflater inflater = ((Activity) getApplicationContext()).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.activity_salama,
                null);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams wlmp = dialog.getWindow()
                .getAttributes();
        wlmp.gravity = Gravity.BOTTOM;


//        Button btnCamera = (Button) dialogLayout.findViewById(R.id.button_Camera);
//        Button btnGallery = (Button) dialogLayout.findViewById(R.id.button_Gallery);
//        Button btnDismiss = (Button) dialogLayout.findViewById(R.id.btnCancelCamera);


        builder.setView(dialogLayout);

        dialog.show();
    }
});
//submit.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        firebaseDatabase=FirebaseDatabase.getInstance().getReference();
//firebaseDatabase.child("area").child(area.getText().toString());
//firebaseDatabase.child("chompersNo").child(chompersNo.getText().toString());
//firebaseDatabase.child("hint discribtion").child(hint.getText().toString());
//    }
//});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== GALARY_INTENT && resultCode==RESULT_OK ){
            //after selecting flat image send it to storage database
            final Uri uri =data.getData();
            StorageReference filepath =storageReference.child("flats").child(user.getText().toString()+i);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                    //to show images on image view we set after selecting image to upload
switch (i){
    case 1 :
        imageView1.setImageURI(uri);

break;
    case 2:
        imageView2.setImageURI(uri);
        break;
    case 3:
        imageView3.setImageURI(uri);
break;
}


                }
            });

        }
    }
}
