package com.example.worklist;

import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.worklist.Adapter.CustomAdapter;
import com.example.worklist.Model.WorkListModel;
import com.example.worklist.Utils.DatabaseConnection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListner{
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    List<WorkListModel> workList;
    WorkListModel workListModel;
    FloatingActionButton floatingActionButton;
    private DatabaseConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.sky_blue));
        }
        // Edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseConnection(this);
        db.openDatabase();

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workList = new ArrayList<>();
        workList.addAll(db.getAllTasks());
        Collections.reverse(workList);

        customAdapter = new CustomAdapter(this, workList, db);
        recyclerView.setAdapter(customAdapter);
        //Set Swip behaviour
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecylerItemTouchListner(customAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // FloatingActionButton setup
        floatingActionButton = findViewById(R.id.addButtonId);
        floatingActionButton.setOnClickListener(view -> {
            AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
        });
    }

    @Override
    public void HandleDialogClose(DialogInterface dialog) {
        workList=db.getAllTasks();
        Collections.reverse(workList);
        customAdapter.setTasks(workList);
        customAdapter.notifyDataSetChanged();

    }
}
