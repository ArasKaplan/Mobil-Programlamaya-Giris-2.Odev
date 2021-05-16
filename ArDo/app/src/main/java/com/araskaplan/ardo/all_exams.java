package com.araskaplan.ardo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

public class all_exams extends AppCompatActivity {

    ArrayList<Exam>exams;
    int cur_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_exams);
        exams=MainActivity.getExams();
        Intent intent=getIntent();
        cur_user_id=intent.getIntExtra("cur_user_id",0);
        ExamRecyclerAdapter recyclerAdapter=new ExamRecyclerAdapter(exams,cur_user_id);
        RecyclerView recyclerView=findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

    }


}