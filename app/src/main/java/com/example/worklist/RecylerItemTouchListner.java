package com.example.worklist;

import static androidx.recyclerview.widget.ItemTouchHelper.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worklist.Adapter.CustomAdapter;

import java.util.Objects;

public class RecylerItemTouchListner extends ItemTouchHelper.SimpleCallback {
   CustomAdapter customAdapter;
    public RecylerItemTouchListner(CustomAdapter adapter) {
        super(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        this.customAdapter=adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position=viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(customAdapter.getContext());

            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure ?");

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    customAdapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    customAdapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();

        }else{
            customAdapter.editItem(position);
        }
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(c, recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView=viewHolder.itemView;
        int bgColorOffset=20;

        if(dX>0){
            icon= ContextCompat.getDrawable(customAdapter.getContext(),R.drawable.baseline_edit);
            background=new ColorDrawable(ContextCompat.getColor(customAdapter.getContext(),R.color.simple_green));

        }else{
            icon=ContextCompat.getDrawable(customAdapter.getContext(),R.drawable.baseline_delete);
            background=new ColorDrawable(Color.RED);
        }
        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + bgColorOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - bgColorOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
