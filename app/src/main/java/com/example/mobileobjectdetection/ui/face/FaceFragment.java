package com.example.mobileobjectdetection.ui.face;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileobjectdetection.MainActivity;
import com.example.mobileobjectdetection.R;
import com.example.mobileobjectdetection.SCRFDNcnn;

public class FaceFragment extends Fragment {

    public static final int REQUEST_CAMERA = 100;

    private SCRFDNcnn scrfdncnn = new SCRFDNcnn();
    private int facing = 0;

    private Spinner spinnerModel;
    private Spinner spinnerCPUGPU;
    private int current_model = 0;
    private int current_cpugpu = 0;

    private SurfaceView cameraView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FaceViewModel faceViewModel =
                new ViewModelProvider(this).get(FaceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_detection, container, false);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cameraView = (SurfaceView) root.findViewById(R.id.surfaceFaceCamView);

        cameraView.getHolder().setFormat(PixelFormat.RGBA_8888);
        //FIXME the callback bugs here
//        cameraView.getHolder().addCallback(this);

        Button buttonSwitchCamera = (Button) root.findViewById(R.id.buttonFaceSwitchCamera);
        buttonSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                int new_facing = 1 - facing;

                scrfdncnn.closeCamera();

                scrfdncnn.openCamera(new_facing);

                facing = new_facing;
            }
        });

        spinnerModel = (Spinner) root.findViewById(R.id.spinnerFaceModel);
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
            {
                if (position != current_model)
                {
                    current_model = position;
                    reload();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });

        spinnerCPUGPU = (Spinner) root.findViewById(R.id.spinnerFaceCPUGPU);
        spinnerCPUGPU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
            {
                if (position != current_cpugpu)
                {
                    current_cpugpu = position;
                    reload();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });

        reload();


        return root;
    }

    private void reload()
    {
        boolean ret_init = scrfdncnn.loadModel(getResources().getAssets(), current_model, current_cpugpu);
        if (!ret_init)
        {
            Log.e("MainActivity", "scrfdncnn loadModel failed");
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        scrfdncnn.setOutputWindow(holder.getSurface());
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }

        scrfdncnn.openCamera(facing);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        scrfdncnn.closeCamera();
    }

}