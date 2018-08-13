package com.dentalclinic.capstone.activities;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.RetrofitClient;
import com.dentalclinic.capstone.api.responseobject.SuccessResponse;
import com.dentalclinic.capstone.api.services.PaymentService;
import com.dentalclinic.capstone.api.services.UserService;
import com.dentalclinic.capstone.fragment.AppointmentFragment;
import com.dentalclinic.capstone.fragment.DentalFragment;
import com.dentalclinic.capstone.fragment.HistoryAppointmentFragment;
import com.dentalclinic.capstone.fragment.HistoryFragment;
import com.dentalclinic.capstone.fragment.HistoryPaymentFragment;
import com.dentalclinic.capstone.fragment.HistoryTreatmentFragment;
import com.dentalclinic.capstone.fragment.MyAccoutFragment;
import com.dentalclinic.capstone.fragment.NewsFragment;
import com.dentalclinic.capstone.fragment.PromotionFragment;
import com.dentalclinic.capstone.fragment.SettingFragment;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Staff;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.dentalclinic.capstone.utils.SettingManager;
import com.dentalclinic.capstone.utils.Utils;
import com.dentalclinic.capstone.view.DigitalView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rupins.drawercardbehaviour.CardDrawerLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.Util;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager = getSupportFragmentManager();
    private CardDrawerLayout drawer;
    public static AccountHeader headerResult = null;
    private Drawer result = null;
    private TextView currentDate;
    private static final int PROFILE_SETTING = 100000;
    private long elapsedTime = 0;
    private Button button;
    private DigitalView digitalView;
    private Handler handler;
    private FirebaseDatabase firebaseDbInstance;
    private ListView listView;
    private User user;
    private List<IProfile> listIprofile;
    private PrimaryDrawerItem logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.new_fragment_title));
        Utils.setVNLocale(this);
        SettingManager.initSetting(this);
        digitalView = findViewById(R.id.digital);
        currentDate = findViewById(R.id.txt_date);
        currentDate.setText(DateUtils.getCurrentDateFormat());
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        user = CoreManager.getUser(this);
        requestPermission();
        listIprofile = new ArrayList<>();
        if (user != null) {
            RetrofitClient.setAccessToken(user.getAccessToken());
            List<Patient> patients = user.getPatients();
            if (patients != null && patients.size() > 0) {
                IProfile profile = null;
                for (Patient p : patients) {
                    profile = new ProfileDrawerItem()
                            .withName(p.getName())
                            .withSelectedBackgroundAnimated(true)
                            .withEmail(p.getPhone())
                            .withIcon(p.getAvatar())
                            .withIdentifier(p.getId());
                    listIprofile.add(profile);
                }
            }
        } else {
            listIprofile.add(new ProfileSettingDrawerItem().withName("Đăng Nhập").withDescription("Đăng Nhập Tài Khoản").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING));
        }
//        listIprofile.add(new ProfileSettingDrawerItem().withName("Quản Lý Bệnh Nhân").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001));
        IProfile[] arrayIProfile = new IProfile[listIprofile.size()];
        listIprofile.toArray(arrayIProfile);
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
//                .addProfiles((IProfile) (listIprofile.toArray()))
                .addProfiles(arrayIProfile)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        CoreManager.setCurrentPatient((int) profile.getIdentifier(), MainActivity.this);
                        result.setSelectionAtPosition(1, true);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            if (user == null) {
                                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                startActivityForResult(intent1, AppConst.REQUEST_LOGIN_ACTIVITY);
                            }
                        } else {
                            CoreManager.setCurrentPatient((int) profile.getIdentifier(), MainActivity.this);
//                            result.setSelectionAtPosition(1,true);
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        //Create the drawer
        if (CoreManager.getUser(MainActivity.this) != null) {
            logout = new PrimaryDrawerItem();
            logout.withName(R.string.logout_titile).withIcon(R.drawable.ic_power_settings_new_black_24dp).withIdentifier(8).withSelectable(false);

        } else {
            logout = null;
        }
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.new_fragment_title).withIcon(R.drawable.ic_fiber_new_black_24dp).withIdentifier(1).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.dental_fragment_title).withIcon(R.drawable.ic_airline_seat_flat_angled_black_24dp).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.appointment_fragment_title).withIcon(R.drawable.ic_add_alert_black_24dp).withIdentifier(3).withSelectable(false),
                        new SectionDrawerItem().withName(R.string.account_nav_bar_title),
                        new PrimaryDrawerItem().withName(R.string.my_accout_fragment_title).withIcon(R.drawable.ic_account_circle_black_24dp).withIdentifier(4).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.histrory_fragment_title).withIcon(R.drawable.ic_history_black_24dp).withIdentifier(5).withSelectable(true).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        new PrimaryDrawerItem().withName(R.string.setting_title).withIcon(R.drawable.ic_settings_black_24dp).withIdentifier(99).withSelectable(true).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700))
