package com.araskaplan.ardo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class AfterLog extends AppCompatActivity {

    RadioGroup RG;
    EditText ansA,ansB,ansC,ansD,ansE;
    EditText textpart;
    Button addQ,allQ;
    ImageView imageView;
    Bitmap selectedimage;
    RadioButton radioButtonA,radioButtonB,radioButtonC,radioButtonD,radioButtonE;
    ArrayList<RadioButton>radios=new ArrayList<RadioButton>();

    int cur_user_id;
    String mode;
    int q_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_log);
        textpart=findViewById(R.id.textPart);
        ansA=findViewById(R.id.answerA);
        ansB=findViewById(R.id.answerB);
        ansC=findViewById(R.id.answerC);
        ansD=findViewById(R.id.answerD);
        ansE=findViewById(R.id.answerE);
        addQ=findViewById(R.id.Qadder);
        allQ=findViewById(R.id.Qall);
        RG=findViewById(R.id.RG);
        radioButtonA=findViewById(R.id.radioButton2);
        radioButtonB=findViewById(R.id.radioButton7);
        radioButtonC=findViewById(R.id.radioButton8);
        radioButtonD=findViewById(R.id.radioButton9);
        radioButtonE=findViewById(R.id.radioButton10);
        radios.add(radioButtonA);
        radios.add(radioButtonB);
        radios.add(radioButtonC);
        radios.add(radioButtonD);
        radios.add(radioButtonE);
        imageView=findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.imageselect);
        selectedimage=null;
        Intent intent=getIntent();
        cur_user_id=intent.getIntExtra("cur_user_id",0);
        mode=intent.getStringExtra("mode");
        if(mode.equals("update")){
            q_id=intent.getIntExtra("q_id",-1);
            addQ.setText("Update Question");
            addQ.setGravity(CENTER_HORIZONTAL);
            allQ.setGravity(CENTER_HORIZONTAL);
            fillBoxes();
        }
    }

    public void fillBoxes(){
        try {
            SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("MainDB",MODE_PRIVATE,null);
            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Question WHERE id=?",new String[]{Integer.toString(q_id)});

            int idIx=cursor.getColumnIndex("id");
            int rootIx=cursor.getColumnIndex("root");
            int ansaIx=cursor.getColumnIndex("ansa");
            int ansbIx=cursor.getColumnIndex("ansb");
            int anscIx=cursor.getColumnIndex("ansc");
            int ansdIx=cursor.getColumnIndex("ansd");
            int anseIx=cursor.getColumnIndex("anse");
            int imgIx=cursor.getColumnIndex("img");
            int q_cor_ansIx=cursor.getColumnIndex("correctans");
            while (cursor.moveToNext()){
                textpart.setText(cursor.getString(rootIx));
                ansA.setText(cursor.getString(ansaIx));
                ansB.setText(cursor.getString(ansbIx));
                ansC.setText(cursor.getString(anscIx));
                ansD.setText(cursor.getString(ansdIx));
                ansE.setText(cursor.getString(anseIx));
                radios.get(cursor.getInt(q_cor_ansIx)).toggle();

                byte[] bytearray=cursor.getBlob(imgIx);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
                imageView.setImageBitmap(bitmap);
                selectedimage=bitmap;
            }
            cursor.close();
            sqLiteDatabase.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void addQuestion(View view) {
        if(mode.equals("update")){
            try {
                SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("MainDB",MODE_PRIVATE,null);
                String sqlstring=("UPDATE Question SET root=?,ansa=?,ansb=?,ansc=?,ansd=?,anse=?,img=?,correctans=? WHERE id=?");
                SQLiteStatement sqLiteStatement=sqLiteDatabase.compileStatement(sqlstring);
                sqLiteStatement.bindString(1,textpart.getText().toString());
                sqLiteStatement.bindString(2,ansA.getText().toString());
                sqLiteStatement.bindString(3,ansB.getText().toString());
                sqLiteStatement.bindString(4,ansC.getText().toString());
                sqLiteStatement.bindString(5,ansD.getText().toString());
                sqLiteStatement.bindString(6,ansE.getText().toString());

                if (selectedimage==null){
                    imageView.setImageResource(R.drawable.noimageselected);
                    selectedimage=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                selectedimage=makeSmallerImage(selectedimage,400);
                selectedimage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                byte[] bytearray = outputStream.toByteArray();
                sqLiteStatement.bindBlob(7, bytearray);

                sqLiteStatement.bindLong(8,getSelectedAnswer());
                sqLiteStatement.bindLong(9,q_id);
                sqLiteStatement.execute();
                sqLiteDatabase.close();
                Toast.makeText(getApplicationContext(),"Question successfully updated",Toast.LENGTH_LONG).show();
                toAllQuestions();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            try {
                SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("MainDB", MODE_PRIVATE, null);
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Question(id INTEGER PRIMARY KEY,root VARCHAR,ansa VARCHAR,ansb VARCHAR,ansc VARCHAR,ansd VARCHAR,anse VARCHAR,img BLOB,correctans INTEGER,authorid INTEGER)");
                String sqlString = ("INSERT INTO Question(root,ansa,ansb,ansc,ansd,anse,img,correctans,authorid) VALUES(?,?,?,?,?,?,?,?,?)");
                SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sqlString);
                sqLiteStatement.bindString(1, textpart.getText().toString());
                sqLiteStatement.bindString(2, ansA.getText().toString());
                sqLiteStatement.bindString(3, ansB.getText().toString());
                sqLiteStatement.bindString(4, ansC.getText().toString());
                sqLiteStatement.bindString(5, ansD.getText().toString());
                sqLiteStatement.bindString(6, ansE.getText().toString());


                if (selectedimage==null){
                    imageView.setImageResource(R.drawable.noimageselected);
                    selectedimage=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                selectedimage=makeSmallerImage(selectedimage,200);
                selectedimage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                byte[] bytearray = outputStream.toByteArray();
                sqLiteStatement.bindBlob(7, bytearray);

                sqLiteStatement.bindLong(8, getSelectedAnswer());
                sqLiteStatement.bindLong(9, cur_user_id);
                sqLiteStatement.execute();
                Toast.makeText(getApplicationContext(), "Question added successfully.", Toast.LENGTH_LONG).show();
                sqLiteStatement.close();
                toAllQuestions();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void toAllQuestions(){
        Intent intent=new Intent(getApplicationContext(),allQuestions_v3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("cur_user_id",cur_user_id);
        startActivity(intent);
    }
    public Bitmap makeSmallerImage(Bitmap image,int maxSize){
        int width=image.getWidth();
        int height=image.getHeight();
        float bitmapRatio=(float) width/(float)height;
        if(bitmapRatio>1){
            width=maxSize;
            height=(int)(width/bitmapRatio);
        }else{
            height=maxSize;
            width=(int)(height*bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }


    public void allQuestions(View view){
        toAllQuestions();

    }
    public int getSelectedAnswer(){
        int selectedOpt=RG.getCheckedRadioButtonId();
        RadioButton temp=findViewById(selectedOpt);
        int x=0;
        for(RadioButton i:radios){
            if(i.equals(temp)){
                return x;
            }
            x++;
        }
        return 0;
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
                    imageView.setImageBitmap(selectedimage);

                }
                else{
                    selectedimage=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    imageView.setImageBitmap(selectedimage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}