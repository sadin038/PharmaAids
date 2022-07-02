package com.example.pharmaaids;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterForChatList extends RecyclerView.Adapter<AdapterForChatList.MyViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Context context;
    private List<Pharma> pharmas;
    private static final int REQUEST_CODE = 1;
    private String phone;


    public AdapterForChatList(Context context, List<Pharma> pharmas) {
        this.context = context;
        this.pharmas = pharmas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_user_chat_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Pharma pharma = pharmas.get(position);
        holder.textView1.setText(pharma.getName());

        Picasso.with(context).load(pharma.getImgUrl()).
                placeholder(R.mipmap.ic_launcher).into(holder.circleImageView);

        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = pharma.getPhone();
                phoneCall();
            }
        });
        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MessageActivity.class);
                intent.putExtra("userId",pharma.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pharmas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        ImageView imageView1,imageView2;
        CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.UserChatListNameId);
            imageView1 = itemView.findViewById(R.id.UserChatListPhoneId);
            imageView2 = itemView.findViewById(R.id.UserChatLisEmailId);
            circleImageView = itemView.findViewById(R.id.UserChatListCircleImageView);

        }
    }


    private void phoneCall()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {


            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE);

            return;
        }
        else
        {
            context.startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                phoneCall();
            }
            else
            {
                Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