//                        new PrimaryDrawerItem().withName(R.string.logout_titile).withIcon(R.drawable.ic_power_settings_new_black_24dp).withIdentifier(8).withSelectable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        user = CoreManager.getUser(MainActivity.this);
                        if (drawerItem != null) {
                            if (drawer.isDrawerOpen(GravityCompat.END)) {
                                drawer.closeDrawer(GravityCompat.END);
                            }
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                setTitle(getResources().getString(R.string.new_fragment_title));
                                NewsFragment newFragment = new NewsFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_fragment, newFragment).commit();
                            } else if (drawerItem.getIdentifier() == 2) {
                                setTitle(getResources().getString(R.string.dental_fragment_title));
                                DentalFragment dentalFragment = new DentalFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainActivity.this, QuickBookActivity.class);
                                startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                setTitle(getResources().getString(R.string.my_accout_fragment_title));
                                if (user == null) {
                                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivityForResult(intent1, AppConst.REQUEST_LOGIN_ACTIVITY);
                                } else {
                                    MyAccoutFragment myAccoutFragment = new MyAccoutFragment();
                                    fragmentManager.beginTransaction().replace(R.id.main_fragment, myAccoutFragment).commit();
                                }
                            } else if (drawerItem.getIdentifier() == 5) {
                                setTitle(getResources().getString(R.string.histrory_fragment_title));
                                if (user == null) {
                                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivityForResult(intent1, AppConst.REQUEST_LOGIN_ACTIVITY);
                                } else {
                                    HistoryFragment dentalFragment = new HistoryFragment();
                                    fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
                                }
                            } else if (drawerItem.getIdentifier() == 99) {
                                setTitle("Cài đặt");
                                SettingFragment settingFragment = new SettingFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_fragment, settingFragment).commit();
                                //donothing
                            } else {
                                if (result != null) {
                                    result.removeItem(8);
                                }
                                logout();
                                logoutOnServer();
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//              .withShowDrawerUntilDraggedOpened(true)
                .build();
        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            User user = CoreManager.getUser(this);
            if (user != null) {
                if (user.getCurrentPatient() != null) {
                    for (int i = 0; i < arrayIProfile.length; i++) {
                        if (arrayIProfile[i].getIdentifier() == user.getCurrentPatient().getId()) {
                            headerResult.setActiveProfile(arrayIProfile[i]);
                            break;
                        }
                    }
                }
            } else {
                headerResult.setActiveProfile(arrayIProfile[0]);
            }
        }
        result.setOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
            @Override
            public boolean onNavigationClickListener(View clickedView) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                return true;
            }
        });
//        result.updateBadge(5, new StringHolder(1 + ""));
        result.setSelectionAtPosition(1);

        listenOrderNumber();
