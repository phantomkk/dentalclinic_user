package com.dentalclinic.capstone.activities;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import com.dentalclinic.capstone.fragment.AppointmentFragment;
import com.dentalclinic.capstone.fragment.DentalFragment;
import com.dentalclinic.capstone.fragment.HistoryAppointmentFragment;
import com.dentalclinic.capstone.fragment.HistoryFragment;
import com.dentalclinic.capstone.fragment.HistoryPaymentFragment;
import com.dentalclinic.capstone.fragment.HistoryTreatmentFragment;
import com.dentalclinic.capstone.fragment.MyAccoutFragment;
import com.dentalclinic.capstone.fragment.NewsFragment;
import com.dentalclinic.capstone.fragment.PromotionFragment;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Staff;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentDetail;
import com.dentalclinic.capstone.models.User;
import com.dentalclinic.capstone.utils.CoreManager;
import com.dentalclinic.capstone.utils.DateTimeFormat;
import com.dentalclinic.capstone.utils.DateUtils;
import com.dentalclinic.capstone.view.DigitalView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.rupins.drawercardbehaviour.CardDrawerLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager = getSupportFragmentManager();
    private CardDrawerLayout drawer;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private TextView currentDate;
    private static final int PROFILE_SETTING = 100000;
    private long elapsedTime = 0;
    private Button button;
    private DigitalView digitalView;
    private Handler handler;
    private FirebaseDatabase firebaseDbInstance;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.new_fragment_title));
        NewsFragment newFragment = new NewsFragment();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, newFragment).commit();
        button = findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TreatmentDetail detail = new TreatmentDetail();
                detail.setDentist(new Staff("Vo Quoc Trinh","https://thumbs.dreamstime.com/b/dentist-avatar-flat-icon-isolated-white-series-caucasian-blue-coat-background-eps-file-available-95672861.jpg"));
                detail.setCreatedDate(new Date());
                detail.setTreatment(new Treatment("Tram Rang"));
                showFeedbackDialog(detail);
            }
        });

        digitalView = findViewById(R.id.digital);
        currentDate = findViewById(R.id.txt_date);
//        Date currentTime = Calendar.getInstance().getTime();
//        logError("Date",currentTime.toString());
        currentDate.setText(DateUtils.getCurrentDateFormat());
        listView = findViewById(R.id.lv_appointment_list);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
////        drawer.setViewScale(Gravity.START, 0.9f);
////        drawer.setRadius(Gravity.START, 35);
////        drawer.setViewElevation(Gravity.START, 20);
//        navigationView.getMenu().getItem(0).setChecked(true);

