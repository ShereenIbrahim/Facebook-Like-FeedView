package info.androidhive.listviewfeed;

import info.androidhive.listviewfeed.adapter.FeedListAdapter;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.Feeds;
import info.androidhive.listviewfeed.data.UserProfile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<Feeds> feedItems;
	//private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	private String URL_FEED = "http://192.168.43.197:8084/ElmoezWebService/services/elmoez/feeds";
	final static String UPLOAD_URL="http://192.168.43.197:8084/ElmoezWebService/services/elmoez/image";
	private ProgressDialog progressBar;
    private EditText feedMsg;
    private ImageButton postFeed;
    private String userImage;
	private ImageButton imagePost;

    final static  int CAPTURE_IMAGE = 208;
    final static  int PICK_IMAGE = 209;
    private Bitmap bitmap;
    private Uri filePath;
    //create dialog
    private Dialog dialog ;
    private ImageView postImageView;
    private LinearLayout linearLayoutPostImage;
    private Button acceptPostImage;
    private Button cancelPostImage;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
        feedMsg = (EditText) findViewById(R.id.writeFeed);
        postFeed = (ImageButton) findViewById(R.id.post);

        linearLayoutPostImage = (LinearLayout) View.inflate(this, R.layout.activity_post_image, null);
        linearLayoutPostImage.setBackgroundColor(Color.GRAY);
        // Gets a reference to the postImageView
        postImageView = (ImageView) linearLayoutPostImage.findViewById(R.id.postImageView);
        acceptPostImage = (Button) linearLayoutPostImage.findViewById(R.id.acceptPostImage);
        cancelPostImage = (Button) linearLayoutPostImage.findViewById(R.id.cancelPostImage);


        //create dialog
//        postImageView= (ImageView) findViewById(R.id.postImageView);
        dialog = new Dialog(this);//for show information about place
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(linearLayoutPostImage);
//        dialog.setTitle("Confirmation");
//        dialog.getWindow().setLayout(700,700);
		feedItems = new ArrayList<>();

		imagePost = (ImageButton) findViewById(R.id.imagePost);
		listAdapter = new FeedListAdapter(this, feedItems);
		listView.setAdapter(listAdapter);

		
		// These two lines not needed,
		// just to get the look of facebook (changing background color & hiding the icon)
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#108070")));
		getActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		// We first check for cached request
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONArray(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			// making fresh volley request and getting json
//			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
//					URL_FEED, null, new Response.Listener<JSONObject>() {
//
//						@Override
//						public void onResponse(JSONObject response) {
//							VolleyLog.d(TAG, "Response: " + response.toString());
//							if (response != null) {
//								parseJsonFeed(response);
//							}
//						}
//					}, new Response.ErrorListener() {
//
//						@Override
//						public void onErrorResponse(VolleyError error) {
//							VolleyLog.d(TAG, "Error: " + error.getMessage());
//						}
//					});
//
//			// Adding request to volley request queue
//			AppController.getInstance().addToRequestQueue(jsonReq);
			progressBar=ProgressDialog.show(MainActivity.this,"Processing","Please Wait",true,false);
			getGsonArray();

		}

        postFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feed = feedMsg.getText().toString();
                if(!feed.equals("")||bitmap!=null) {
                    final Feeds newFeed = new Feeds();
                    newFeed.setUserName("ayad");
                    newFeed.setUserImage(userImage);
                    if(!feed.equals("")&&bitmap==null){

//                    final Feeds newFeed = new Feeds();
//                    newFeed.setUserName("ayad");
                        newFeed.setFeed(feed);
                        Log.e("yes","only feed");
//                    newFeed.setUserImage(userImage);
//                    listView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            feedItems.add(0, newFeed);
//                            listAdapter.notifyDataSetChanged();
//                            listView.smoothScrollToPosition(0);
//                        }
//                    });

                    }else if(bitmap!=null&&feed.equals("")){
                        newFeed.setOfflineImage(bitmap);
                        Log.e("yes","only bitmap");


                    }else{
                        newFeed.setFeed(feed);
                        newFeed.setOfflineImage(bitmap);
                        Log.e("yes","only both");


                    }
                    feedItems.add(0,newFeed);
                    listAdapter.notifyDataSetChanged();
                    feedMsg.setText("");
					uploadPicture();
                }else {
                    Toast.makeText(getApplicationContext(),"please you must write something",Toast.LENGTH_LONG).show();

                }
            }
        });


		//		create image multi choose
		imagePost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				//Creating the instance of PopupMenu
				PopupMenu popup = new PopupMenu(MainActivity.this, imagePost);
				//Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

				//registering popup with OnMenuItemClickListener

				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()){

							case R.id.Camera:
								openCamera();

								break;
							case R.id.Gallery:

								showFileChooser();
								break;
							case R.id.Remove:
								removeImage();

								Toast.makeText(MainActivity.this, "You Clicked : " + "threeee", Toast.LENGTH_SHORT).show();
								break;


						}
