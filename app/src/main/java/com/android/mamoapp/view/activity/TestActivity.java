package com.android.mamoapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.mamoapp.R;
import com.android.mamoapp.adapter.QuestionAdapter;
import com.android.mamoapp.api.ApiClient;
import com.android.mamoapp.api.ApiInterface;
import com.android.mamoapp.api.reponse.QuestionResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private QuestionAdapter questionAdapter;
    private RecyclerView recyclerViewQuestion;
    private TextView textViewQuestion;
    private MaterialButton materialButtonYes, materialButtonNo;

    private ArrayList<QuestionResponse.QuestionModel> questionModelArrayList = new ArrayList<>();
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        apiInterface = ApiClient.getClient();
        recyclerViewQuestion = findViewById(R.id.recyclerViewQuestion);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        materialButtonYes = findViewById(R.id.materialButtonYes);
        materialButtonNo = findViewById(R.id.materialButtonNo);

        apiInterface.getQuestion().enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                        if (!response.body().data.isEmpty()) {
                            questionModelArrayList.addAll(response.body().data);
                            //questionModelArrayList.get(position).isActive = true;
                            textViewQuestion.setText(questionModelArrayList.get(position).contentQuestion);
                            setRecyclerViewQuestion(questionModelArrayList);
                        }
                    }
                } else {
                    alertErrorServer();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                alertErrorServer();
                Log.e("question", t.getMessage());
            }
        });

        materialButtonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionModelArrayList.get(position).answer = true;
                position++;
                if (position < questionModelArrayList.size()) {
                    setRecyclerViewQuestion(questionModelArrayList);
                    textViewQuestion.setText(questionModelArrayList.get(position).contentQuestion);
                } else {
                    getResult();
                }
            }
        });

        materialButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionModelArrayList.get(position).answer = false;
                position++;
                if (position < questionModelArrayList.size()) {
                    setRecyclerViewQuestion(questionModelArrayList);
                    textViewQuestion.setText(questionModelArrayList.get(position).contentQuestion);
                } else {
                    getResult();
                }
            }
        });
    }

    public void alertErrorServer() {
        new AlertDialog.Builder(TestActivity.this)
                .setTitle("Pesan")
                .setMessage("Terjadi kesalahan pada server, silahkan coba beberapa saat lagi")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    public void setRecyclerViewQuestion(ArrayList<QuestionResponse.QuestionModel> list) {
        questionAdapter = new QuestionAdapter(list, position);
        recyclerViewQuestion.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewQuestion.setAdapter(questionAdapter);
    }

    public void getResult() {
        Intent intent = new Intent(this, TestResultActivity.class);
        intent.putExtra("RESULT", questionModelArrayList);
        startActivity(intent);
        finish();
    }
}