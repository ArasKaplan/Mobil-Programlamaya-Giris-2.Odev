package com.araskaplan.ardo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ExamRecyclerAdapter extends RecyclerView.Adapter<ExamRecyclerAdapter.PostHolder> {
    int cur_user_id;
    ArrayList<Exam>exams;
    public ExamRecyclerAdapter(ArrayList<Exam> exams,int cur_user_id){
        this.cur_user_id=cur_user_id;
        this.exams=exams;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recyclerview_2,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.pos=position;
        holder.num.setText("Number of Questions:"+exams.get(position).getQuestions().size());
        holder.length.setText("Exam Length:"+Integer.toString(exams.get(position).time));
        holder.difficulty.setText("Exam Difficulty:"+Integer.toString(exams.get(position).difficulty));
        holder.ppq.setText("Points Per Question:"+Integer.toString(exams.get(position).ppq));
        holder.toptextview.setText("Exam #"+Integer.toString(position+1)+" "+exams.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
        TextView num,length,ppq,difficulty,toptextview;
        Button export,delete;
        int pos;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            num=itemView.findViewById(R.id.cyclerview2_nametext);
            length=itemView.findViewById(R.id.cyclerview2_examlength);
            ppq=itemView.findViewById(R.id.cyclerview2_ppq);
            difficulty=itemView.findViewById(R.id.cyclerview2_difficulty);
            export=itemView.findViewById(R.id.cyclerview2_exportbutton);
            delete=itemView.findViewById(R.id.cyclerview2_deletebutton);
            toptextview=itemView.findViewById(R.id.textView17);
            export.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text="";
                    String cor_ans="";
                    text+="Exam Name:"+exams.get(pos).name+" \nExam Length:"+exams.get(pos).getTime()+"\n" +
                            "Points Per Question:"+exams.get(pos).getPpq()+"\nExam Difficulty:"+exams.get(pos).getDifficulty()+"\n";
                    int i=1;
                    for (Question question:exams.get(pos).getQuestions()){
                        text+="\nQuestion#"+i;
                        ArrayList<String> temp=shuffler(question);
                        text+="\n\nQuestion Root:"+question.getText()+"\n";
                        for(String string:temp){
                            text+=string+"\n";
                        }
                        i++;
                        text+="Correct Answer:"+question.getAnswers().get(question.getCorrectanswer());
                        text+="\n";
                    }
                    System.out.println(text);
                    FileOutputStream fileOutputStream=null;
                    try {
                        fileOutputStream=v.getContext().openFileOutput(exams.get(pos).getName()+".txt", Context.MODE_PRIVATE);
                        fileOutputStream.write(text.getBytes());
                        Toast.makeText(v.getContext(),"Saved to:"+v.getContext().getFilesDir()+"/"+exams.get(pos).getName()+".txt",Toast.LENGTH_LONG).show();
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File file=new File(v.getContext().getFilesDir(),exams.get(pos).getName()+".txt");
                    Uri content=FileProvider.getUriForFile(v.getContext().getApplicationContext(),v.getContext().getApplicationContext().getPackageName(),file);
                    v.getContext().getApplicationContext().grantUriPermission(v.getContext().getApplicationContext().getPackageName(),content,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_STREAM,content);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    v.getContext().startActivity(Intent.createChooser(intent,"Share Exam"));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Delete Exam");
                    builder.setMessage("Are you sure you want to delete this exam?");
                    builder.setNegativeButton("No",null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exams.remove(pos);
                            notifyDataSetChanged();
                            Intent intent=new Intent(v.getContext(),allQuestions_v3.class);
                            intent.putExtra("cur_user_id",cur_user_id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            v.getContext().startActivity(intent);
                        }
                    });
                    builder.show();
                }
            });
        }


        public ArrayList<String> shuffler(Question question){
            ArrayList<String> answers=new ArrayList<String>();
            ArrayList<Integer>already=new ArrayList<Integer>();
            answers.add(question.getAnswers().get(question.getCorrectanswer()));
            already.add(question.getCorrectanswer());
            int i,temprand=question.getCorrectanswer();
            for (i=0;i<exams.get(pos).getDifficulty()-1;i++){
                while (checkContainer(already,temprand)==true){
                    temprand=randomizer(exams.get(pos).getDifficulty());
                }
                answers.add(question.getAnswers().get(temprand));
                already.add(temprand);
            }
            for (i=0;i<exams.get(pos).getDifficulty();i++){
                String temp;
                temp=answers.remove(i);
                answers.add(randomizer(exams.get(pos).getDifficulty()),temp);
            }
            return answers;
        }

        public boolean checkContainer(ArrayList<Integer> already,int temp){
            for (Integer integer:already){
                if(integer.intValue()==temp){
                    return true;
                }
            }
            return false;
        }


        public int randomizer(int bound){
            Random random=new Random();
            int int_random= random.nextInt(bound);
            return int_random;
        }
    }
}
