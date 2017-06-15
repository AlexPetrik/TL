package ru.alexpetrik.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import ru.alexpetrik.todolist.utils.MyTask;
import ru.alexpetrik.todolist.utils.TaskListAdapter;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private ListView lvMain;
    private TaskListAdapter adapter;
    private Button btnAdd, btnDel;
    private Spinner spMain, spSort;
    private EditText etFind;

    private Menu menu;

    private Resources resources;

    private String[] types;
    private String[] sorts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        resources = getResources();

        lvMain = (ListView) findViewById(R.id.lvMain);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDel = (Button) findViewById(R.id.btnDel);

        spMain = (Spinner) findViewById(R.id.spMain);
        spSort = (Spinner) findViewById(R.id.spSort);

        etFind = (EditText)findViewById(R.id.etFind);

        types = resources.getStringArray(R.array.typesFilter);
        sorts = resources.getStringArray(R.array.sorts);

        Realm.init(this);

        realm = Realm.getDefaultInstance();
        RealmResults<MyTask> tasks = realm.where(MyTask.class).findAll();
        adapter = new TaskListAdapter(this, tasks, realm);
        lvMain.setAdapter(adapter);

        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        spMain.setAdapter(spAdapter);
        spMain.setPrompt(resources.getString(R.string.filterBy));

        spAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sorts);
        spSort.setAdapter(spAdapter);
        spSort.setPrompt(resources.getString(R.string.sortBy));

        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                for (int i = 0; i < parent.getCount(); i++) {
                    View v = parent.getAdapter().getView(i, parent.getChildAt(i), parent);
                    v.findViewById(R.id.checkBox).setVisibility(View.VISIBLE);
                }

                btnAdd.setVisibility(View.GONE);
                btnDel.setVisibility(View.VISIBLE);

                return true;
            }
        });

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), EditTaskActivity.class);

                MyTask task = (MyTask) lvMain.getAdapter().getItem(position);

                intent.putExtra("caption", task.getCaption());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("deadline", task.getDeadline());
                intent.putExtra("ref", task.getRefToFile());
                intent.putExtra("date", task.getDate());
                intent.putExtra("type", task.getTypeOfNote());
                intent.putExtra("remind", task.isRemind());

                startActivityForResult(intent, 1);
            }
        });

        spMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayFilterOrSortListOnScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayFilterOrSortListOnScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayFilterOrSortListOnScreen();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MenuItem mItem;

        switch (item.getItemId()){
            case R.id.select_all:
                for (int i = 0; i < lvMain.getCount(); i++) {
                    View v = lvMain.getAdapter().getView(i, lvMain.getChildAt(i), lvMain);
                    CheckBox chb = (CheckBox) v.findViewById(R.id.checkBox);
                    chb.setVisibility(View.VISIBLE);
                    chb.setChecked(true);
                }

                btnAdd.setVisibility(View.GONE);
                btnDel.setVisibility(View.VISIBLE);

                item.setVisible(false);
                mItem = menu.findItem(R.id.deselect_all);
                mItem.setVisible(true);

                break;
            case R.id.deselect_all:
                for (int i = lvMain.getCount() - 1; i >= 0; i--) {
                    View v = lvMain.getAdapter().getView(i, lvMain.getChildAt(i), lvMain);
                    CheckBox chb = (CheckBox) v.findViewById(R.id.checkBox);
                    chb.setVisibility(View.GONE);
                    chb.setChecked(false);
                }

                btnAdd.setVisibility(View.VISIBLE);
                btnDel.setVisibility(View.GONE);

                item.setVisible(false);
                mItem = menu.findItem(R.id.select_all);
                mItem.setVisible(true);

                break;
            case R.id.find:
                etFind.setVisibility(View.VISIBLE);
                etFind.setSelection(0);
                item.setVisible(false);
                mItem = menu.findItem(R.id.cancel_find);
                mItem.setVisible(true);

                break;
            case R.id.cancel_find:
                etFind.setText("");
                etFind.setVisibility(View.GONE);
                item.setVisible(false);
                mItem = menu.findItem(R.id.find);
                mItem.setVisible(true);

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayFilterOrSortListOnScreen(){

        RealmQuery<MyTask> rq;
        RealmResults<MyTask> rr;
        String s = etFind.getText().toString();

        if (types[0].equalsIgnoreCase(spMain.getSelectedItem().toString()) &&
                sorts[0].equalsIgnoreCase(spSort.getSelectedItem().toString())) {

            rq = realm.where(MyTask.class);
            rq = addSearchTerms(rq, s);
            rr = rq.findAll();

        } else if (types[0].equalsIgnoreCase(spMain.getSelectedItem().toString())) {

            rq = realm.where(MyTask.class);
            rq = addSearchTerms(rq, s);
            rr = rq.findAllSorted(spSort.getSelectedItem().toString());

        } else if (sorts[0].equalsIgnoreCase(spSort.getSelectedItem().toString())) {

            rq = realm.where(MyTask.class);
            rq = addSearchTerms(rq, s);
            rq = rq.equalTo("typeOfNote", spMain.getSelectedItem().toString());
            rr = rq.findAll();

        } else {
            rq = realm.where(MyTask.class);
            rq = addSearchTerms(rq, s);
            rq = rq.equalTo("typeOfNote", spMain.getSelectedItem().toString());
            rr = rq.findAllSorted(spSort.getSelectedItem().toString());

        }

        adapter = new TaskListAdapter(this, rr, realm);
        lvMain.setAdapter(adapter);
    }

    private RealmQuery<MyTask> addSearchTerms(RealmQuery<MyTask> rq, String s){
        if (!s.isEmpty())
            rq = rq.contains("caption", s)
                    .or()
                    .contains("deadline", s);
        return rq;
    }

    public void onClickButton(View view) {

        switch (view.getId()){
            case R.id.btnAdd:
                addTask();
                break;
            case R.id.btnDel:
                delTask();
                break;
            default:
                break;
        }

    }

    private void delTask() {

        realm.beginTransaction();

        RealmResults<MyTask> tasks = realm.where(MyTask.class).findAll();
        if (!tasks.isEmpty()) {

            ArrayList<Integer> checkedItems = adapter.getCheckedItems();

            for (int i = checkedItems.size() - 1; i >= 0; i--){
                tasks.get(i).deleteFromRealm();
            }
        }

        realm.commitTransaction();
        btnAdd.setVisibility(View.VISIBLE);
        btnDel.setVisibility(View.GONE);

        clearCheckBox();
    }

    private void clearCheckBox() {

        for (int i = 0; i < lvMain.getCount(); i++) {
            View v = lvMain.getAdapter().getView(i, lvMain.getChildAt(i), lvMain);
            CheckBox chb = (CheckBox) v.findViewById(R.id.checkBox);
            chb.setChecked(false);
            chb.setVisibility(View.GONE);
        }

    }

    private void addTask() {

        Intent intent = new Intent(this, EditTaskActivity.class);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK)
            return;

        if (data == null)
            return;

        realm.beginTransaction();
        MyTask task = new MyTask();

        task.setCaption(data.getStringExtra("caption"));
        task.setRefToFile(data.getStringExtra("ref"));
        task.setDescription(data.getStringExtra("description"));
        task.setTypeOfNote(data.getStringExtra("type"));
        task.setRemind(data.getBooleanExtra("remind", false));
        task.setDeadline(data.getStringExtra("deadline"));

        String tmpId = data.getStringExtra("date");
        if (tmpId == null)
            tmpId = Calendar.getInstance().getTime().toString();
        task.setDate(tmpId);

        realm.copyToRealmOrUpdate(task);

        realm.commitTransaction();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(resources.getString(R.string.exit));
        quitDialog.setPositiveButton(resources.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton(resources.getString(R.string.no),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        quitDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
