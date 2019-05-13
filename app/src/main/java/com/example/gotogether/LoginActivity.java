package com.example.gotogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG="SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance(); //인스턴스 초기화

        findViewById(R.id.signUpButton).setOnClickListener(OnClikListener);
        findViewById(R.id.gotoPasswordReset).setOnClickListener(OnClikListener);
        findViewById(R.id.gotoPasswordReset).setOnClickListener(OnClikListener);
    }

    View.OnClickListener OnClikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) { //클릭이되었을때
                case R.id.signUpButton:
                    login(); //함수실행
                    break;
                case R.id.gotoPasswordReset:
                    myStartActivity(PasswordResetActivity.class);
                    break;
            }
        }
    };

    private void login(){
        //입력한 Text를 얻어오는 작업
        String email=((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password=((EditText)findViewById(R.id.passwordEditText)).getText().toString();

        //password확인이 맞으면
        if(email.length()>0&& password.length()>0){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공하셨습니다.");
                                finish();
                                //myStartActivity(MemberInitActivity.class);
                            } else {
                                if(task.getException() != null){
                                    startToast(task.getException().toString());
                                }
                            }
                            // ...
                        }
                    });

        } else{
            startToast("email,또는 비밀번호를 입력해 주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
