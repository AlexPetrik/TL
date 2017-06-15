package ru.alexpetrik.todolist.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ru.alexpetrik.todolist.EditTaskActivity;

public class TaskDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String title = args.getString("title");
        String message = args.getString("message");
        boolean remind = args.getBoolean("remind");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        if (remind){
//            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ((EditTaskActivity) getActivity()).createRemind();
//                        }
//                    });
//            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//                            ((EditTaskActivity) getActivity()).cancelRemindCreate();
//                        }
//                    });
        } else {
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((EditTaskActivity) getActivity()).okClicked();
                }
            });
            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ((EditTaskActivity) getActivity()).cancelClicked();
                }
            });
        }

        builder.setCancelable(true);
        return builder.create();
    }
}
