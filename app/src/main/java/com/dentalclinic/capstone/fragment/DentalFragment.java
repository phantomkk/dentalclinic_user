package com.dentalclinic.capstone.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.RegisterActivity;
import com.dentalclinic.capstone.adapter.ServiceAdapter;
import com.dentalclinic.capstone.animation.AnimatedExpandableListView;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.services.TreatmentCategoryService;
import com.dentalclinic.capstone.models.Patient;
import com.dentalclinic.capstone.models.Treatment;
import com.dentalclinic.capstone.models.TreatmentCategory;
import com.dentalclinic.capstone.models.TreatmentHistory;
import com.dentalclinic.capstone.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DentalFragment extends BaseFragment implements MenuItem.OnActionExpandListener
        ,SearchView.OnQueryTextListener,SearchView.OnCloseListener
{


    public DentalFragment() {
        // Required empty public constructor
    }

    List<TreatmentCategory> treatmentCategories = new ArrayList<>();
//    HashMap<TreatmentCategory, List<Treatment>> listDataChild;

    ExpandableListView expandableListView;
    ServiceAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dental, container, false);
//        prepareData();
        showLoading();
        callApiGetAllTreatmentCategories();
        expandableListView = v.findViewById(R.id.eplv_list_categories);
        if(treatmentCategories==null){
//            prepareData();
        }
        adapter = new ServiceAdapter(getContext(), treatmentCategories);
        expandableListView.setAdapter(adapter);
        return v;
    }
    private void expandAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.expandGroup(i);
        }
    }
    private void colpanlAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.collapseGroup(i);
        }
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        adapter.filterData(s);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onClose() {
        adapter.filterData("");
        colpanlAll();
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }
    public void prepareData(){
        treatmentCategories = new ArrayList<>();
        TreatmentCategory treatmentCategory = new TreatmentCategory("NHA CHU","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/11.png");
        Treatment treatment = new Treatment("TRam RaNG COMPOSITE","tại nhà",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment2 = new Treatment("Tay TRaNG",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment3 = new Treatment("NHo RaNG CoI LoN HOaC RaNG KHoN HaM TReN",Long.parseLong("40000"),Long.parseLong("40000"));
        treatmentCategory.getTreatments().add(treatment);
        treatmentCategory.getTreatments().add(treatment2);
        treatmentCategory.getTreatments().add(treatment3);
        TreatmentCategory treatmentCategory2 = new TreatmentCategory("PHuC HiNH Co ĐiNH","https://nhakhoakim.com/wp-content/themes/Themesnhakhoaandong/assets/img/igray/12.png");
        Treatment treatment4 = new Treatment("TRaM RaNG COMPOSITE","tại nhà",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment5 = new Treatment("TaY TRaNG",Long.parseLong("40000"),Long.parseLong("40000"));
        Treatment treatment6 = new Treatment("NHo RaNG CoI LoN HOaC RaNG KHoN HaM TReN",Long.parseLong("40000"),Long.parseLong("40000"));
        treatmentCategory.getTreatments().add(treatment4);
        treatmentCategory.getTreatments().add(treatment5);
        treatmentCategory.getTreatments().add(treatment6);
        treatmentCategories.add(treatmentCategory);
        treatmentCategories.add(treatmentCategory);
        treatmentCategories.add(treatmentCategory);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }
    private TreatmentCategoryService treatmentCategory = APIServiceManager.getService(TreatmentCategoryService.class);
    private Disposable treatmentCategoriesServiceDisposable;

    public void callApiGetAllTreatmentCategories() {
        treatmentCategory.getAll().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<TreatmentCategory>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        hideLoading();
                        treatmentCategoriesServiceDisposable = d;
                    }
                    @Override
                    public void onSuccess(Response<List<TreatmentCategory>> listResponse) {
                        hideLoading();
                        if (listResponse.isSuccessful()) {
                            treatmentCategories.addAll(listResponse.body());
                            adapter.getListDataHeaderOriginal().addAll(treatmentCategories);
                            adapter.notifyDataSetChanged();
                            logError("treatmentCategories", String.valueOf(treatmentCategories.size()));
                        } else {
//                            String erroMsg = Utils.getErrorMsg(listResponse.errorBody());
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
//                                    .setMessage(erroMsg)
//                                    .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
//                                    }) ;
//                            alertDialog.show();
                        }

                    }
                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                        showMessage(getResources().getString(R.string.error_on_error_when_call_api));
//                        Toast.makeText(getContext(), getResources().getString(R.string.error_on_error_when_call_api), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
