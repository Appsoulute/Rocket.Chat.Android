package chat.rocket.app.ui.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import chat.rocket.app.R;
import chat.rocket.app.ui.base.BaseActivity;
import chat.rocket.models.Message;
import chat.rocket.models.Messages;
import chat.rocket.operations.meteor.MessageSearchResults;
import chat.rocket.operations.methods.listeners.LoadHistoryListener;
import chat.rocket.operations.methods.listeners.LogListener;
import chat.rocket.operations.methods.listeners.MessageSearchListener;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<Message> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat Room: GENERAL");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.LeftNavView);
        navigationView.setNavigationItemSelectedListener(this);
        ListView listview = (ListView) findViewById(R.id.RoomMsgsListView);
        mAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<>());
        listview.setAdapter(mAdapter);
        mRocketMethods.loadHistory("GENERAL", 0, 50, 0, new LoadHistoryListener() {
            @Override
            public void onResult(Messages result) {
                mAdapter.addAll(result.getMessages());
            }

            @Override
            public void onError(String error, String reason, String details) {
                super.onError(error, reason, details);
            }
        });
        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.RightNavView);
        rightNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.RightNavViewMenuItem) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        } else if (item.getItemId() == R.id.SearchMenuItem) {
            mRocketMethods.messageSearch("GENERAL", "teste", new MessageSearchListener() {
                @Override
                public void onResult(MessageSearchResults result) {
                    mAdapter.clear();
                    mAdapter.addAll(result.getMessages());
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }
        return false;
    }

}