package com.example.todotestapp.PopUps;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.todotestapp.DialogCloseListener;
import com.example.todotestapp.Otherclass.ToDoModel;
import com.example.todotestapp.R;
import com.example.todotestapp.classDB.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddPopUp2 extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private static final String type = "weakness";
    private EditText addtolist;
    private Button addtolistBtn;
    private DatabaseHandler db;
    SharedPreferences pref;

    public static AddPopUp2 newInstance(){
        return new AddPopUp2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);

        pref = getActivity().getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState){
        View view = inflator.inflate(R.layout.addtolist,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        addtolist = getView().findViewById(R.id.additem);
        addtolistBtn = getView().findViewById(R.id.addbtn);

        db = new DatabaseHandler(getActivity(),type);
        db.openDatabase();

        boolean updating = false;
        final Bundle bundle = getArguments();
        if(bundle !=null){
            updating = true;
            String task = bundle.getString("task");
            addtolist.setText(task);
            if(task.length()>0){
                addtolistBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
            }
        }
        addtolist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    addtolistBtn.setEnabled(false);
                    addtolistBtn.setTextColor(Color.GRAY);
                }
                else{
                    addtolistBtn.setEnabled(true);
                    addtolistBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalUpdating = updating;
        addtolistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = addtolist.getText().toString();
                if (finalUpdating){
                    db.updateTask(bundle.getInt("id"),txt);
                }
                else{
                    ToDoModel task = new ToDoModel();
                    task.setTask(txt);
                    task.setUser(pref.getString("loggedUser",null));
                    db.insertTask(task);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity act = getActivity();
        if(act instanceof DialogCloseListener){
            ((DialogCloseListener)act).handleDialogClose(dialog);
        }
    }
}
