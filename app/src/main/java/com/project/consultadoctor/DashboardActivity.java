package com.project.consultadoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.consultadoctor.Fragment.HomeFragment;
import com.project.consultadoctor.Fragment.PayHistoryFragment;
import com.project.consultadoctor.Fragment.PaymentFragment;
import com.project.consultadoctor.Fragment.ProfileFragment;
import com.project.consultadoctor.Fragment.UsersFragment;
import com.project.consultadoctor.Modal.ProfileModal;

public class DashboardActivity extends AppCompatActivity implements ProfileFragment.MyCallback, PaymentFragment.MyPayCallback {

    private static Boolean isDoctor = false;

    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



            final FirebaseAuth user=FirebaseAuth.getInstance();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final BottomNavigationView navigationView=findViewById(R.id.navigation);
        DatabaseReference doctorReference=database.getReference("Users").child("Doctor");
       doctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              for (DataSnapshot ds:dataSnapshot.getChildren()){
                  ProfileModal profileModal=ds.getValue(ProfileModal.class);

                  if (profileModal.getUid().equals(user.getUid())){

                      isDoctor = true;
                     flag = 1;
                      navigationView.getMenu().findItem(R.id.nav_users).setTitle("Patients");
                      navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
                      ProfileFragment profileFragment=new ProfileFragment(isDoctor);
                      FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                      ft2.add(R.id.content,profileFragment,"");
                      ft2.commit();

                     break;
                  }
              }
              if(flag == 0){
                  isDoctor = false;
                  navigationView.getMenu().findItem(R.id.nav_users).setTitle("Doctors");
                  /*HomeFragment homeFragment=new HomeFragment();
                  navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
                  FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                  ft1.add(R.id.content,homeFragment,"");
                  ft1.commit();*/
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashboardActivity.this, databaseError.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

    }




    public BottomNavigationView.OnNavigationItemSelectedListener selectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                           // actionBar.setTitle("Home");

                            HomeFragment homeFragment=new HomeFragment();
                            FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,homeFragment,"");
                            ft1.commit();
                            return  true;

                        case R.id.nav_profile:
                          //  actionBar.setTitle("Profile");
                            ProfileFragment profileFragment=new ProfileFragment(isDoctor);
                            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,profileFragment,"");
                            ft2.commit();
                            return  true;

                        case R.id.nav_users:
                           // actionBar.setTitle("Users");
                            UsersFragment usersFragment=new UsersFragment(isDoctor);
                            FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,usersFragment,"");
                            ft3.commit();
                            return  true;

                        case R.id.nav_pay:
                          //  actionBar.setTitle("Chat");

                            if (isDoctor){
                               PayHistoryFragment payHistoryFragment=new PayHistoryFragment(true);
                               FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                               ft4.replace(R.id.content, payHistoryFragment,"");
                               ft4.commit();//wa

                           }else {
                               PaymentFragment paymentFragment=new PaymentFragment();
                               FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                               ft4.replace(R.id.content,paymentFragment,"");
                               ft4.commit();
                           }
                            return  true;
                    }
                    return false;
                }
            };

    @Override
    public void singOut() {
        startActivity(new Intent(getApplication(),RegisterActivity.class));
        finish();
    }

    @Override
    public void payCall() {
        PayHistoryFragment paymentHistory=new PayHistoryFragment(isDoctor);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content,paymentHistory,"");
        fragmentTransaction.commit();
    }
}
