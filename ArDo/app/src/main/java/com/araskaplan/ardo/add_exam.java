package com.araskaplan.ardo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class add_exam extends AppCompatActivity {
    ArrayList<Exam> exams;
    Button add_exam;
    EditText name,time,ppq,difficulty;
    int cur_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        exams=MainActivity.getExams();
        name=findViewById(R.id.add_exam_name);
        time=findViewById(R.id.add_exam_time);
        ppq=findViewById(R.id.add_exam_ppq);
        difficulty=findViewById(R.id.add_exam_difficulty);
        add_exam=findViewById(R.id.add_exam_create);
        SharedPreferences sharedPreferences=this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        time.setText(sharedPreferences.getString("def_exam_length","30"));
        difficulty.setText(sharedPreferences.getString("def_difficulty","5"));
        ppq.setText(sharedPreferences.getString("def_ppq","5"));
        Intent intent=getIntent();
        cur_user_id=intent.getIntExtra("cur_user_id",0);
    }
    public void create_exam(View view){
        Exam temp=new Exam(name.getText().toString(),Integer.parseInt(difficulty.getText().toString()),Integer.parseInt(time.getText().toString()),Integer.parseInt(ppq.getText().toString()));
        exams.add(temp);
        Toast.makeText(view.getContext(),"Exam Creation Successful",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getApplicationContext(),allQuestions_v3.class);
        intent.putExtra("cur_user_id",cur_user_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}