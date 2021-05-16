package com.araskaplan.ardo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;



public class signUp extends AppCompatActivity {

    EditText nametext,surnametext,phonetext,mailtext,passwordtext,repasswordtext,datetext;
    Button button4;
    ImageView img;
    Bitmap selectedimage;
    int broken=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nametext=findViewById(R.id.nametext);
        surnametext=findViewById(R.id.surnametext);
        phonetext=findViewById(R.id.phonetext);
        mailtext=findViewById(R.id.mailtext);
        passwordtext=findViewById(R.id.passwordtext);
        repasswordtext=findViewById(R.id.repasswordtext);
        button4=findViewById(R.id.button4);
        datetext=findViewById(R.id.datetext);
        img=findViewById(R.id.singup_act_img);
        img.setImageResource(R.drawable.imageselect);
        selectedimage=null;
    }

    public void saynap(View view){
        if(passwordtext.getText().toString().equals(repasswordtext.getText().toString()) && mailtext.getText().toString().equals("")==false && passwordtext.getText().toString().equals("")==false ){
            try {
                SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("MainDB",MODE_PRIVATE,null);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Users(id INTEGER PRIMARY KEY,name VARCHAR,surname VARCHAR,phonenumber VARCHAR,mail VARCHAR,password VARCHAR,bdate VARCHAR,userimage BLOB)");

                Cursor cursor=sqLiteDatabase.rawQuery("SELECT mail FROM Users WHERE mail=?",new String[]{mailtext.getText().toString()});
                int instances=0;
                while(cursor.moveToNext()){
                    instances++;
                }
                cursor.close();
                if(instances==0){
                    String sqlString=("INSERT INTO Users(name,surname,phonenumber,mail,password,bdate,userimage) VALUES (?,?,?,?,?,?,?)");
                    SQLiteStatement sqLiteStatement=sqLiteDatabase.compileStatement(sqlString);
                    sqLiteStatement.bindString(1,nametext.getText().toString());
                    sqLiteStatement.bindString(2,surnametext.getText().toString());
                    sqLiteStatement.bindString(3,phonetext.getText().toString());
                    sqLiteStatement.bindString(4,mailtext.getText().toString());
                    sqLiteStatement.bindString(5,MD5(passwordtext.getText().toString()));
                    sqLiteStatement.bindString(6,datetext.getText().toString());

                    if (selectedimage==null){
                        img.setImageResource(R.drawable.noimageselected);
                        selectedimage=((BitmapDrawable)img.getDrawable()).getBitmap();
                    }
                    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                    selectedimage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                    byte[] bytearray=outputStream.toByteArray();
                    sqLiteStatement.bindBlob(7,bytearray);

                    sqLiteStatement.execute();
                    Toast.makeText(getApplicationContext(),"User signed up successfully.",Toast.LENGTH_LONG).show();

                    sqLiteStatement.close();
                    Intent intent=new Intent(getApplicationContext(),Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),"A user with the same email already exists.",Toast.LENGTH_LONG).show();
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            if(mailtext.getText().toString().equals("")==false || passwordtext.getText().toString().equals("")==false){
                Toast.makeText(getApplicationContext(),"User must have a password and mail address.",Toast.LENGTH_LONG).show();
            }

            else {
                Toast.makeText(getApplicationContext(),"Passwords don't match.",Toast.LENGTH_LONG).show();
            }

        }

    }

    public String MD5(String raw){

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(raw.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public void getImageFromGallery(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            Uri imageData=data.getData();
            try {
                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedimage=ImageDecoder.decodeBitmap(source);
                    img.setImageBitmap(selectedimage);

                }
                else{
                    selectedimage=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    img.setImageBitmap(selectedimage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}