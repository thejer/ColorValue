package com.google.developer.colorvalue;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.developer.colorvalue.data.CardProvider;
import com.google.developer.colorvalue.ui.ColorView;

public class CardDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private ColorView mDetailsColorView;
    private TextView mDetailsColorName,
            mDetailsColorHex;

    private Uri mDetailsUri;

    private static final int ID_DETAIL_LOADER = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDetailsColorView = findViewById(R.id.details_color_view);
        mDetailsColorName = findViewById(R.id.details_color_name);
        mDetailsColorHex = findViewById(R.id.details_hex_value);

        mDetailsUri = getIntent().getData();
        if (mDetailsUri == null)
            throw new NullPointerException(getString(R.string.uri_cannot_be_null));


        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete){
            deleteColor();
        }else if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mDetailsUri,
                        null,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()){
            String hex = data.getString(data.getColumnIndex(CardProvider.Contract.Columns.COLOR_HEX));
            mDetailsColorView.setColor(hex);
            String name = data.getString(data.getColumnIndex(CardProvider.Contract.Columns.COLOR_NAME));
            mDetailsColorName.setText(name);
            mDetailsColorHex.setText(hex);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteColor (){

        new AsyncTask<Uri, Void, Integer>() {
            @Override
            protected Integer doInBackground(Uri... uris) {
                return getContentResolver().delete(uris[0], null, null);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if (integer > 0) {
                    CardDetailsActivity.this.finish();
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CardDetailsActivity.this, "Unable to delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.execute(mDetailsUri);
    }
}
