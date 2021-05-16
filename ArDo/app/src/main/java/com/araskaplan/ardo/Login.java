package com.araskaplan.ardo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Login extends AppCompatActivity {

    EditText email2,pass3;
    Button button3;
    TextView  num, failsafe;
    int tries=3;
    String mail,pass;
    int broken=0;
    String tempname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email2=findViewById(R.id.email2);
        num=findViewById(R.id.num);
        pass3=findViewById(R.id.pass3);
        button3=findViewById(R.id.button3);
        num.setText(Integer.toString(tries));
    }

    @Override
    protected void onResume() {
        tries=3;
        num.setText("3");
        super.onResume();
    }

    public void saynin(View view){
        mail=email2.getText().toString();
        pass=pass3.getText().toString();
        try {
            SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("MainDB",MODE_PRIVATE,null);
            Cursor cursor=sqLiteDatabase.rawQuery("SELECT id FROM Users WHERE mail=? and password=?",new String[]{email2.getText().toString(),MD5(pass3.getText().toString())});
            int idIx=cursor.getColumnIndex("id");
            int instances=0;
            int cur_user_id = 0;
            while (cursor.moveToNext()){
                instances++;
                cur_user_id=cursor.getInt(idIx);
            }
            if(instances==1){
                Toast.makeText(getApplicationContext(),"Welcome Back",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(),allQuestions_v3.class);
                intent.putExtra("cur_user_id",cur_user_id);
                cursor.close();
                sqLiteDatabase.close();
                startActivity(intent);
            }

            else{
                cursor.close();
                sqLiteDatabase.close();
                tries--;
                num.setText(Integer.toString(tries));
                Toast.makeText(getApplicationContext(),"No user found with this combination",Toast.LENGTH_LONG).show();
            }

            if(tries==0){
                cursor.close();
                sqLiteDatabase.close();
                Intent intent=new Intent(Login.this,signUp.class);
                startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
            tries--;
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


}