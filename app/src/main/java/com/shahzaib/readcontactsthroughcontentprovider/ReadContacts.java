package com.shahzaib.readcontactsthroughcontentprovider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.Toast;

public class ReadContacts extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView contactsRecyclerView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contacts);
        contactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED)
            {
                getSupportLoaderManager().initLoader(1,null,this);
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},0);
                Toast.makeText(this, "Grant the permission", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            getSupportLoaderManager().initLoader(1,null,this);
        }


    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if(checkSelfPermission(Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "User Granted the permission :)", Toast.LENGTH_SHORT).show();
                    getSupportLoaderManager().initLoader(1,null,this);
                }
            }
            else
            {
                Toast.makeText(this, "User Denied the permission :(", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},0);
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data != null)
        {
            if(data.getCount()>0){
                adapter = new Adapter();
                adapter.setCursor(data);
                contactsRecyclerView.setAdapter(adapter);
                Toast.makeText(this, "Total Contacts are: "+data.getCount(), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "There is no contact", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
