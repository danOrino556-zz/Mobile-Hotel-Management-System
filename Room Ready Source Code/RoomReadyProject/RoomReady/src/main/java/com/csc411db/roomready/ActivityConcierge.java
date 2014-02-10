package com.csc411db.roomready;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class ActivityConcierge extends Activity
{
    private static final int REQUEST_DIRECTIONS = 1;
    private String directionsResponse = new String ("didnt initialize yet");

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_concierge);

        createTopLeftGUI();
        createTopRightGUI();
        createBottomUI();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_concierge, menu);
        return true;
    }

    public void createTopLeftGUI()
    {
        ImageButton backButton = (ImageButton)findViewById(R.id.button_new_reservation);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    public void createTopRightGUI()
    {
        ImageButton infoButton = (ImageButton)findViewById(R.id.buttonInfo);
        infoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityConcierge.this, ActivityInfo.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
        infoButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                return true;
            }
        });

        ImageButton settingsButton = (ImageButton)findViewById(R.id.buttonSettingsMain);
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(ActivityConcierge.this, ActivityLogIn.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }

    public void createBottomUI()
    {
        Button directionsButton = (Button)findViewById(R.id.buttonConDirections);
        directionsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                Bundle extras = new Bundle();
                extras.putString("URL", "http://www.weather.com/weather/today/Hattiesburg+MS+USMS0152");
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        Button restaurantButton = (Button)findViewById(R.id.buttonConRestaurants);
        restaurantButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog restaurantListDialog = new Dialog(context);
                restaurantListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = restaurantListDialog.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                restaurantListDialog.setContentView(R.layout.dialog_restaraunt_options);
                restaurantListDialog.setTitle("Select Cuisine Type");

                Button italian = (Button)restaurantListDialog.findViewById(R.id.buttonFoodItalian);
                italian.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=italian&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button mexican = (Button)restaurantListDialog.findViewById(R.id.buttonFoodMexican);
                mexican.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=mexican&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button chinese = (Button)restaurantListDialog.findViewById(R.id.buttonFoodChinese);
                chinese.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=chinese&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button american = (Button)restaurantListDialog.findViewById(R.id.buttonFoodAmerican);
                american.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=american&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button indian = (Button)restaurantListDialog.findViewById(R.id.buttonFoodIndian);
                indian.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=indian&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button greek = (Button)restaurantListDialog.findViewById(R.id.buttonFoodGreek);
                greek.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=greek&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button barbeque = (Button)restaurantListDialog.findViewById(R.id.buttonFoodBarbeque);
                barbeque.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=barbeque&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button french = (Button)restaurantListDialog.findViewById(R.id.buttonFoodFrench);
                french.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=french&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button german = (Button)restaurantListDialog.findViewById(R.id.buttonFoodGerman);
                german.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=german&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button mediterranean = (Button)restaurantListDialog.findViewById(R.id.buttonFoodMedit);
                mediterranean.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=mediterranean&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button seafood = (Button)restaurantListDialog.findViewById(R.id.buttonFoodSeafood);
                seafood.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=seafood&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button cajun = (Button)restaurantListDialog.findViewById(R.id.buttonFoodCajun);
                cajun.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=cajun&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                Button steakHouse = (Button)restaurantListDialog.findViewById(R.id.buttonFoodSteakhouse);
                steakHouse.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://www.yelp.com/search?find_desc=cajun&find_loc=39401&ns=1");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        restaurantListDialog.dismiss();
                    }
                });

                restaurantListDialog.show();


            }
        });

        Button theaterButton = (Button)findViewById(R.id.buttonConTheaters);
        theaterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                Bundle extras = new Bundle();
                extras.putString("URL", "http://www.fandango.com/39401_movietimes");
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        Button localEntertainmentButton = (Button)findViewById(R.id.buttonConLocalEntertainment);
        localEntertainmentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                Bundle extras = new Bundle();
                extras.putString("URL", "http://www.hattiesburg.org/index.cfm/play/attractions/");
                i.putExtras(extras);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        Button localMapButton = (Button)findViewById(R.id.buttonConLocalMap);
        localMapButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Dialog localMapDirectory = new Dialog(context);
                localMapDirectory.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window dialogWindow = localMapDirectory.getWindow();
                dialogWindow.setBackgroundDrawableResource(R.drawable.grey_rounded_corners);
                localMapDirectory.setContentView(R.layout.dialog_local_maps);

                Button downtownMapButton = (Button)localMapDirectory.findViewById(R.id.buttonMapDowntown);
                downtownMapButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://government.hattiesburgms.com/images/stories/FedPrograms/GIS/maps/downtown_jpg.jpg");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                });

                Button publicTransitMapButton = (Button)localMapDirectory.findViewById(R.id.buttonMapPublicTransit);
                publicTransitMapButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://services.hattiesburgms.com/images/stories/FedPrograms/GIS/maps/transit_jpg.jpg");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                });

                Button histricDistrictMapButton = (Button)localMapDirectory.findViewById(R.id.buttonMapHistoric);
                histricDistrictMapButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://government.hattiesburgms.com/images/stories/FedPrograms/GIS/maps/historic_jpg.jpg");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                });

                Button schoolDistrictsButton = (Button)localMapDirectory.findViewById(R.id.buttonMapSchoolDistricts);
                schoolDistrictsButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://government.hattiesburgms.com/images/stories/FedPrograms/GIS/maps/schooldist_jpg.jpg");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                });

                Button USMMapButton = (Button)localMapDirectory.findViewById(R.id.buttonMapUSM);
                USMMapButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), ActivityWebView.class);
                        Bundle extras = new Bundle();
                        extras.putString("URL", "http://map.usm.edu");
                        i.putExtras(extras);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                });

                localMapDirectory.show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_DIRECTIONS  && resultCode==RESULT_OK)
        {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            directionsResponse =thingsYouSaid.get(0);
        }
    }
    
}
