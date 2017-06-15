package ru.alexpetrik.todolist;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ru.alexpetrik.todolist.utils.TaskDialog;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etCaption, etDeadline, etRef, etDescription;
    private CheckBox chbRemind;
    private Spinner spinner;
    private String date;
    private Intent intent;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etCaption = (EditText) findViewById(R.id.etCaption);
        etDeadline = (EditText) findViewById(R.id.etDateDeadline);
        etRef = (EditText) findViewById(R.id.etRef);
        etDescription = (EditText) findViewById(R.id.etDescription);
        spinner = (Spinner) findViewById(R.id.spEdit);
        chbRemind = (CheckBox) findViewById(R.id.chbRemind);

        res = getResources();

        String[] types = res.getStringArray(R.array.types);

        intent = getIntent();

        etCaption.setText(intent.getStringExtra("caption"));
        etRef.setText(intent.getStringExtra("ref"));
        etDescription.setText(intent.getStringExtra("description"));
        etDeadline.setText(intent.getStringExtra("deadline"));
        chbRemind.setChecked(intent.getBooleanExtra("remind", false));
        date = intent.getStringExtra("date");

        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        spinner.setAdapter(spAdapter);
        spinner.setPrompt(res.getString(R.string.sortBy));
        spinner.setSelection(findId(intent.getStringExtra("type"), types));

    }

    public void btnOk(View view) {
        intent = new Intent();
        intent.putExtra("caption", etCaption.getText().toString());
        intent.putExtra("ref", etRef.getText().toString());
        intent.putExtra("description", etDescription.getText().toString());
        intent.putExtra("deadline", etDeadline.getText().toString());
        intent.putExtra("date", date);
        intent.putExtra("type", spinner.getSelectedItem().toString());
        intent.putExtra("remind", chbRemind.isChecked());

        // TODO: 14.06.2017 - не сообразил пока, как сделать напоминание и уведомления...
//        if (chbRemind.isChecked()) {
//            TaskDialog dialog = new TaskDialog();
//            Bundle args = new Bundle();
//            args.putString("title", "Creating remind");
//            args.putString("message", "Create remind?");
//            args.putBoolean("remind", true);
//            dialog.setArguments(args);
//
//            FragmentManager manager = getSupportFragmentManager();
//            dialog.show(manager, "dialog");
//            setResult(RESULT_OK, intent);
//            finish();

//        } else {
            TaskDialog dialog = new TaskDialog();
            Bundle args = new Bundle();
            args.putString("title", res.getString(R.string.saving) + " " +
                    spinner.getSelectedItem().toString());
            args.putString("message", res.getString(R.string.save) + " " +
                    spinner.getSelectedItem().toString() + "?");
            dialog.setArguments(args);

            FragmentManager manager = getSupportFragmentManager();
            dialog.show(manager, "dialog");
//        }

    }

    private int findId(String str, String[] arr){
        for (int i = 0; i < arr.length; i++){
            if (arr[i].equalsIgnoreCase(str))
                return i;
        }
        return -1;
    }

    public void okClicked(){
        Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString() + " " +
                res.getString(R.string.saved), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, intent);
        finish();
    }

//    public void createRemind(){
//        TimePickerDialog tpDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                String[] s = etDeadline.getText().toString().replace('.', ' ').split(" ");
//                Calendar beginTime = Calendar.getInstance();
//                beginTime.set(Integer.parseInt(s[2]), Integer.parseInt(s[1]),
//                        Integer.parseInt(s[0]), hourOfDay, minute);
//                Intent intent = new Intent(Intent.ACTION_INSERT)
//                        .setData(CalendarContract.Events.CONTENT_URI)
//                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
//                                beginTime.getTimeInMillis())
//                        .putExtra(CalendarContract.Events.TITLE, "Yoga")
//                        .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
//                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
//                        .putExtra(CalendarContract.Events.AVAILABILITY,
//                                CalendarContract.Events.AVAILABILITY_BUSY);
//                startActivity(intent);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        }, 0, 0, true);
//        tpDialog.show();
//    }

//    public void cancelRemindCreate(){
//        chbRemind.setChecked(false);
//        intent.putExtra("remind", false);
//        setResult(RESULT_OK, intent);
//    }

    public void cancelClicked(){
        Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString() + " not saved!",
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
