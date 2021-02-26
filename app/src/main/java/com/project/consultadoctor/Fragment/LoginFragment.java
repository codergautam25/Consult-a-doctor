package com.project.consultadoctor.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.consultadoctor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText mEmaitEt,mPasswordEt;
    Button mLoginBtn;
    TextView mNotHaveAccountTv,mRecoverPasswordTv;
    SignInButton mGoogleLoginBtn;
    ProgressDialog progressDialog;

/*
    private static final int RC_SIGN_IN=100;
    GoogleSignInClient mGoogleSignInClient;*/


    FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    private MyCallback mListener = null;

    public interface MyCallback{
        void changeFragment(String fName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MyCallback) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);

/*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getActivity().getResources().getString( R.string.default_web_client_id))
                .requestEmail()
                .build();*/
       /* mGoogleSignInClient= GoogleSignIn.getClient(getActivity(),gso);*/
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getContext());
        mEmaitEt=view.findViewById(R.id.emailEt);
        mPasswordEt=view.findViewById(R.id.passordEt);
        mLoginBtn=view.findViewById(R.id.loginBtn);
        mNotHaveAccountTv=view.findViewById(R.id.nothave_accountTV);
        mRecoverPasswordTv=view.findViewById(R.id.recoverPasswordTv);
      /*  mGoogleLoginBtn=view.findViewById(R.id.googleSignInBtn);
*/
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mEmaitEt.getText().toString().trim();
                String password=mPasswordEt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmaitEt.setError("Invalid Email");
                    mEmaitEt.setFocusable(true);
                }else if (password.length()<6){
                    mPasswordEt.setError("Invalid Password");
                    mPasswordEt.setFocusable(true);

                }else {
                    loginUser(email,password);
                }
            }
        });
        mNotHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mListener.changeFragment("register");

            }
        });

        mRecoverPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

      /*  mGoogleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });*/

        return view;
    }


    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(getContext());
        final EditText emailEt=new EditText(getContext());
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailEt.getText().toString().trim();
                beginRecovery(email);

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {


        progressDialog.setMessage("Sending Email....");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"Email Sent",Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(getActivity(),"Failed....",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loginUser(String email, String password) {

        progressDialog.setMessage("Logging In....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+user.getUid());
                            Toast.makeText(getActivity(), "Logged In...\n"+user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            mListener.changeFragment("dashboard");
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go previous activity
        return super.onSupportNavigateUp();
    }
*/
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                String email=user.getEmail();
                                String uid=user.getUid();

                                HashMap<Object,String> hashMap=new HashMap<>();
                                hashMap.put("email",email);
                                hashMap.put("uid",uid);
                                hashMap.put("name","");//user later
                                hashMap.put("onlineStatus","online");
                                hashMap.put("typingTo","noOne");
                                hashMap.put("phone","");
                                hashMap.put("image","");
                                hashMap.put("cover","");
                                //FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+user.getUid());
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                DatabaseReference reference=database.getReference("Users");
                                reference.child(uid).setValue(hashMap);
                            }

                            assert user != null;
                            Toast.makeText(getActivity(),"success "+user.getEmail(),Toast.LENGTH_SHORT).show();
                           mListener.changeFragment("dashboard");
                        } else {
                            Toast.makeText(getActivity(),"Login failed..",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
