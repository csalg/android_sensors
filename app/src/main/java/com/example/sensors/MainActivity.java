package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Sensor s;
    private List<Sensor> sensors;
    private SensorManager sm;

    private Map<String,String> sensorMap;

    private ListView lv = null;
    private SensorAdapter adapter = null;

    private class SensorAdapter extends ArrayAdapter<Sensor> {
        private TextView t1;
        private TextView t2;
        private Context context;
        private List<String> sensorNames;
        private List<String> sensorVals;

        public SensorAdapter(Context context, int resource, Map<String, String> sensorMap, List<Sensor> sensors) {
            super(context, resource, sensors);

            Collection<String> keys = sensorMap.keySet();
            Collection<String> vals = sensorMap.values();

            this.sensorNames = new ArrayList<>(keys);
            this.sensorVals = new ArrayList<>(vals);

            this.context = context;

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item, null);

            TextView name = (TextView) view.findViewById(R.id.itemTextView);
            name.setText(sensorNames.get(position));
//
            TextView data = (TextView) view.findViewById(R.id.itemTextView2);
            data.setText(sensorVals.get(position));

            return view;
        }

        public void updateData(Map<String,String> sensorMap){
            Collection<String> vals = sensorMap.values();
            this.sensorVals = new ArrayList<>(vals);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is sensor stuff
        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
//        if (sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){
//            s = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        }

        lv = (ListView) findViewById(R.id.listView1);

        // Let's populate with sensors!

        sensorMap = new HashMap<String,String>();

        sensors = sm.getSensorList(Sensor.TYPE_ALL);

        for (Sensor s : sensors){
            sensorMap.put(s.getName(), "");
            sm.registerListener(this, s, sm.SENSOR_DELAY_NORMAL);
            Log.v("Mapping:",s.getName());
        }


        adapter = new SensorAdapter(this, 0, sensorMap, sensors);

        lv.setAdapter((android.widget.ListAdapter) adapter);
        // This populates the ListView with dogs.
//        animals = getData(7);
//        adapter = new SimpleAdapter(MainActivity.this,
//                animals,
//                R.layout.item,
//                new String[]{"animal", "age"},
//                new int[]{R.id.itemTextView, R.id.itemTextView2}
//                );
//        lv.setAdapter((android.widget.ListAdapter) adapter);

    }
    public void onSensorChanged(SensorEvent event) {
        String name = event.sensor.getName();
        String val = "";

        for (float value : event.values){
            val += value + " ";
        }

        sensorMap.put(name, val);
        Log.v("New value", val);
        adapter.updateData(sensorMap);  //update adapter's data
        adapter.notifyDataSetChanged();
//        lv.setAdapter((android.widget.ListAdapter) adapter);

    };

    public void onAccuracyChanged(Sensor s, int i) {};

//    public List<Map<String,String>> getData(int n){
//        List<Map<String,String>> animals = new ArrayList<Map<String,String>>();
//
//        for(int i=0; i !=n; ++i){
//            Map<String,String> animal = new HashMap<String,String>();
//            animal.put("animal", "dog "+i);
//            animal.put("age", ""+i*7);
//            animals.add(animal);
//        }
//
//        return animals;
//    }

}
