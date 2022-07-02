package com.example.pharmaaids;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForShowAllMedicines extends RecyclerView.Adapter<AdapterForShowAllMedicines.MyViewholder> {

    private Context context;
    private List<Medicine> medicines;

    public AdapterForShowAllMedicines(Context context, List<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fragment_all_medicine_list,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        Medicine medicine = medicines.get(position);
        holder.textView1.setText(medicine.getName());
        holder.textView2.setText(medicine.getAvailability());
        holder.textView3.setText(medicine.getPrice());

    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView textView1,textView2,textView3;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.UserShowMedicineNameId);
            textView2 = itemView.findViewById(R.id.UserShowMedicineAvailabilityId);
            textView3 = itemView.findViewById(R.id.UserShowMedicinePriceId);
        }
    }
}