//
//                        Toast.makeText(Profile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
						return true;
					}
				});
				try {
					Class<?> classPopupMenu = Class.forName(popup.getClass().getName());
					Field mPopup = classPopupMenu.getDeclaredField("mPopup");
					mPopup.setAccessible(true);
					Object menuPopupHelper = mPopup.get(popup);
					Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
					java.lang.reflect.Method setForceIcons = classPopupHelper.getMethod(
							"setForceShowIcon", boolean.class);
					setForceIcons.invoke(menuPopupHelper, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				popup.show(); //showing popup menu

			}
		});
//		handle post Image
//		accept post image

		acceptPostImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(),"accept image",Toast.LENGTH_LONG).show();

			}
		});

//		refuse post image
		cancelPostImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				bitmap=null;
				dialog.dismiss();
				Toast.makeText(getApplicationContext(),"cancel image",Toast.LENGTH_LONG).show();


			}
		});

	}


	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private void parseJsonFeed(JSONArray response) {
		try {

			for (int i = 0; i < response.length(); i++) {
				try {
					JSONObject jo = response.getJSONObject(i);
//								String name = jo.getString("monumentsName");
					Feeds item = new Feeds();


					item.setUserName(jo.getString("userName"));
                    userImage=jo.getString("userImage");
					item.setUserImage(userImage);

					// Image might be null sometimes
					String image = jo.isNull("image") ? null : jo.getString("image");
					item.setFeedImage(image);
					item.setFeed(jo.getString("feed"));


					feedItems.add(item);

//								Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			progressBar.dismiss();

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



//	test

	public void getGsonArray(){
		JsonArrayRequest jreq = new JsonArrayRequest(Request.Method.GET,URL_FEED,null,
				new Response.Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {

						parseJsonFeed(response);




					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
		jreq.setTag(TAG);
//		MySingleton.getInstance(this).addToRequestQueue(jreq);
		AppController.getInstance().addToRequestQueue(jreq);



	}


    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAPTURE_IMAGE);

    }

    private void showFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    private void removeImage() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
//						deleteUserDataFromServer();
//
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){

            case PICK_IMAGE:
                if(resultCode==RESULT_OK){


                    filePath = data.getData();
                    Toast.makeText(MainActivity.this, filePath.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URL ", "success" + filePath.toString());
                    try {
                        //Getting the Bitmap from Gallery
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

//						reduce size of image
						bitmap = getResizedBitmap(bitmap,500,500);

                        //Setting the Bitmap to ImageView
                        postImageView.setImageBitmap(bitmap);
                        dialog.show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                uploadPicture();

                break;


            case CAPTURE_IMAGE:

                try {


                    bitmap = (Bitmap) data.getExtras().get("data");
//					reduce size of image
					bitmap = getResizedBitmap(bitmap,500,500);

                    postImageView.setImageBitmap(bitmap);
                    dialog.show();
                    filePath = data.getData();
                    Toast.makeText(MainActivity.this, filePath.toString(), Toast.LENGTH_LONG).show();
                    Log.e("URL ", "success" + filePath.toString());


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

//                uploadPicture();

                break;
            default:
                break;
        }
    }


	/** getResizedBitmap method is used to Resized the Image according to custom width and height
	 * @param image
	 * @param newHeight (new desired height)
	 * @param newWidth (new desired Width)
	 * @return image (new resized image)
	 * */
	public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
		int width = image.getWidth();
		int height = image.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	/**
	 * reduces the size of the image
	 * @param image
	 * @param maxSize
	 * @return
	 */
	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float)width / (float) height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

//	upload feed component
public void uploadPicture() {
	String path=PathValue.getPath(getApplicationContext(), filePath);
	if(path!=null) {

		final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
		Ion.with(getApplicationContext())
				.load(UPLOAD_URL).setLogging("UPLOAD LOGS", Log.DEBUG)
				.setMultipartParameter("email", "c.ayad-2010@yahoo.com")
				.setMultipartFile("file", "application/zip", new File(path)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
			@Override
			public void onCompleted(Exception e, JsonObject result) {
				if (e != null) {
					e.printStackTrace();
				}
				loading.dismiss();
				Log.i("completed", "completed");

				if (result != null) {

					Log.i("success", result.get("message").getAsString());

					loading.dismiss();

				}
			}
		});
	}


	else{
		Log.i("result of imageeeeeee", "nullllll");




	}
}

	private void callback(){

		Log.i("CallBack", "i was called");
	}
}
