package com.project.consultadoctor.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.consultadoctor.DashboardActivity;
import com.project.consultadoctor.Modal.ProfileModal;
import com.project.consultadoctor.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

/**
 * A simple {@link Fragment} subclass.
 */;

public class ProfileFragment extends Fragment {



    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    MyCallback mCallback=null;

    public interface MyCallback {

        void singOut();
    }


    StorageReference storageReference;
    String storagePath="Users_Profile_Cover_Imgs/";

    ImageView avatarIv,coverIv;
    TextView nameTv,emailTv,phoneTv,genderTv,specializationTv,experienceTv,degreeTv;
    TextInputLayout tilGender,tilExperience,tilSpecialization,tilDegree;
    FloatingActionButton fab;
    Button btn_logOut;

    ProgressDialog pd;

    String name,image,email,phone,cover,gender,specialization,degree,experience;

    private static  final  int CAMERA_REQUEST_CODE=1;
    private static  final  int STORAGE_REQUEST_CODE=2;
    private static  final  int IMAGE_PICK_GALLERY_CODE=3;
    private static  final  int IMAGE_PICK_CAMERA_CODE=4;

    //array of permissions to be required
    String cameraPermission[];
    String storagePermission[];

    //uri of picked Image
    Uri image_uri;

    //for checking profile or cover photo
    String profileOrCoverPhoto;
    Boolean isDoctor;


    public ProfileFragment(Boolean isDoctor) {
        this.isDoctor = isDoctor;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback= (MyCallback) context;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);


        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        if(isDoctor){
            databaseReference=firebaseDatabase.getReference("Users").child("Doctor");
        }else{
            databaseReference=firebaseDatabase.getReference("Users").child("Patient");
        }
        storageReference=getInstance().getReference();


        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        avatarIv =(ImageView) view.findViewById(R.id.avatarIv);
        nameTv=(TextView) view.findViewById(R.id.nameTv);
        emailTv=(TextView)view.findViewById(R.id.emailTv);
        phoneTv=(TextView)view.findViewById(R.id.phoneTv);
        coverIv=(ImageView) view.findViewById(R.id.coverIv);
        genderTv = (TextView) view.findViewById(R.id.genderTv);
        specializationTv = (TextView) view.findViewById(R.id.specializationTv);
        experienceTv = (TextView) view.findViewById(R.id.experienceTv);
        degreeTv = (TextView) view.findViewById(R.id.degreeTv);
        btn_logOut=(Button)view.findViewById(R.id.btn_logOut);

        tilGender=(TextInputLayout)view.findViewById(R.id.tilGender);
        tilExperience = (TextInputLayout)view.findViewById(R.id.tilExperience);
        tilSpecialization=(TextInputLayout)view.findViewById(R.id.tilSpecialization);
        tilDegree=(TextInputLayout)view.findViewById(R.id.tilDegree);



        fab=view.findViewById(R.id.fab);

        pd=new ProgressDialog(getActivity());

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference;

        if(isDoctor){
            reference=database.getReference("Users").child("Doctor");
        }else{
            reference=database.getReference("Users").child("Patient");
        }

         reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot ds:dataSnapshot.getChildren()){
                   ProfileModal profileModal=ds.getValue(ProfileModal.class);
                   if (profileModal.getUid().equals(user.getUid())){
                       if(isDoctor){
                           updateValueForDoctor(profileModal);
                       }else{
                           updateValueForPatient(profileModal);
                       }
                       break;
                   }
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();

            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                mCallback.singOut();

            }
        });
        return view;
    }

    private void updateValueForDoctor(ProfileModal profileModal) {
        name = profileModal.getName();
        nameTv.setText(name);

        email = profileModal.getEmail();
       emailTv.setText(email);

        phone = profileModal.getPhone();
         phoneTv.setText(phone);

        tilSpecialization.setVisibility(View.VISIBLE);
        specialization = profileModal.getSpecialization();
        specializationTv.setText(specialization);

        tilExperience.setVisibility(View.VISIBLE);
        experience = profileModal.getExperience();
        experienceTv.setText(experience.concat(" years of experience"));

        tilDegree.setVisibility(View.VISIBLE);
        degree = profileModal.getDegree();
        degreeTv.setText(degree);

        image = profileModal.getImage();
        cover=profileModal.getCover();

        try {
            String val=profileModal.getImage();
            Picasso.get().load(val).placeholder(R.drawable.ic_person_black_24dp).fit().into(avatarIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_perm_identity_black_24dp).into(avatarIv);
        }
        try {
            Picasso.get().load(profileModal.getCover()).into(coverIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_perm_identity_black_24dp).into(coverIv);
        }

    }
    private void updateValueForPatient(ProfileModal profileModal) {
        name = profileModal.getName();
        nameTv.setText(name);

        email = profileModal.getEmail();
        emailTv.setText(email);

        phone = profileModal.getPhone();
        phoneTv.setText(phone);

       tilGender.setVisibility(View.VISIBLE);
        gender = profileModal.getGender();
        genderTv.setText(gender);

        image = profileModal.getImage();
        cover=profileModal.getCover();

        try {
            String val=profileModal.getImage();
            Picasso.get().load(val).placeholder(R.drawable.ic_person_black_24dp).fit().into(avatarIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_perm_identity_black_24dp).into(avatarIv);
        }
        try {
            Picasso.get().load(profileModal.getCover()).into(coverIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_perm_identity_black_24dp).into(coverIv);
        }

    }

    private boolean checkStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        requestPermissions(storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

        boolean result1= ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){

        requestPermissions(cameraPermission,CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        String options[]={"Edit Profile Picture","Edit Cover Photo","Edit Name","Edit Phone"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    //Edit Profile Clicked
                    pd.setMessage("Updating Profile Picture");
                    profileOrCoverPhoto="image";
                    showImagePicDialog();
                }else if(which==1){
                    //Edit Cover Clicked
                    pd.setMessage("Updating Cover Photo");
                    profileOrCoverPhoto="cover";
                    showImagePicDialog();
                }else if (which==2){
                    //Edit Name clicked
                    pd.setMessage("Updating Name..");


                    showNamePhoneUpdateDialog("name");
                }else if (which==3){
                    //Edit Phone Clicked
                    pd.setMessage("Updating Phone Number..");
                    showNamePhoneUpdateDialog("phone");
                }
            }
        });

        builder.create().show();
    }

    private void showNamePhoneUpdateDialog(final String key) {


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Update "+ key);

        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        final EditText editText=new EditText(getActivity());
        editText.setHint("Enter "+key); //hint : e.g. edit name or edit phone
        linearLayout.addView(editText);

        builder.setView(linearLayout);


        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value=editText.getText().toString().trim();

                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String,Object> result=new HashMap<>();
                    result.put(key,value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //update dismiss progress
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"Updated..",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            pd.dismiss();
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //create and show
        builder.create().show();
    }

    private void showImagePicDialog() {
        //show dialog containing options camera and Gallery to pick the image
        String options[]={"Camera","Gallery"};

        //Alert Dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle("Pick Image");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items to click
                if (which==0){
                    //Camera Clicked
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }

                }else if(which==1){
                    //Gallery Clicked
                    if (!checkStoragePermission())
                    {
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){

                        pickFromCamera();
                    }else {

                        Toast.makeText(getActivity(),"Please enable Camera And Storage Permissions",Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean writeStorageAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){

                        pickFromGallery();
                    }else {

                        Toast.makeText(getActivity(),"Please enable Storage Permissions",Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode==RESULT_OK){
            if (requestCode==IMAGE_PICK_GALLERY_CODE){
                image_uri=data.getData();

                uploadProfilecoverPhoto(image_uri);
            }
            if (requestCode==IMAGE_PICK_CAMERA_CODE){
                uploadProfilecoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilecoverPhoto(Uri uri) {
        //show progress
        pd.show();


        String filePathAndName=storagePath+ " "+ profileOrCoverPhoto + " " + user.getUid();

        StorageReference storageReference2nd=storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image is uploaded to storage,now get it's url and store in user's database
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri=uriTask.getResult();


                if (uriTask.isSuccessful()){

                    HashMap<String,Object>results=new HashMap<>();

                    results.put(profileOrCoverPhoto,downloadUri.toString());
                    databaseReference.child(user.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            pd.dismiss();
                            Toast.makeText(getActivity(),"Image Updated....",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            pd.dismiss();
                            Toast.makeText(getActivity(),"Error in Updating....",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {

                    pd.dismiss();
                    Toast.makeText(getActivity(),"Some Error Occurred",Toast.LENGTH_SHORT).show();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void pickFromGallery() {

        Intent galleryIntent=new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {

        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        image_uri=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);


    }


    private  void checkUserStatus(){

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if (user!=null){

            //mPeofileTv.setText(user.getEmail());
        }else {
            startActivity(new Intent(getActivity(), DashboardActivity.class));
            getActivity(). finish();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        if (id==R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
