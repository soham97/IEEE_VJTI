package vjti.ieee.com.ieee_vjti;

import android.*;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sohamdeshmukh on 04/11/17.
 */

public class BlogListActivity extends AppCompatActivity {

    DatabaseReference database;
    BlogRecyclerViewAdapter adapter;
    private SearchView mSearchView;
    private RecyclerView mBlogRecyclerView;

    static boolean calledAlready = false;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        new BackgroundFunctions().execute();

        final Toolbar tToolbar = (Toolbar) findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        database = FirebaseDatabase.getInstance().getReference();
        adapter = new BlogRecyclerViewAdapter(Collections.<Blog>emptyList());

        mBlogRecyclerView = (RecyclerView) findViewById(R.id.blog_recycler_view);
        mBlogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBlogRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateadapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_blog_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                final List<Blog> mBlogs;
                SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
                mSearchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
                mBlogs = getbloglist();
                SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
                {
                    @Override
                    public boolean onQueryTextChange(String query) {
                        if (query.matches("")) {
                            updateadapter();
                        } else {
                            query = query.toLowerCase();
                            final List<Blog> filteredRegisterList = new ArrayList<>();
                            for (Blog blog : mBlogs) {
                                final String text = blog.getTitle().toLowerCase();
                                final String text1 = blog.getSubject().toLowerCase();
                                if (text.contains(query) || text1.contains(query) || text1.contains(query)) {
                                    filteredRegisterList.add(blog);
                                }
                            }
                            adapter.animateTo(filteredRegisterList);
                            mBlogRecyclerView.scrollToPosition(0);
                        }

                        return true;
                    }
                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        mSearchView.clearFocus();
                        return true;
                    }
                };
                mSearchView.setOnQueryTextListener(textChangeListener);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateadapter()
    {
        database.child("blogs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Blog> blogs = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Blog blog = noteDataSnapshot.getValue(Blog.class);
                    blogs.add(blog);
                }
                adapter.updateList(blogs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int audio = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private class BackgroundFunctions extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground (Void...params) {
            checkAndRequestPermissions();
            return null;
        }
    }

    public List<Blog> getbloglist()
    {
        final List<Blog> blogs = new ArrayList<>();
        database.child("blogs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Blog blog = noteDataSnapshot.getValue(Blog.class);
                    Log.e("Subject", String.valueOf(blog.getSubject()));
                    blogs.add(blog);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return blogs;
    }

}
