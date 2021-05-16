package com.araskaplan.ardo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedRecyclerAdapterv2 extends RecyclerView.Adapter<FeedRecyclerAdapterv2.PostHolder> {
    ArrayList<Question> questions;
    ArrayList<Exam> exams;

    public FeedRecyclerAdapterv2(ArrayList<Question> questions,ArrayList<Exam> exams) {
        this.questions = questions;
        this.exams=exams;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.allquestionsadapterv3,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.corans_from_db=questions.get(position).getCorrectanswer();
        holder.pos=position;
        holder.cur_user_id=questions.get(position).getAuthorId();
        holder.q_id=questions.get(position).getQuestion_id();
        holder.imageView.setImageBitmap(questions.get(position).getQuestionImage());
        holder.questionRoot.setText(questions.get(position).getText());
        holder.ansA.setText("A)"+questions.get(position).getAnswers().get(0));
        holder.ansB.setText("B)"+questions.get(position).getAnswers().get(1));
        holder.ansC.setText("C)"+questions.get(position).getAnswers().get(2));
        holder.ansD.setText("D)"+questions.get(position).getAnswers().get(3));
        holder.ansE.setText("E)"+questions.get(position).getAnswers().get(4));
        holder.correctAns.setText("***"/*+questions.get(position).getCorrectanswer()*/);
        holder.questionNumber.setText("Question #"+(position+1));
    }


    @Override
    public int getItemCount() {
        return questions.size();
    }


    class PostHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView ansA,ansB,ansC,ansD,ansE,correctAns,questionRoot,questionNumber;
        Button showAns,deleteQuestion,editQuestion,add_to_exam;
        Spinner spinner;
        int corans_from_db;
        int pos;
        int q_id;
        int cur_user_id;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            questionRoot=itemView.findViewById(R.id.qViewQuestionRoot);
            imageView=itemView.findViewById(R.id.qViewImg);
            ansA=itemView.findViewById(R.id.qViewAnsA);
            ansB=itemView.findViewById(R.id.qViewAnsB);
            ansC=itemView.findViewById(R.id.qViewAnsC);
            ansD=itemView.findViewById(R.id.qViewAnsD);
            ansE=itemView.findViewById(R.id.qViewAnsE);
            correctAns=itemView.findViewById(R.id.qViewAnswer);
            questionNumber=itemView.findViewById(R.id.questionNumber);
            showAns=itemView.findViewById(R.id.qViewShowAnswer);
            spinner=itemView.findViewById(R.id.spinner2);
            fillSpinner();
            showAns.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    if(correctAns.getText().equals("***"))
                    {
                        if(corans_from_db==0) {
                            correctAns.setText("A");
                            ansA.setBackgroundColor(Color.RED);
                        }
                        else if (corans_from_db==1){
                            correctAns.setText("B");
                            ansB.setBackgroundColor(Color.RED);
                        }
                        else if (corans_from_db==2){
                            correctAns.setText("C");
                            ansC.setBackgroundColor(Color.RED);
                        }
                        else if (corans_from_db==3){
                            correctAns.setText("D");
                            ansD.setBackgroundColor(Color.RED);
                        }
                        else{
                            correctAns.setText("E");
                            ansE.setBackgroundColor(Color.RED);
                        }
                    }else{
                        ansA.setBackgroundColor(Color.WHITE);
                        ansB.setBackgroundColor(Color.WHITE);
                        ansC.setBackgroundColor(Color.WHITE);
                        ansD.setBackgroundColor(Color.WHITE);
                        ansE.setBackgroundColor(Color.WHITE);
                        correctAns.setText("***");
                    }

                }
            });
            deleteQuestion=itemView.findViewById(R.id.qViewDelete);
            deleteQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Delete Request");
                    builder.setMessage("Are you sure you want to delete this question?");
                    builder.setNegativeButton("No",null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                SQLiteDatabase sqLiteDatabase=v.getContext().openOrCreateDatabase("MainDB", Context.MODE_PRIVATE,null);
                                /*sqLiteDatabase.rawQuery("DELETE FROM Question WHERE id=?",new String[] {Integer.toString(q_id)});
                                sqLiteDatabase.close();*/
                                String sqlstring=("DELETE FROM Question where id=?");
                                SQLiteStatement sqLiteStatement=sqLiteDatabase.compileStatement(sqlstring);
                                sqLiteStatement.bindLong(1,q_id);
                                sqLiteStatement.execute();
                                questions.remove(pos);
                                notifyDataSetChanged();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.show();
                }
            });

            editQuestion=itemView.findViewById(R.id.qViewEdit);
            editQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(),AfterLog.class);
                    intent.putExtra("cur_user_id",cur_user_id);
                    intent.putExtra("mode","update");
                    intent.putExtra("q_id",q_id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    v.getContext().startActivity(intent);
                }
            });
            add_to_exam=itemView.findViewById(R.id.recycler_row_add_to_exam);
            add_to_exam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.getExams().size()!=0){
                        exams.get(spinner.getSelectedItemPosition()).questions.add(questions.get(pos));
                        Toast.makeText(v.getContext(),"Question is added to"+exams.get(spinner.getSelectedItemPosition()).getName(),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(v.getContext(),"Please create an exam first",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        public void fillSpinner(){
            ArrayList<String> temp=new ArrayList<>();
            for(Exam exam:exams){
                temp.add(exam.name);
                System.out.println(exam.name);
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(itemView.getContext(), android.R.layout.simple_spinner_item,temp);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }



}
