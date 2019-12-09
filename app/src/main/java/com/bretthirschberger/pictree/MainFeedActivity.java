package com.bretthirschberger.pictree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import com.bretthirschberger.pictree.ui.home.HomeFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.SparseIntArray;
import android.view.Menu;
import android.view.Surface;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class MainFeedActivity extends AppCompatActivity {

    private HomeFragment mHomeFragment;

    private AppBarConfiguration mAppBarConfiguration;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String POSTS_REFERENCE = "posts";
    private static final String USER_REFERENCE = "user";
    private String mCurrentPhotoPath;
    private StorageReference mStorageRef;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mPostsReference;
    private DatabaseReference mUserReference;
    private Uri mPhotoUri;
    private PictureDatabaseHandler mPictureDatabaseHandler;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mDatabase = FirebaseDatabase.getInstance();
        mPostsReference = mDatabase.getReference(POSTS_REFERENCE);
        mUserReference = mDatabase.getReference(USER_REFERENCE);
        mPictureDatabaseHandler = new PictureDatabaseHandler(this, null);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> dispatchTakePictureIntent());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_posts)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            uploadPostToStorage(mPhotoUri);
            getDescription(mPhotoUri);
            findObjectsInImage(mPhotoUri);
//            try {
//                mPictureDatabaseHandler.addPost(MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPhotoUri));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "IO Exception", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mPhotoUri = FileProvider.getUriForFile(this,
                        "com.bretthirschberger.pictree.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            } else {
                Toast.makeText(getApplicationContext(), "File Null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("Storage Dir", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadPost(Uri imageUri, String description) {
        if (imageUri != null) {
            String uploadId = mPostsReference.push().getKey();
            mPostsReference.child(uploadId).setValue(new Post(1, imageUri.toString(), description, User.getCurrentUser(), uploadId));
            mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUserReference.child(User.getCurrentUser().getUserId()).child("rootAmt").setValue(dataSnapshot.child(User.getCurrentUser().getUserId()).getValue(User.class).getRootAmt() + 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPostToStorage(Uri photoUri, String description) {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef.child("posts/" + userID + "/" + System.currentTimeMillis() + ".jpg").putFile(photoUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get a URL to the uploaded content
                    Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    downloadUrl.addOnSuccessListener(uri -> uploadPost(downloadUrl.getResult(), description));

                })
                .addOnFailureListener(exception -> exception.printStackTrace());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_feed, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void findObjectsInImage(Uri uri) {
        FirebaseVisionObjectDetectorOptions options =
                new FirebaseVisionObjectDetectorOptions.Builder()
                        .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()  // Optional
                        .build();
        FirebaseVisionObjectDetector objectDetector =
                FirebaseVision.getInstance().getOnDeviceObjectDetector(options);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(), true);
        Bitmap finalBitmap = bitmap;
        objectDetector.processImage(image)
                .addOnSuccessListener(detectedObjects -> {
                    Canvas canvas = new Canvas(bitmapCopy);
                    Paint rectPaint = new Paint();
                    Paint textPaint = new Paint();
                    textPaint.setTextSize(100);
                    textPaint.setColor(Color.RED);
//                    textPaint.
                    rectPaint.setStyle(Paint.Style.STROKE);
                    rectPaint.setStrokeWidth(20);
                    rectPaint.setColor(Color.RED);
                    for (FirebaseVisionObject visionObject : detectedObjects) {
                        canvas.drawRect(visionObject.getBoundingBox(), rectPaint);
                        String category;
                        switch (visionObject.getClassificationCategory()) {
                            case FirebaseVisionObject.CATEGORY_FASHION_GOOD:
                                category = "Fashion Good";
                                break;
                            case FirebaseVisionObject.CATEGORY_FOOD:
                                category = "Food";
                                break;
                            case FirebaseVisionObject.CATEGORY_HOME_GOOD:
                                category = "Home Good";
                                break;
                            case FirebaseVisionObject.CATEGORY_PLACE:
                                category = "Place";
                                break;
                            case FirebaseVisionObject.CATEGORY_PLANT:
                                category = "Plant";
                                break;
                            default:
                                category = "Unknown";

                        }
                        canvas.drawText("Category: " + category +
                                        ", Confidence: " + visionObject.getClassificationConfidence(),
                                visionObject.getBoundingBox().left,
                                visionObject.getBoundingBox().top,
                                textPaint);
                    }
                    Log.i("Objects Found", detectedObjects.size() + "");
                    String file_path = getFilesDir().getAbsolutePath() +
                            "/PicTree";
                    File dir = new File(file_path);
                    if (!dir.exists())
                        dir.mkdirs();
                    File file = new File(dir, "pictree" + LocalDateTime.now().toString().replace('.', '-') + ".png");
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    bitmapCopy.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPictureDatabaseHandler.addPost(finalBitmap, Uri.fromFile(file).toString());
                }).addOnFailureListener(e -> e.printStackTrace());


    }

    private void getDescription(Uri uri) {
        FirebaseVisionImage image = null;
        final String[] description = {""};
        try {
            image = FirebaseVisionImage.fromBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Multiple object detection in static images
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getCloudImageLabeler();
        labeler.processImage(image)
                .addOnSuccessListener(labels -> {
//                    String description = "";
                    for (FirebaseVisionImageLabel label : labels) {
                        Log.i("Detected Objects", label.getText());
                        description[0] += label.getText() + " ";
                    }
                    uploadPostToStorage(mPhotoUri, description[0]);
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }
}
