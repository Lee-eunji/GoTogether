package com.example.gotogether.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.gotogether.ReportInfo;
import com.example.gotogether.R;
import com.example.gotogether.ReportInfo;
import com.example.gotogether.adapter.ReportAdapter;
import com.example.gotogether.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ReportActivity extends BasicActivity {
    private static final String TAG = "ReportActivity";
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private ReportAdapter reportAdapter;
    private ArrayList<ReportInfo> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        //setToolbarTitle("Gotogether");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            myStartActivity(SignUpActivity.class);
        } else {
            firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("users").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        reportList = new ArrayList<>();
        reportAdapter= new ReportAdapter(ReportActivity.this,reportList);
        reportAdapter.setOnPostListener(onPostListener);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoboardButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoinfoButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotomainButton).setOnClickListener(onClickListener);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReportActivity.this));
        recyclerView.setAdapter(reportAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reportsUpdate();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.gotoboardButton:
                    myStartActivity(BoardActivity.class);
                    break;
                case R.id.gotoinfoButton:
                    myStartActivity(MemberInitActivity.class);
                    break;
                case R.id.gotomainButton:
                    break;
                case R.id.floatingActionButton:
                    myStartActivity(WriteReportActivity.class);
                    break;
            }
        }
    };

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete() {
            reportsUpdate();
            Log.e("로그: ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그: ","수정 성공");
        }
    };

    private void reportsUpdate() {
        if (firebaseUser != null) {
            CollectionReference collectionReference = firebaseFirestore.collection("reports");
            collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) { //서버에서 데이터가져오기
                            if (task.isSuccessful()) {
                                reportList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    reportList.add(new ReportInfo(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>) document.getData().get("contents"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime()),
                                            document.getId()));
                                }
                                reportAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
