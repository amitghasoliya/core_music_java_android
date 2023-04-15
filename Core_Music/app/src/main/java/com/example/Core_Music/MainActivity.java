package com.example.Core_Music;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        //getting external storage permission
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

//                        Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();

                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
//                        Log.d("user","size"+mySongs.size());

                        String[] items = new String[mySongs.size()];
                        for(int i=0;i<mySongs.size();i++){
                            items[i] = mySongs.get(i).getName().replace(".mp3", "");
//                            Log.d("user","size"+items[i]);
                        }
//
                        ArrayAdapter adapter = new ArrayAdapter(
                                MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1, items);
//                        ArrayAdapter ad=new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,data);
//                        listVar.setAdapter(ad);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(MainActivity.this,Play_Music.class);
                                String currentSong = listView.getItemAtPosition(i).toString();
                                intent.putExtra("songlist",mySongs);
                                intent.putExtra("currentSong",currentSong);
                                intent.putExtra("position",i);
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> fetchSongs(File file){
        ArrayList arrayList = new ArrayList();
        File [] songs = file.listFiles();
        if(songs !=null){
            for(File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith(".")){
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }
}