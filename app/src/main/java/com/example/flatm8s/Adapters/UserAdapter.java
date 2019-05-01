package com.example.flatm8s.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flatm8s.R;
import com.example.flatm8s.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class implemented in order to help retrieve user Info onto the home screen
 * by inflating the item_user XML layout.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserProfile> profiles;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference(firebaseAuth.getUid());
    ImageView profilePic;

    public UserAdapter(Context c, ArrayList<UserProfile>p){
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(profiles.get(i).getUserName());
        myViewHolder.email.setText(profiles.get(i).getUserEmail());
        myViewHolder.dob.setText(profiles.get(i).getUserDOB());
        myViewHolder.age.setText(profiles.get(i).getUserAge());
        myViewHolder.university.setText(profiles.get(i).getUserUniversity());
        myViewHolder.course.setText(profiles.get(i).getUserCourse());
        myViewHolder.year.setText(profiles.get(i).getUserYear());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, email, dob, age, university, course, year;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.userInfoName);
            email = itemView.findViewById(R.id.userInfoEmail);
            dob = itemView.findViewById(R.id.userInfoAge);
            age = itemView.findViewById(R.id.userInfoAge);
            university = itemView.findViewById(R.id.userInfoUniversity);
            course = itemView.findViewById(R.id.userInfoCourse);
            year = itemView.findViewById(R.id.userInfoYear);
            profilePic = itemView.findViewById(R.id.userInfoPicture);

            storageReference.child("Images/Profile Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into(profilePic);
                }
            });
        }
    }
}
