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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForNearestPharmacy extends RecyclerView.Adapter<AdapterForNearestPharmacy.MyViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Context context;
    private List<Pharma> pharmas;
    private String phone;
    private static final int REQUEST_CODE = 1;

    public AdapterForNearestPharmacy(Context context, List<Pharma> pharmas) {
        this.context = context;
        this.pharmas = pharmas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_nearest_pharmacy, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Pharma pharma = pharmas.get(position);
        holder.textView.setText(pharma.getName());
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

        holder.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserNearestPharmaLocation.class);
                intent.putExtra("latitude",Double.toString(pharma.getLan()));
                intent.putExtra("longitude",Double.toString(pharma.getLon()));
                context.startActivity(intent);
            }
        });

        holder.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserShowAllMedicineList.class);
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

        TextView textView;
        ImageView imageView1,imageView2,imageView3,imageView4;
        CircleImageView circleImageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.NearestPharmacyNameId);

            imageView1 = itemView.findViewById(R.id.NearestPharmacyPhoneId);
            imageView2 = itemView.findViewById(R.id.NearestPharmacyEmailId);
            imageView3 = itemView.findViewById(R.id.NearestPharmacyLocationId);
            imageView4 = itemView.findViewById(R.id.NearestPharmacyMedicineId);
            circleImageView = itemView.findViewById(R.id.NearestPharmacyCircleImageId);
        }
    }

    private void phoneCall()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
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
