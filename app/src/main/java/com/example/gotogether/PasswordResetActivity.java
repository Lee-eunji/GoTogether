package com.example.gotogether;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance(); //인스턴스 초기화

        findViewById(R.id.signUpButton).setOnClickListener(OnClikListener);

    }

    View.OnClickListener OnClikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) { //클릭이되었을때
                case R.id.signUpButton:
                    send(); //함수실행
                    break;

            }
        }
    };
    //신규사용자  함수
    private void send(){
        //입력한 Text를 얻어오는 작업
        String email=((EditText)findViewById(R.id.emailEditText)).getText().toString();

        if(email.length()>0){
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startToast("이메일을 보냈습니다.");
                            }
                        }
                    });
        } else{
            startToast("이메일을 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
