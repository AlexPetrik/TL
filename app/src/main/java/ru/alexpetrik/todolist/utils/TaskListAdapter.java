package ru.alexpetrik.todolist.utils;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ru.alexpetrik.todolist.R;

public class TaskListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater lInflater;
    private RealmResults<MyTask> tasks;
    private Realm realm;

    private ArrayList<Integer> checkedItems;

    private CompoundButton.OnCheckedChangeListener checkedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked)
                checkedItems.add((Integer) buttonView.getTag());
            else
                checkedItems.remove((int) buttonView.getTag());
        }
    };

    public TaskListAdapter(Context _ctx, RealmResults<MyTask> tasks, Realm rlm) {
        this.context = _ctx;
        this.tasks = tasks;
        this.realm = rlm;
        lInflater = (LayoutInflater) _ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tasks.addChangeListener(new RealmChangeListener<RealmResults<MyTask>>() {
            @Override
            public void onChange(RealmResults<MyTask> myTasks) {
                notifyDataSetChanged();
            }
        });
        checkedItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public MyTask getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null)
            view = lInflater.inflate(R.layout.item, parent, false);

        final MyTask task = getItem(position);

        ((TextView) view.findViewById(R.id.tvDate)).setText(task.getDeadline());
        ((TextView) view.findViewById(R.id.tvCaption)).setText(task.getCaption());
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setTag(position);
        checkBox.setOnCheckedChangeListener(checkedChangeListener);
        view.findViewById(R.id.tvOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.mnu_item_delete:
                                realm.beginTransaction();
                                task.deleteFromRealm();
                                realm.commitTransaction();
                                Toast.makeText(context, "Deleted",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        return view;
}

    public ArrayList<Integer> getCheckedItems(){
        return checkedItems;
    }


}
