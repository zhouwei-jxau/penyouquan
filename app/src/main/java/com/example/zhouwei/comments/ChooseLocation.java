package com.example.zhouwei.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapActivity;
import com.tencent.tencentmap.mapsdk.map.MapView;

public class ChooseLocation extends MapActivity implements TencentLocationListener,HttpResponseListener {
    private TencentLocationManager tencentLocationManager;
    private RadioButton radioButton_location_current;
    private RadioButton radioButton_location_none;
    private RadioButton radioButton_location_map;
    private String currentMapLocation="";
    private String localPosition="";
    private MapView mapView;
    private boolean located=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        Context context=this;
        TencentLocationListener tencentLocationListener=this;
        TencentLocationRequest tencentLocationRequest=TencentLocationRequest.create();
        tencentLocationManager= TencentLocationManager.getInstance(context);
        int error=tencentLocationManager.requestLocationUpdates(tencentLocationRequest,tencentLocationListener);

        mapView=(MapView)findViewById(R.id.mapview);
        radioButton_location_current=(RadioButton) findViewById(R.id.radioButton_location_current);
        radioButton_location_map=(RadioButton) findViewById(R.id.radioButton_location_map);
        radioButton_location_none=(RadioButton)findViewById(R.id.radioButton_location_none);


        Button button_comfirm=(Button)findViewById(R.id.confirm);
        button_comfirm.setOnClickListener(v->{
            if(radioButton_location_none.isChecked())
            {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("location","");
                intent.putExtras(bundle);
                setResult(write.CHOOSELOCATIONREQUESETCODE,intent);
                this.finish();
            }
            else if(radioButton_location_map.isChecked())
            {
                LatLng latLng =mapView.getMap().getMapCenter();;
                Geo2AddressParam  geo2AddressParam=new Geo2AddressParam();
                geo2AddressParam.location(new Location((float)latLng.getLatitude(),(float)latLng.getLongitude()));
                TencentSearch tencentSearch=new TencentSearch(getApplicationContext());
                tencentSearch.geo2address(geo2AddressParam,this);
            }
            else
            {
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("location",localPosition);
                intent.putExtras(bundle);
                setResult(write.CHOOSELOCATIONREQUESETCODE,intent);
                this.finish();
            }
            tencentLocationManager.removeUpdates(this);
        });

        Button button_cancel=(Button)findViewById(R.id.cancel);
        button_cancel.setOnClickListener(v->{
            tencentLocationManager.removeUpdates(this);
            this.finish();
        });
        return;
    }


    //监听位置变化
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if(i==TencentLocation.ERROR_OK)
        {
            if(tencentLocation.getAddress()==null||tencentLocation.getAddress().equals("Unknown"))
            {
                radioButton_location_current.setText("当前位置:无法识别");
            }
            else if(located==false)
            {
                radioButton_location_current.setText("当前位置:"+tencentLocation.getAddress());
                double lat=tencentLocation.getLatitude();
                double lng=tencentLocation.getLongitude();
                LatLng latLng =new LatLng(lat,lng);
                mapView.getMap().setCenter(latLng);
                currentMapLocation=tencentLocation.getAddress();
                localPosition=tencentLocation.getAddress();
                located=true;
            }
        }
    }
    //监听状态变化
    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Override
    public void onSuccess(int i, BaseObject baseObject) {
        String location=((Geo2AddressResultObject)baseObject).result.address;
        if(location==null||location.equals("Unknown"))
        {
            currentMapLocation=location;
            radioButton_location_map.setText("使用地图中的位置:无法识别");
        }
        else
        {
            currentMapLocation=location;
            radioButton_location_map.setText("使用地图中的位置:"+location);
        }
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("location",this.currentMapLocation);
        intent.putExtras(bundle);
        setResult(-1,intent);
        this.finish();
        return;
    }

    @Override
    public void onFailure(int i, String s, Throwable throwable) {
        currentMapLocation="";
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString("location",this.currentMapLocation);
        intent.putExtras(bundle);
        setResult(-1,intent);
        this.finish();
        return;
    }
}
