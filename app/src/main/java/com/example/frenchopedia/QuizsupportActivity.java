package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frenchopedia.Adapter.PracticesupportAdapter;
import com.example.frenchopedia.Model.Material;
import com.example.frenchopedia.Model.Question1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QuizsupportActivity extends AppCompatActivity {

    String value;
    PracticesupportAdapter practiceAdapter;
    DatabaseReference d;
    //ArrayList<Material> materials = new ArrayList<>();
    RecyclerView recyclerView;
    TextView txt_q;
    Button btn_next;
    RadioGroup selectAns;
    RadioButton a_1,a_2,a_3,a_4,a;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser curUser;
    int total=0;
    int correct=0;
    int wrong=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizsupport);
        txt_q=findViewById(R.id.txt_q1);
        selectAns=findViewById(R.id.selectAns1);
        a_1=findViewById(R.id.a_1);
        a_2=findViewById(R.id.a_2);
        a_3=findViewById(R.id.a_3);
        a_4=findViewById(R.id.a_4);
        btn_next=findViewById(R.id.btn_sub1);
        Intent intent = getIntent();
        value = intent.getStringExtra("Title");
        select(value);
    }

    private void select(String value) {
        switch (value) {
            case "Saison":
                Log.d("MainFragment", "idmon=" + d);
                loadQuiz();
                break;
        }
    }

    private void loadQuiz() {
        total++;
        if(total >5){
            Intent intent=new Intent(QuizsupportActivity.this,ResultActivity.class);
            intent.putExtra("correct",String.valueOf(correct));
            intent.putExtra("wrong",String.valueOf(wrong));
            startActivity(intent);

        }else{
            Log.d("Quiz","called="+d);
            d = FirebaseDatabase.getInstance().getReference().child("Quiz3 Saisons").child(String.valueOf(total));
            d.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Question1 question1=dataSnapshot.getValue(Question1.class);
                    txt_q.setText(question1.getQuestion());
                    a_1.setText(question1.option1);
                    a_2.setText(question1.option2);
                    a_3.setText(question1.option3);
                    a_4.setText(question1.option4);
                    Toast.makeText(getApplicationContext(),"Ans="+question1.getAnswer(),Toast.LENGTH_LONG).show();
                    btn_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedId = selectAns.getCheckedRadioButtonId();
                            a=findViewById(selectedId);
                            if(a.getText().toString().equals(question1.answer)){
                                a.setTextColor(Color.GREEN);
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        a.setTextColor(Color.parseColor("#808080"));
                                        correct++;
                                        loadQuiz();
                                    }
                                },1500);
                            }else{
                                a.setTextColor(Color.RED);
                                if(a_1.getText().toString().equals(question1.answer)){
                                    a_1.setTextColor(Color.GREEN);
                                }else if(a_2.getText().toString().equals(question1.answer)){
                                    a_2.setTextColor(Color.GREEN);
                                }else if(a_3.getText().toString().equals(question1.answer)){
                                    a_3.setTextColor(Color.GREEN);
                                }else{
                                    a_4.setTextColor(Color.GREEN);
                                } Handler handler=new Handler();
                                handler.postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        a.setTextColor(Color.parseColor("#808080"));
                                        a_1.setTextColor(Color.parseColor("#808080"));
                                        a_2.setTextColor(Color.parseColor("#808080"));
                                        a_3.setTextColor(Color.parseColor("#808080"));
                                        a_4.setTextColor(Color.parseColor("#808080"));
                                        wrong++;
                                        loadQuiz();
                                    }
                                },1500);


                            }
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),""+databaseError,Toast.LENGTH_LONG).show();
                }
            });
        }


    }
}