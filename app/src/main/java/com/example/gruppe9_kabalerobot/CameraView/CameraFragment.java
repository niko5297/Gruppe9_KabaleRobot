package com.example.gruppe9_kabalerobot.CameraView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.gruppe9_kabalerobot.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class CameraFragment extends Fragment implements View.OnClickListener, CameraXConfig.Provider {

    private PreviewView previewView;
    private FloatingActionButton imageCaptureButton;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container,false);

        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        previewView = view.findViewById(R.id.preview);
        imageCaptureButton = view.findViewById(R.id.captureImage);
        imageCaptureButton.setOnClickListener(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getContext()));

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == imageCaptureButton) {
            imageCapture.takePicture(ContextCompat.getMainExecutor(getContext()), imageCapturedCallback);
        }
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() { return Camera2Config.defaultConfig(); }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {


        //FIXME: Jeg fjerner alle instancer af lifecycle, for at undgå crash, men dette forsinker programmet. Find anden løsning
        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder()
                .build();

        preview.setSurfaceProvider(previewView.getPreviewSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageCapture =
                new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build();

        OrientationEventListener orientationEventListener = new OrientationEventListener(getContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;

                // Monitors orientation values to determine the target rotation value
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation < 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation < 315) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }

                imageCapture.setTargetRotation(rotation);
            }
        };

        orientationEventListener.enable();

        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview);

    }

    private ImageCapture.OnImageCapturedCallback imageCapturedCallback = new ImageCapture.OnImageCapturedCallback() {
        @Override
        public void onCaptureSuccess(@NonNull ImageProxy image) {
            // Create new fragment and transaction
            Fragment newFragment = new ImageFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            // Set arguments

            Bitmap bitmap = toBitmapImage(image);
            Bundle args = new Bundle();
            args.putParcelable("path", bitmap);
            newFragment.setArguments(args);
            // Replace whatever is in the fragment_container view with this fragment,
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }

        @Override
        public void onError(@NonNull ImageCaptureException exception) {
            Toast.makeText(getActivity(), "Image Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private Bitmap toBitmapImage(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}