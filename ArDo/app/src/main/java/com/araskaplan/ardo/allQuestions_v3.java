package com.araskaplan.ardo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class allQuestions_v3 extends AppCompatActivity {
    static ArrayList<Question> questions;
    int cur_user_id;
    ArrayList<Exam> exams;
    int any_exams;

    public static ArrayList<Question> getQuestions() {
        return questions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_questions_v3);
        questions=new ArrayList<>();
        Intent intent=getIntent();
        cur_user_id=intent.getIntExtra("cur_user_id",0);
        exams=MainActivity.getExams();
        getQuestionsFromSQL();
        FeedRecyclerAdapterv2 recyclerAdapter=new FeedRecyclerAdapterv2(questions,exams);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        any_exams=intent.getIntExtra("any_exams",0);
        if(any_exams==1){
            exams=MainActivity.getExams();
        }
    }

    public void getQuestionsFromSQL(){
        try {
            SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("MainDB",MODE_PRIVATE,null);
            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Question WHERE authorid=?",new String[]{Integer.toString(cur_user_id)});
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
                String temp_q_root="";
                ArrayList<String> temp_q_answers=new ArrayList<>();
                temp_q_root=cursor.getString(rootIx);
                temp_q_answers.add(cursor.getString(ansaIx));
                temp_q_answers.add(cursor.getString(ansbIx));
                temp_q_answers.add(cursor.getString(anscIx));
                temp_q_answers.add(cursor.getString(ansdIx));
                temp_q_answers.add(cursor.getString(anseIx));
                int q_cor_ans=cursor.getInt(q_cor_ansIx);
                byte[] bytearray=cursor.getBlob(imgIx);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytearray,0,bytearray.length);
                Question temp_q=new Question(temp_q_root,temp_q_answers,q_cor_ans,cur_user_id,cursor.getInt(idIx));
                temp_q.setQuestionImage(bitmap);
                questions.add(temp_q);
            }
            cursor.close();
            sqLiteDatabase.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.addquestionmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_question_item){
            Intent intent=new Intent(allQuestions_v3.this,AfterLog.class);
            intent.putExtra("cur_user_id",cur_user_id);
            intent.putExtra("mode","add_question");
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.options){
            Intent intent=new Intent(allQuestions_v3.this,options.class);
            intent.putExtra("cur_user_id",cur_user_id);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.all_exams){
            Intent intent=new Intent(allQuestions_v3.this,all_exams.class);
            intent.putExtra("cur_user_id",cur_user_id);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.add_exam){
            Intent intent=new Intent(allQuestions_v3.this,add_exam.class);
            intent.putExtra("cur_user_id",cur_user_id);
            startActivity(intent);
        }
        /*else if(item.getItemId()==R.id.how_to_ardo){
            Intent intent=new Intent(allQuestions_v3.this,how_to_ardo.class);
            intent.putExtra("cur_user_id",cur_user_id);
            startActivity(intent);
        }*/
        return super.onOptionsItemSelected(item);
    }
}