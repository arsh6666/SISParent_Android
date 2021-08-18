package dt.sis.parent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import dt.sis.parent.R;
import dt.sis.parent.databinding.ActivityMainBinding;
import dt.sis.parent.databinding.NavHeaderMainBinding;
import dt.sis.parent.fragments.ChatFriendFragment;
import dt.sis.parent.fragments.DashboardFragment;
import dt.sis.parent.helper.SessionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    Context mContext;
    SessionManager sessionManager;
    private boolean doubleBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = MainActivity.this;
        sessionManager = new SessionManager(mContext);

        setSupportActionBar(binding.appBarLayout.customToolbar.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarLayout.customToolbar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        Fragment homeFragment = DashboardFragment.getInstance();
        binding.appBarLayout.navBottomMenu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setFragmentLayout(homeFragment);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.nav_comment:
                    Fragment chatFragment = ChatFriendFragment.getInstance();
                    setFragmentLayout(chatFragment);
                    return true;

                case R.id.nav_home:
                    Fragment homeFragment = DashboardFragment.getInstance();
                    setFragmentLayout(homeFragment);
                    return true;
            }
            return false;
        }
    };

    public void setFragmentLayout(Fragment fragment) {
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBack) {
                finish();
            }

            this.doubleBack = true;
            Toast.makeText(mContext, "Please Click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBack = false;
                }
            }, 2000);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_comment, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.nav_home:
                Fragment homeFragment = DashboardFragment.getInstance();
                setFragmentLayout(homeFragment);
                break;

            case R.id.nav_change_password:
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                break;

            case R.id.nav_change_school:
                break;

            case R.id.nav_change_guardian:
                startActivity(new Intent(MainActivity.this, GuardianActivity.class));

                break;

            case R.id.nav_my_notification:
                 startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                 break;

            case R.id.nav_contact_us:
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                break;

            case R.id.nav_about_us:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;

            case R.id.nav_logout:
                sessionManager.logout();
                break;

            default:
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}