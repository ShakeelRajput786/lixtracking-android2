
package com.lixtracking.lt.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lixtracking.lt.R;
import com.lixtracking.lt.activities.VehicleDetailActivity;
import com.lixtracking.lt.adapters.ExpandableListAdapter;
import com.lixtracking.lt.common.Settings;
import com.lixtracking.lt.common.URL;
import com.lixtracking.lt.data_class.VehicleData;
import com.lixtracking.lt.parsers.ParceVehicles;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class FragmentAll extends Fragment{

    private int lastExpandedPosition = -1;
    private static boolean isRunning = false;
    private ProgressBar progressBar;
    ArrayList<HashMap<String, Object>> listObjects = null;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader=new ArrayList<String>();
    static Context context;
    Settings settings;
    ProgressDialog progressDialog;
    HashMap<String, List<String>> listDataChild=new HashMap<String,List<String>>();
    // Context context;
    private List<VehicleData>vehicleDataList = null;
    static String temp[]={"33.487007, -117.143784","41.653934, -81.450394","46.602070, -120.505898","28.018349, -82.764473","44.949642, -93.093124","47.380932, -122.234840"};
    public FragmentAll() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_all, container, false);
        this.context=getActivity().getApplicationContext();
        settings=new Settings(context);
        progressBar=new ProgressBar(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        // get the listview
        expListView = (ExpandableListView)view.findViewById(R.id.lvExpMain);

        // preparing list data
        //prepareListData();

        // listAdapter = new ExpandableListAdapter(getActivity().getBaseContext(), listDataHeader, listDataChild);

        // setting list adapter
        // expListView.setAdapter(listAdapter);
        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

  /*      // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });*/

        // Listview Group collasped listener
//        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub




                       String vin = ((TextView)v.findViewById(R.id.tvVin)).getText().toString();





                        VehicleData data = null;
                        for (int i = 0; i<vehicleDataList.size(); i++) {
                            String temp=vehicleDataList.get(i).vin;

                            if(vin.equalsIgnoreCase(vehicleDataList.get(i).vin)){
                                data = vehicleDataList.get(i);
                            }


                        }
                        if(data != null) {
                            Intent intent = new Intent(context,VehicleDetailActivity.class);
                            intent.putExtra(VehicleData.VIN, data.vin);
                            intent.putExtra(VehicleData.GPS_ID, data.gps_id);
                            intent.putExtra(VehicleData.USER_ID, data.user_id);
                            intent.putExtra(VehicleData.FIRST_NAME, data.first_name);
                            intent.putExtra(VehicleData.LAST_NAME, data.last_name);
                            intent.putExtra(VehicleData.MAKE, data.make);
                            intent.putExtra(VehicleData.MODEL, data.model);
                            intent.putExtra(VehicleData.STOCK_NUMBER, data.stock_number);
                            intent.putExtra(VehicleData.YEAR, data.year);
                            intent.putExtra(VehicleData.STATUS, data.status);
                            intent.putExtra(VehicleData.VEHICLE_IDENTITY, data.vehicleIdentity);
                            intent.putExtra(VehicleData.SPEED, data.speed);
                            startActivity(intent);
                        }

                return false;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });


        return view;


    }
    public void onResume() {
        if(vehicleDataList==null){
            new getVehiclesTask().execute();
        }
        else{
            progressDialog.hide();
            listAdapter = new ExpandableListAdapter(getActivity().getBaseContext(), listDataHeader, listDataChild);



            expListView.setAdapter(listAdapter);
            expListView.invalidate();
        }


        super.onResume();
    }


    class getVehiclesTask extends AsyncTask<Void, Void, String> {
        private String resultString = null;
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);


        }
        @Override
        protected String doInBackground(Void... voids) {
            if (isRunning == true)
                return null;
            isRunning = true;
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
            HttpConnectionParams.setSoTimeout(httpParams, 10000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(URL.getVehiclesUrl);

            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(1);
            nameValuePairList.add(new BasicNameValuePair("user_id", new Settings(context).getUserId()));
            nameValuePairList.add(new BasicNameValuePair("RoleID", new Settings(context).getUserRoleId()));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                resultString = EntityUtils.toString(httpEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultString;
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            if(result == null && listObjects == null) {
                /*if(((MainActivity)getActivity()).getCurrentFragmentTag() == MainActivity.TAB_HOME) {
                    result = "Error connection";
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error");
                    builder.setMessage(result);
                    builder.setIcon(R.drawable.ic_action_warning_dark);
                    builder.setCancelable(true);
                    builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }*/
                progressDialog.hide();
                progressBar.setVisibility(View.INVISIBLE);
                isRunning = false;
                return;
            }else if(result == null && listObjects != null) {
                //new String[]{ICON,ID,NAME,GPS_ID,STOCK_NUMBER}
                progressDialog.hide();
                progressBar.setVisibility(View.INVISIBLE);
                isRunning = false;
                return;
            }

            //listObjects = new ArrayList<HashMap<String, Object>>();
            vehicleDataList = new ParceVehicles(context).parceXml(result);
            listDataHeader.clear();
            listDataChild.clear();

            String groupBy=settings.getGroupBy();
            if(vehicleDataList != null && (!vehicleDataList.isEmpty())) {{
                List<String> tempList = new ArrayList<String>();
                for(int b=0;b<vehicleDataList.size();b++){
                    VehicleData tempVehicleData = vehicleDataList.get(b);
                    String data="";
                    switch (groupBy) {
                        case "Year":
                            data = String.valueOf(tempVehicleData.year);break;
                        case "City":
                            //data = String.valueOf(tempVehicleData.year);break;
                            data=getCity(tempVehicleData.year);break;
                        case "Model":
                            data = String.valueOf(tempVehicleData.model);break;
                        case "Make":
                            data = String.valueOf(tempVehicleData.make);break;

                    }
                    if(!listDataHeader.contains(data))
                        listDataHeader.add(data);
                }

                for(int c=0;c<listDataHeader.size();c++) {

                    for (int a = 0; a <vehicleDataList.size(); a++) {

                        VehicleData tempVehicleData = vehicleDataList.get(a);

                        switch (groupBy) {
                            case "Year":
                            if (listDataHeader.get(c).equals(String.valueOf(tempVehicleData.year))) {
                                String tempData = tempVehicleData.gps_id + "#:#" + tempVehicleData.vehicleIdentity + "#:#" + tempVehicleData.speed + "#:#" + tempVehicleData.status + "#:#" + tempVehicleData.vin;
                                tempList.add(tempData);
                            }
                            break;
                            case "City":
                                if (listDataHeader.get(c).equals(getCity(tempVehicleData.year))) {
                                    String tempData = tempVehicleData.gps_id + "#:#" + tempVehicleData.vehicleIdentity + "#:#" + tempVehicleData.speed + "#:#" + tempVehicleData.status + "#:#" + tempVehicleData.vin;
                                    tempList.add(tempData);
                                }
                                break;
                            case "Model":
                                if (listDataHeader.get(c).equals(String.valueOf(tempVehicleData.model))) {
                                    String tempData = tempVehicleData.gps_id + "#:#" + tempVehicleData.vehicleIdentity + "#:#" + tempVehicleData.speed + "#:#" + tempVehicleData.status + "#:#" + tempVehicleData.vin;
                                    tempList.add(tempData);
                                }
                                break;
                            case "Make":
                                if (listDataHeader.get(c).equals(String.valueOf(tempVehicleData.make))) {
                                    String tempData = tempVehicleData.gps_id + "#:#" + tempVehicleData.vehicleIdentity + "#:#" + tempVehicleData.speed + "#:#" + tempVehicleData.status + "#:#" + tempVehicleData.vin;
                                    tempList.add(tempData);
                                }
                                break;
                        }




                    }

                    listDataChild.put(listDataHeader.get(c), tempList);

                    tempList = new ArrayList<>();
                }

                listAdapter = new ExpandableListAdapter(getActivity().getBaseContext(), listDataHeader, listDataChild);



                expListView.setAdapter(listAdapter);
                expListView.invalidate();
                //Toast.makeText(context, vehicleDataList.get(0).gps_id, Toast.LENGTH_SHORT).show();
            }}
            progressBar.setVisibility(View.INVISIBLE);
            isRunning = false;
            progressDialog.hide();
            //   listAdapter = new ExpandableListAdapter(getActivity().getBaseContext(), listDataHeader, listDataChild);

            // setting list adapter
            // expListView.setAdapter(listAdapter);
            // expListView.invalidate();

        }
    }
    private static String getCity(int year) {
        String cityName = "NOT FOUND";

        String address=temp[year%6];
        String dump[]=address.split(",");

        double lati=Double.parseDouble(dump[0]);
        double longi=Double.parseDouble(dump[1]);
        if(Geocoder.isPresent()) {
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            try {

                List<Address> addresses = gcd.getFromLocation(lati, longi, 1);
                if (addresses.size() > 0) {

                    cityName = addresses.get(0).getAddressLine(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            cityName="GeoCoder fail";
        }


        return cityName;

    }

}
