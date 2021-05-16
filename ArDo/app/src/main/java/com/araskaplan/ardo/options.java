package com.araskaplan.ardo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class options extends AppCompatActivity {

    EditText examLength,difficulty,pointsPerQuestion;
    Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        examLength=findViewById(R.id.options_act_exam_length);
        difficulty=findViewById(R.id.options_act_difficulty);
        pointsPerQuestion=findViewById(R.id.options_act_ppq);
        saveButton=findViewById(R.id.options_act_savebutton);
        SharedPreferences sharedPreferences=this.getSharedPreferences(this.getPackageName(),Context.MODE_PRIVATE);
        examLength.setText(sharedPreferences.getString("def_exam_length","30"));
        difficulty.setText(sharedPreferences.getString("def_difficulty","5"));
        pointsPerQuestion.setText(sharedPreferences.getString("def_ppq","5"));
    }
    public void saveDefaultOptions(View view){
        SharedPreferences sharedPreferences=this.getSharedPreferences(this.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("def_exam_length",examLength.getText().toString());
        editor.putString("def_difficulty",difficulty.getText().toString());
        editor.putString("def_ppq",pointsPerQuestion.getText().toString());
        editor.commit();
        Toast.makeText(view.getContext(),"Options saved",Toast.LENGTH_LONG).show();
    }
}