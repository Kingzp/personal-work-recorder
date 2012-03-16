package jp.gr.java_conf.choplin_j.imanani;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.EditText;

public class ShowEditDialog implements OnLongClickListener {
    private Context context;
    private View dialogLayout;
    private EditText taskCode;
    private EditText taskDescription;
    private AlertDialog customDialog;
    LayoutInflater inf;

    public ShowEditDialog(Context context, Cursor cursor) {
        this.context = context;

        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogLayout = inf.inflate(R.layout.task_dialog, null);
        taskCode = (EditText)dialogLayout.findViewById(R.id.taskCodeEdit);
        taskCode.setText(cursor.getString(1));
        taskDescription = (EditText)dialogLayout.findViewById(R.id.descriptionEdit);
        taskDescription.setText(cursor.getString(2));
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.edit_task);
        builder.setView(dialogLayout);
        builder.setPositiveButton(android.R.string.ok,
                                  new UpdateTask(cursor, cursor.getLong(0)));

        builder.setNegativeButton(android.R.string.cancel,
                                  new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface  dialog, int which) {
                                          dialog.dismiss();
                                      }
                                  });
        customDialog = builder.create();
    }

    @Override
    public boolean onLongClick(View v) {
        customDialog.show();
        return true;
    }

    private class UpdateTask implements DialogInterface.OnClickListener {
        private Cursor cursor;
        private long id;

        public UpdateTask(Cursor cursor, long id) {
            this.cursor = cursor;
            this.id = id;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            SQLiteDatabase db = (new DBOpenHelper(context)).getWritableDatabase();
            ContentValues taskValue = new ContentValues();
            taskValue.put("code", taskCode.getText().toString());
            taskValue.put("description", taskDescription.getText().toString());
            db.update(Task.TABLE_NAME, taskValue, "_id = ?",
                      new String[] { String.format("%d", id) });
            dialog.dismiss();
            cursor.requery();
        }
    }
}