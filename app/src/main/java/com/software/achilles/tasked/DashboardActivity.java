package com.software.achilles.tasked;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.clans.fab.FloatingActionMenu;
import com.software.achilles.tasked.extras.BlurBuilder;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        scrollFabListener();
    }

    private void scrollFabListener(){
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.scrollView);
        final FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);

        Animation fab_slide_down = AnimationUtils.loadAnimation(this, R.anim.fab_slide_down);
        fab_slide_down.setInterpolator(new AccelerateInterpolator());

        Animation fab_slide_up = AnimationUtils.loadAnimation(this, R.anim.fab_slide_up);
        fab_slide_up.setInterpolator(new AccelerateInterpolator());

        fam.setMenuButtonHideAnimation(fab_slide_down);
        fam.setMenuButtonShowAnimation(fab_slide_up);


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int direction = scrollY-oldScrollY;

                if (direction > 0)
                    fam.hideMenu(true);
                else
                    fam.showMenu(true);

            }
        });
    }

    @Override
    public void onBackPressed() {
        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.menuFAB);

        if(fam.isOpened())
            fam.close(true);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_filter:
                break;

            case R.id.action_search:
                break;

            case R.id.action_settings:
                Intent intent = new Intent(this, Preferences.class);
                startActivity(intent);
                break;

            case android.R.id.home:
//                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