//        logError("Active",headerResult.getActiveProfile().getName().toString());
        if (CoreManager.getUser(MainActivity.this) != null) {
            result.addItem(logout);
        }
    }

    @Override
    public String getMainTitle() {
        return null;
    }

    @Override
    public void onCancelLoading() {

    }

    public static void resetHeader(Context context) {
        headerResult.clear();
        User user = CoreManager.getUser(context);
        List<IProfile> listIprofile = new ArrayList<>();
        if (user != null) {
            List<Patient> patients = user.getPatients();
            if (patients != null && patients.size() > 0) {
                IProfile profile = null;
                for (Patient p : patients) {
                    profile = new ProfileDrawerItem()
//                            .withNameShown(true)
                            .withName(p.getName())
                            .withSelectedBackgroundAnimated(true)
                            .withEmail(p.getPhone())
                            .withIcon(p.getAvatar())
                            .withIdentifier(p.getId());
                    listIprofile.add(profile);
                }
            }
        } else {
            listIprofile.add(new ProfileSettingDrawerItem().withName("Đăng Nhập").withDescription("Đăng Nhập Tài Khoản").withIcon(new IconicsDrawable(context, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING));
        }
        headerResult.setProfiles(listIprofile);
        if (user != null) {
            if (user.getCurrentPatient() != null) {
                for (int i = 0; i < listIprofile.size(); i++) {
                    if (listIprofile.get(i).getIdentifier() == user.getCurrentPatient().getId()) {
                        headerResult.setActiveProfile(listIprofile.get(i));
                        break;
                    }
                }
            }
        } else {
            headerResult.setActiveProfile(listIprofile.get(0));
        }
    }

    public void createSlideBar() {

    }

    protected void showFeedbackDialog(TreatmentDetail treatmentDetail) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);
        TextView txtName = dialog.findViewById(R.id.txt_dentist_name);
        CircleImageView imgAvatar = dialog.findViewById(R.id.img_dentist_avatar);
        TextView txtTreatmentContent = dialog.findViewById(R.id.treatment_content_feedback);
        RatingBar ratingBar = dialog.findViewById(R.id.rate_bar);
        AutoCompleteTextView contentFeedback = dialog.findViewById(R.id.edit_content);
        Button button = dialog.findViewById(R.id.btn_submit_feedback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (treatmentDetail.getDentist().getName() != null) {
            txtName.setText(treatmentDetail.getDentist().getName());
        }
        String result = "";
        if (treatmentDetail.getTreatment().getName() != null) {
            result += treatmentDetail.getTreatment().getName();
        }
        if (treatmentDetail.getCreatedDate() != null) {
            result += treatmentDetail.getCreatedDate();
        }
        if (!result.isEmpty()) {
            txtTreatmentContent.setText(result);
        }
        if (treatmentDetail.getDentist().getAvatar() != null) {
            Picasso.get().load("https://thumbs.dreamstime.com/b/dentist-avatar-flat-icon-isolated-white-series-caucasian-blue-coat-background-eps-file-available-95672861.jpg").into(imgAvatar);
        }
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_new) {
            setTitle(getResources().getString(R.string.new_fragment_title));
            NewsFragment newFragment = new NewsFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, newFragment).commit();
        } else if (id == R.id.nav_dental) {
            setTitle(getResources().getString(R.string.dental_fragment_title));
            DentalFragment dentalFragment = new DentalFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_appointment) {
//            setTitle(getResources().getString(R.string.appointment_fragment_title));
////            AppointmentFragment dentalFragment = new AppointmentFragment();
////            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
            Intent intent = new Intent(MainActivity.this, QuickBookActivity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_promotion) {
//            setTitle(getResources().getString(R.string.promotion_fragment_title));
//            PromotionFragment dentalFragment = new PromotionFragment();
//            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_history_treatment) {
            setTitle(getResources().getString(R.string.history_treatment_fragment_title));
            HistoryTreatmentFragment dentalFragment = new HistoryTreatmentFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_history_appointment) {
            setTitle(getResources().getString(R.string.history_appointment_title));
            HistoryAppointmentFragment dentalFragment = new HistoryAppointmentFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
        } else if (id == R.id.nav_my_accout) {
            setTitle(getResources().getString(R.string.my_accout_fragment_title));
            MyAccoutFragment myAccoutFragment = new MyAccoutFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, myAccoutFragment).commit();
        } else if (id == R.id.nav_history_payment) {
            setTitle(getResources().getString(R.string.history_payment_fragment_title));
            HistoryPaymentFragment historyPaymentFragment = new HistoryPaymentFragment();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, historyPaymentFragment).commit();
        } else if (id == R.id.nav_log_out) {
//            logout();
            showWarningMessage("Đăng xuất");
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutOnServer() {
        UserService userService = APIServiceManager.getService(UserService.class);
        userService.logout().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SuccessResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<SuccessResponse> successResponseResponse) {
//                        showWarningMessage("Đăng xuất");

//                        result.removeItem(8);
                        logError("LogoutOnServer", "Logout success response");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void logout() {
        CoreManager.clearUser(MainActivity.this);
        user = null;
        result.setSelectionAtPosition(1, true);
        listIprofile = new ArrayList<>();
        listIprofile.add(new ProfileSettingDrawerItem().withName("Đăng Nhập").withIcon(new IconicsDrawable(MainActivity.this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING));
        headerResult.clear();
        headerResult.setProfiles(listIprofile);
        headerResult.setActiveProfile(listIprofile.get(0));
    }

    private void listenOrderNumber() {
        Calendar c = Calendar.getInstance();
        String crrDate = DateUtils.getDate(c.getTime(), DateTimeFormat.DATE_APP);
        firebaseDbInstance = FirebaseDatabase.getInstance();
        firebaseDbInstance.getReference(crrDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long val = (dataSnapshot.getValue() instanceof Long) ? (Long) dataSnapshot.getValue() : 1;
                if (digitalView != null) {
                    digitalView.setDigital(val);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logError("listenOrderNumber", databaseError.getMessage());
            }
        });
    }

    public static int REQUEST_CODE_PAYMENT = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == AppConst.REQUEST_CODE_REMINDER) {
            result.setSelection(5);
        }
        if (requestCode == AppConst.REQUEST_LOGIN_ACTIVITY) {
            resetHeader(MainActivity.this);
            if (CoreManager.getUser(MainActivity.this) != null) {
                logout = new PrimaryDrawerItem();
                logout.withName(R.string.logout_titile).withIcon(R.drawable.ic_power_settings_new_black_24dp).withIdentifier(8).withSelectable(false);
                result.addItem(logout);
            }
        }
    }

    static final int REQUEST_READ_PHONE_NUMBERS = 142;

    private void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_NUMBERS);
        } else {
            //TODO
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_NUMBERS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }


}