//        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);

        //new code
        User user = CoreManager.getUser(this);
        List<IProfile> listIprofile = new ArrayList<>();
        if (user != null) {
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
        }
        listIprofile.add(new ProfileSettingDrawerItem().withName("Thêm Bệnh Nhân").withDescription("Tạo Mới Một Bệnh Nhân").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING));

        listIprofile.add(new ProfileSettingDrawerItem().withName("Quản Lý Bệnh Nhân").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001));
        IProfile[] arrayIProfile = new IProfile[listIprofile.size()];
        listIprofile.toArray(arrayIProfile);
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header2)
//                .addProfiles((IProfile) (listIprofile.toArray()))
                .addProfiles(arrayIProfile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
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
//                        new PrimaryDrawerItem().withName(R.string.history_treatment_fragment_title).withIcon(R.drawable.ic_history_black_24dp).withIdentifier(6).withSelectable(true),
//                        new PrimaryDrawerItem().withName(R.string.history_payment_fragment_title).withIcon(R.drawable.ic_payment_black_24dp).withIdentifier(7).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.logout_titile).withIcon(R.drawable.ic_power_settings_new_black_24dp).withIdentifier(8).withSelectable(false)
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom_container_drawer).withDescription(R.string.drawer_item_custom_container_drawer_desc).withIcon(R.drawable.dental_icon).withIdentifier(9).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_menu_drawer).withDescription(R.string.drawer_item_menu_drawer_desc).withIcon(R.drawable.dental_icon).withIdentifier(10).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_mini_drawer).withDescription(R.string.drawer_item_mini_drawer_desc).withIcon(R.drawable.dental_icon).withIdentifier(11).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon(R.drawable.dental_icon).withIdentifier(12).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_collapsing_toolbar_drawer).withDescription(R.string.drawer_item_collapsing_toolbar_drawer_desc).withIcon(R.drawable.dental_icon).withIdentifier(13).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_persistent_compact_header).withDescription(R.string.drawer_item_persistent_compact_header_desc).withIcon(R.drawable.dental_icon).withIdentifier(14).withSelectable(false),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_crossfade_drawer_layout_drawer).withDescription(R.string.drawer_item_crossfade_drawer_layout_drawer_desc).withIcon(R.drawable.dental_icon).withIdentifier(15).withSelectable(false),
//
//                        new ExpandableBadgeDrawerItem().withName("Collapsable Badge").withIcon(R.drawable.dental_icon).withIdentifier(18).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge("100").withSubItems(
//                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(R.drawable.dental_icon).withIdentifier(2000),
//                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(R.drawable.dental_icon).withIdentifier(2001)
//                        ),
//                        new ExpandableDrawerItem().withName("Collapsable").withIcon(R.drawable.dental_icon).withIdentifier(19).withSelectable(false).withSubItems(
//                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(R.drawable.dental_icon).withIdentifier(2002),
//                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(R.drawable.dental_icon).withIdentifier(2003)
//                        ),
//                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(R.drawable.dental_icon).withIdentifier(20).withSelectable(false),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(R.drawable.dental_icon).withIdentifier(21).withTag("Bullhorn")
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

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
                                MyAccoutFragment myAccoutFragment = new MyAccoutFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_fragment, myAccoutFragment).commit();
                            } else if (drawerItem.getIdentifier() == 5) {

                                setTitle(getResources().getString(R.string.histrory_fragment_title));
                                HistoryFragment dentalFragment = new HistoryFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
                            } else if (drawerItem.getIdentifier() == 6) {
                                setTitle(getResources().getString(R.string.history_treatment_fragment_title));
                                HistoryTreatmentFragment dentalFragment = new HistoryTreatmentFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_fragment, dentalFragment).commit();
                            } else if (drawerItem.getIdentifier() == 7) {
                                setTitle(getResources().getString(R.string.history_payment_fragment_title));
                                HistoryPaymentFragment historyPaymentFragment = new HistoryPaymentFragment();
                                fragmentManager.beginTransaction().replace(R.id.main_fragment, historyPaymentFragment).commit();
                            } else {
                                showMessage("Đăng xuất");
                                CoreManager.clearUser(MainActivity.this);
                            }
                        }
//
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
            headerResult.setActiveProfile(arrayIProfile[0]);
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
        result.updateBadge(5, new StringHolder(1 + ""));
        result.setSelectionAtPosition(1);

        listenOrderNumber();

    }
    ///End oncreated

    @Override
    public String getMainTitle() {
        return null;
    }

    @Override
    public void onCancelLoading() {

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
        AutoCompleteTextView  contentFeedback = dialog.findViewById(R.id.edit_content);
        Button button = dialog.findViewById(R.id.btn_submit_feedback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if(treatmentDetail.getDentist().getName()!=null){
            txtName.setText(treatmentDetail.getDentist().getName());
        }
        String result = "";
        if(treatmentDetail.getTreatment().getName()!=null){
            result += treatmentDetail.getTreatment().getName();
        }
        if(treatmentDetail.getCreatedDate()!=null){
            result+=treatmentDetail.getCreatedDate();
        }
        if(!result.isEmpty()){
            txtTreatmentContent.setText(result);
        }
        if(treatmentDetail.getDentist().getAvatar()!=null){
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
            showMessage("Đăng xuất");
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


}
