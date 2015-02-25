package com.example.jegerima.SIDWeb.fragmentsDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jegerima.SIDWeb.R;

/**
 * Created by Jegerima on 25/02/2015.
 */
public class TextFragmentDialog extends DialogFragment {

    private int type;
    private String msg;
    private String content;
    TextFragmentDialog tfd;

    public TextFragmentDialog(int type, String msg)
    {
        this.type = type;
        this.msg = msg;
        this.content = "";
        tfd = this;
    }

    @Override
    public void  onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch (type) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }

            //case 4: theme = android.R.style.Theme_Holo; break;
            //case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            //case 6: theme = android.R.style.Theme_Holo_Light; break;
            //case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            theme = android.R.style.Theme_Holo_Light;

        setStyle(style, theme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final View v = inflater.inflate(R.layout.text_input_fragmen_dialog, null);
        TextView tv = (TextView)v.findViewById(R.id.lbl_msg);
        tv.setText(this.msg);
        tv.setTextIsSelectable(true);

        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String str = ((EditText)v.findViewById(R.id.txt_content)).getText().toString();
                        System.out.println(str);
                        msg = str;
                    }
                })
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String str = ((EditText)v.findViewById(R.id.txt_content)).getText().toString();
                        System.out.println(str);
                        msg = str;
                    }
                });

        return builder.create();
    }
}
