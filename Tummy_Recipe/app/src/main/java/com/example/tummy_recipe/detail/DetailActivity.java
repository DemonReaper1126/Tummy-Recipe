package com.example.tummy_recipe.detail;

import static com.example.tummy_recipe.main.MainActivity.EXTRA_DETAIL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tummy_recipe.R;
import com.example.tummy_recipe.Utils;
import com.example.tummy_recipe.model.Meals;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailView{

    /* INITIALIZE VARIABLES AND REFERENCES */

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.mealThumb)
    ImageView mealThumb;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.category)
    TextView category;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.country)
    TextView country;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.instructions)
    TextView instructions;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ingredient)
    TextView ingredients;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.measure)
    TextView measure;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.youtube)
    TextView youtube;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.share)
    TextView share;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.source)
    TextView source;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.extendBtn)
    ExtendedFloatingActionButton extendBtn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tts_play)
    FloatingActionButton tts_play;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tts_pause)
    FloatingActionButton tts_stop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.stt_Button)
    FloatingActionButton stt_Button;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tts_play_text)
    TextView tts_play_text;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tts_pause_text)
    TextView tts_pause_text;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.stt_text)
    TextView stt_text;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.stt_text_result)
    TextView stt_text_result;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.shadowView)
    View shadowView;

    boolean extendBtnStatus = false;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* BIND ButterKnife TO THIS ACTIVITY TO BE ABLE TO USE IT'S FUNCTIONS */
        ButterKnife.bind(this);

        /* FUNCTION CALLS */
        setupActionBar();
        textToSpeechConfig();
        extendBtnFunc();
        playStopFunc();
        speechToTextFunc();
        checkTextChanged();

        /* CREATE BUNDLE  */
        Bundle extras = getIntent().getExtras();
        String mealName = extras.getString(EXTRA_DETAIL);
        String test = extras.getString("MEAL");


        /* INITIALIZE DetailPresenter */
        DetailPresenter presenter = new DetailPresenter(this);

        /* AS THERE ARE 2 ACTIVITIES THAT MIGHT COME TO THIS ACTIVITY (SeenActivity, CategoryFragment) */
        /* THIS CHECKS IF EITHER ONE IS EMPTY */
        /* IF EMPTY MEANING IT CAME FROM ANOTHER ACTIVITY */
        if (TextUtils.isEmpty(mealName))
            presenter.getMealById(test);
        if (TextUtils.isEmpty(test))
            presenter.getMealById(mealName);

    }

    /* IF BACK BUTTON PRESSED, WILL STOP THE TTS PLAY */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        textToSpeech.stop();
    }


    /* textToSpeech CONFIGURATION */
    private void textToSpeechConfig() {

        /* INITIALIZE textToSpeech */
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {

            /* IF INITIALIZATION SUCCESS */
            if(i == TextToSpeech.SUCCESS){

                /* CREATE INT THAT PASSES textToSpeech LANGUAGE ENGLISH */
                int language = textToSpeech.setLanguage(Locale.ENGLISH);

                /* IF language HAS MISSING DATA OR NOT SUPPORTED ON DEVICE */
                if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(DetailActivity.this, "Language is not supported!", Toast.LENGTH_SHORT).show();
                }
                /* IF language HAS DATA SUPPORTED ON DEVICE */
                else{
                    Toast.makeText(DetailActivity.this, "Language is supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /* FUNCTION TO EXTEND AND SHRINK ExtendedFloatingActionButton */
    /* CALLED IN onCreate */
    @SuppressLint("ClickableViewAccessibility")
    private void extendBtnFunc() {

        /* INITIALLY SHRINK AND SET ALL TEXT AND BUTTON VISIBILITY TO GONE */
        extendBtn.shrink();
        tts_play.setVisibility(View.GONE);
        tts_stop.setVisibility(View.GONE);
        stt_Button.setVisibility(View.GONE);
        tts_play_text.setVisibility(View.GONE);
        tts_pause_text.setVisibility(View.GONE);
        stt_text.setVisibility(View.GONE);
        stt_text_result.setVisibility(View.GONE);

        /* IF CLICK ON extendBtn */
        extendBtn.setOnClickListener(view -> {

            /* IF extendBtnStatus IS FALSE */
            if (!extendBtnStatus) {

                /* SHOW 3 BUTTONS */
                tts_play.show();
                tts_stop.show();
                stt_Button.show();

                /* SET 4 TEXT GUIDE TO VISIBLE */
                tts_play_text.setVisibility(View.VISIBLE);
                tts_pause_text.setVisibility(View.VISIBLE);
                stt_text.setVisibility(View.VISIBLE);
                stt_text_result.setVisibility(View.VISIBLE);

                /* SET shadowView TO VISIBLE */
                shadowView.setVisibility(View.VISIBLE);

                /* SET extendBtn TO extend() */
                extendBtn.extend();

                /* CHANGE TO SHRINK ICON OF extendBtn */
                extendBtn.setIconResource(R.drawable.ic_shrink);

                /* SET extendBtnStatus TO TRUE */
                extendBtnStatus = true;

            }

            /* IF extendBtnStatus IS true */
            else{
                /* HIDE 3 BUTTONS */
                tts_play.hide();
                tts_stop.hide();
                stt_Button.hide();

                /* SET 4 TEXT GUIDE TO GONE */
                tts_play_text.setVisibility(View.GONE);
                tts_pause_text.setVisibility(View.GONE);
                stt_text.setVisibility(View.GONE);
                stt_text_result.setVisibility(View.GONE);

                /* SET shadowView TO GONE */
                shadowView.setVisibility(View.GONE);

                /* SET extendBtn TO extend() */
                extendBtn.shrink();

                /* CHANGE TO EXPAND ICON OF extendBtn */
                extendBtn.setIconResource(R.drawable.ic_expand_add);

                /* SET extendBtnStatus TO FALSE */
                extendBtnStatus = false;
            }

            /* IF CLICK ON SHADOW VIEW WILL CLOSE shadowView */
            shadowView.setOnTouchListener((@SuppressLint("ClickableViewAccessibility") View view1,
                                           @SuppressLint("ClickableViewAccessibility") MotionEvent motionEvent) -> {
                tts_play.hide();
                tts_stop.hide();
                stt_Button.hide();
                tts_play_text.setVisibility(View.GONE);
                tts_pause_text.setVisibility(View.GONE);
                stt_text.setVisibility(View.GONE);
                stt_text_result.setVisibility(View.GONE);
                shadowView.setVisibility(View.GONE);
                extendBtn.shrink();
                extendBtn.setIconResource(R.drawable.ic_expand_add);
                extendBtnStatus = false;
                return true;
            });
        });
    }

    /* FUNCTION FOR PLAY AND STOP */
    private void playStopFunc() {

        /* PLAY BUTTON ON CLICK LISTENER */
        tts_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* CREATE STRING data TO GET TEXT FROM instructions */
                String data = instructions.getText().toString();
                Toast.makeText(DetailActivity.this, "PLAY", Toast.LENGTH_SHORT).show();

                textToSpeech.setSpeechRate(0.5f);
                /* RUN TEXT TO SPEECH */
                textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);

                /* PAUSE BUTTON */
                /* IS REQUIRED TO BE INSIDE PLAY BUTTON BECAUSE THEY SHARE THE SAME STRING data */
                tts_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                        /* STOP THE TEXT TO SPEECH */
                        textToSpeech.stop();
                    }
                });
            }
        });
    }

    /* FUNCTION FOR SPEECH TO TEXT */
    private void speechToTextFunc(){
        stt_Button.setOnClickListener(view -> {
            /*  */
            textToSpeech.stop();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            /* SETTING THE LANGUAGE OF SPEECH TO TEXT RECOGNITION */
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

            /* START intent WHEREBY GETTING THE RESULT TO BE USED IN onActivityResult FUNCTION BELOW */
            startActivityForResult(intent, 10);

            /* PAUSE BUTTON */
            /* IS REQUIRED TO BE ABLE TO PAUSE WHILE PLAYING */
            tts_stop.setOnClickListener(view1 -> {
                Toast.makeText(DetailActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                textToSpeech.stop();
            });
        });
    }

    /* GETTING RESULT FROM LINE 261 startActivityForResult */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* GETTING ACTIVITY RESULT BACK FROM INTENT BY INPUT OF REQUEST CODE */
        if (requestCode == 10) {
            /* CHECK IF OPERATION SUCCEEDED AND NOT NULL */
            if (resultCode == RESULT_OK && data != null) {
                /* CREATE ARRAY LIST TO GET THE SPEECH DATA */
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                stt_text_result.setText(result.get(0));
            }
        }

    }
    
    /* CHECK IF stt_text_result CHANGED */
    private void checkTextChanged(){
        stt_text_result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /* AFTER sst_text_result CHANGE, DO... */
            @Override
            public void afterTextChanged(Editable editable) {

                /* CREATE STRING TO GET INSTRUCTIONS TEXT */
                String instructionsText = instructions.getText().toString();

                /* CREATE EMPTY STRING ARRAY  */
                String[] splitText;

                /* IN SOME RECIPES INSTRUCTIONS TEXT, ALREADY INCLUDE "STEP" */
                /* THUS, IF IT ALREADY HAS "STEP", SPLIT THEM BY "STEP" */
                if(instructionsText.contains("STEP")){
                    splitText = instructionsText.split("STEP");

                    /* STT ALWAYS TAKES play step one INSTEAD OF play step 1 */
                    /* THIS CODE IS TO CATER FOR THAT SITUATION */
                    if (stt_text_result.getText().toString().equals("play step one")) {
                        textToSpeech.setSpeechRate(0.5f);
                        textToSpeech.speak("Step " + 1 + ", " + splitText[1].substring(2),
                                TextToSpeech.QUEUE_FLUSH,
                                null);
                    }

                    /* LOOP THOUGH ENTIRE SPLITTEXT AND PLAY ACCORDING TO STEP(i) */
                    for (int i = 1; i <= splitText.length; i++) {
                        if (stt_text_result.getText().toString().equals("play step " + i)) {
                            textToSpeech.setSpeechRate(0.5f);
                            /* SINCE splitText ALREADY SPLIT ACCORDING TO "STEP", IT DOES NOT HAVE IT ANYMORE */
                            /* THUS HAVING TO MANUALLY INCLUDE IT */
                            textToSpeech.speak("Step " + i + ", " + splitText[i].substring(2),
                                    TextToSpeech.QUEUE_FLUSH,
                                    null);
                        }
                    }
                }
                /* IF INSTRUCTIONS NOT HAVING STEP */
                /* SPLIT STRING ACCORDING TO "." */
                else {
                    // Split the instructions text based on "." into splitText[]
                    splitText = instructionsText.split("\\.");

                    /* STT ALWAYS TAKES play step one INSTEAD OF play step 1 */
                    /* THIS CODE IS TO CATER FOR THAT SITUATION */
                    if (stt_text_result.getText().toString().equals("play step one")) {
                        textToSpeech.setSpeechRate(0.5f);
                        textToSpeech.speak(splitText[0], TextToSpeech.QUEUE_FLUSH, null);
                    }

                    /* LOOP THOUGH ENTIRE SPLITTEXT AND PLAY ACCORDING TO STEP(i) */
                    for (int i = 1; i <= splitText.length; i++) {
                        if (stt_text_result.getText().toString().equals("play step " + i)) {
                            textToSpeech.setSpeechRate(0.5f);
                            textToSpeech.speak(splitText[i - 1], TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }

                /* PLAY ALL IF USER SAYS "PLAY" */
                if (stt_text_result.getText().toString().equals("play")){
                    String data2 = instructions.getText().toString();

                    textToSpeech.setSpeechRate(0.5f);
                    textToSpeech.speak(data2, TextToSpeech.QUEUE_FLUSH, null);

                    /* PAUSE BUTTON */
                    /* IS REQUIRED TO BE ABLE TO PAUSE WHILE PLAYING  */
                    tts_stop.setOnClickListener(view -> {
                        Toast.makeText(DetailActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                        textToSpeech.stop();
                    });
                }
            }
        });
    }

    /* SETUP TOOLBAR FUNCTION */
    private void setupActionBar() {
        setSupportActionBar(toolbar);

        /* SETUP COLLAPSING TOOLBAR LAYOUT */
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.orange));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        /* IF TOOLBAR NOT NULL, DISPLAY A BACK BUTTON */
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /* SETUP ITEMS IN TOOLBAR */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* WHEN CLICK ON BACK BUTTON, CALL onBackPressed() */
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* CREATE showLoading FUNCTION */
    /* SET extendBtn FALSE WHILE LOADING */
    /* THIS IS TO PREVENT TTS OR STT BEFORE PAGE IS LOADED */
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        extendBtn.setEnabled(false);
    }

    /* CREATE hideLoading FUNCTION */
    /* SET extendBtn TRUE WHEN DONE LOADING */
    /* THIS ENSURES TTS OR STT FUNCTION ENABLES ONLY WHEN PAGE IS LOADED */
    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        extendBtn.setEnabled(true);
    }

    /* setMeals FUNCTION */
    @Override
    public void setMeals(Meals.Meal meal) {

        /* GET FROM meal(MODEL) MEAL THUMBNAIL INTO VARIABLE mealThumb */
        Picasso.get().load(meal.getStrMealThumb()).into(mealThumb);

        /* SET TITLE OF TOOLBAR WITH meal(MODEL) getStrMeal() */
        collapsingToolbarLayout.setTitle(meal.getStrMeal());

        /* SET TEXT OF CATEGORY FROM meal(MODEL) getStrCategory() */
        category.setText(meal.getStrCategory());

        /* SET TEXT OF COUNTRY FROM meal(MODEL) getStrArea() */
        country.setText(meal.getStrArea());

        /* SETUP INSTRUCTIONS */
        /* CREATE STRING TO GET INSTRUCTIONS */
        String instructionsMeal = meal.getStrInstructions();

        /* CREATE STRING ARRAY  */
        String[] splitText;

        /* IF INSTRUCTIONS ALREADY HAVING STEP */
        /* CAN DIRECTLY SET SAME TEXT */
        if (instructionsMeal.contains("STEP")){
            instructions.setText(instructionsMeal);
        }

        /* IF INSTRUCTIONS NOT HAVING STEP */
        /* ADD STEP (i) ON EVERY SENTENCE AND ENDING WITH "." */
        else {
            /* SPLIT instructionsMeal TEXT, BASED ON ".", INTO splitText[] */
            splitText = instructionsMeal.split("\\.");

            /* CREATE StringBuilder TO BE USED TO STORE MODIFIED splitText IN FOR LOOP BELOW */
            StringBuilder stringBuilder = new StringBuilder();

            /* LOOP THROUGH ALL OF splitText[] AND APPEND TO stringBuilder */
            for (int i = 1; i < splitText.length; i++) {
                stringBuilder.append("Step ").append(i).append(", ").append(splitText[i]).append(".").append("\n");
            }

            /* SET THE INSTRUCTIONS TEXT WITH stringBuilder */
            instructions.setText(stringBuilder);
        }
        setupActionBar();

        /* CHANGING ALL NULL FIELDS GET FROM mealDB TO EMPTY STRING */
        /* IF FIELD IS NULL WILL CRASH */
        /* UNABLE TO CHANGE TO LOOP SINCE THESE ARE BUILT IN FUNCTION FROM theMealDB */
        if (meal.getStrIngredient1() == null)
            meal.setStrIngredient1("");
        if (meal.getStrIngredient2() == null)
            meal.setStrIngredient2("");
        if (meal.getStrIngredient3() == null)
            meal.setStrIngredient3("");
        if (meal.getStrIngredient4() == null)
            meal.setStrIngredient4("");
        if (meal.getStrIngredient5() == null)
            meal.setStrIngredient5("");
        if (meal.getStrIngredient6() == null)
            meal.setStrIngredient6("");
        if (meal.getStrIngredient7() == null)
            meal.setStrIngredient7("");
        if (meal.getStrIngredient8() == null)
            meal.setStrIngredient8("");
        if (meal.getStrIngredient9() == null)
            meal.setStrIngredient9("");
        if (meal.getStrIngredient10() == null)
            meal.setStrIngredient10("");
        if (meal.getStrIngredient11() == null)
            meal.setStrIngredient11("");
        if (meal.getStrIngredient12() == null)
            meal.setStrIngredient12("");
        if (meal.getStrIngredient13() == null)
            meal.setStrIngredient13("");
        if (meal.getStrIngredient14() == null)
            meal.setStrIngredient14("");
        if (meal.getStrIngredient15() == null)
            meal.setStrIngredient15("");
        if (meal.getStrIngredient16() == null)
            meal.setStrIngredient16("");
        if (meal.getStrIngredient17() == null)
            meal.setStrIngredient17("");
        if (meal.getStrIngredient18() == null)
            meal.setStrIngredient18("");
        if (meal.getStrIngredient19() == null)
            meal.setStrIngredient19("");
        if (meal.getStrIngredient20() == null)
            meal.setStrIngredient20("");


        if (meal.getStrMeasure1() == null)
            meal.setStrMeasure1("");
        if (meal.getStrMeasure2() == null)
            meal.setStrMeasure2("");
        if (meal.getStrMeasure3() == null)
            meal.setStrMeasure3("");
        if (meal.getStrMeasure4() == null)
            meal.setStrMeasure4("");
        if (meal.getStrMeasure5() == null)
            meal.setStrMeasure5("");
        if (meal.getStrMeasure6() == null)
            meal.setStrMeasure6("");
        if (meal.getStrMeasure7() == null)
            meal.setStrMeasure7("");
        if (meal.getStrMeasure8() == null)
            meal.setStrMeasure8("");
        if (meal.getStrMeasure9() == null)
            meal.setStrMeasure9("");
        if (meal.getStrMeasure10() == null)
            meal.setStrMeasure10("");
        if (meal.getStrMeasure11() == null)
            meal.setStrMeasure11("");
        if (meal.getStrMeasure12() == null)
            meal.setStrMeasure12("");
        if (meal.getStrMeasure13() == null)
            meal.setStrMeasure13("");
        if (meal.getStrMeasure14() == null)
            meal.setStrMeasure14("");
        if (meal.getStrMeasure15() == null)
            meal.setStrMeasure15("");
        if (meal.getStrMeasure16() == null)
            meal.setStrMeasure16("");
        if (meal.getStrMeasure17() == null)
            meal.setStrMeasure17("");
        if (meal.getStrMeasure18() == null)
            meal.setStrMeasure18("");
        if (meal.getStrMeasure19() == null)
            meal.setStrMeasure19("");
        if (meal.getStrMeasure20() == null)
            meal.setStrMeasure20("");

        if (meal.getStrSource() == null)
            meal.setStrSource("");

        /* FORMATTING INGREDIENTS SECTION */
        /* \u2022 IS CREATING POINT DOT */
        if (!meal.getStrIngredient1().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient1());
        }
        if (!meal.getStrIngredient2().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient2());
        }
        if (!meal.getStrIngredient3().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient3());
        }
        if (!meal.getStrIngredient4().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient4());
        }
        if (!meal.getStrIngredient5().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient5());
        }
        if (!meal.getStrIngredient6().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient6());
        }
        if (!meal.getStrIngredient7().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient7());
        }
        if (!meal.getStrIngredient8().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient8());
        }
        if (!meal.getStrIngredient9().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient9());
        }
        if (!meal.getStrIngredient10().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient10());
        }
        if (!meal.getStrIngredient11().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient11());
        }
        if (!meal.getStrIngredient12().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient12());
        }
        if (!meal.getStrIngredient13().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient13());
        }
        if (!meal.getStrIngredient14().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient14());
        }
        if (!meal.getStrIngredient15().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient15());
        }
        if (!meal.getStrIngredient16().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient16());
        }
        if (!meal.getStrIngredient17().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient17());
        }
        if (!meal.getStrIngredient18().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient18());
        }
        if (!meal.getStrIngredient19().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient19());
        }
        if (!meal.getStrIngredient20().isEmpty()) {
            ingredients.append("\n \u2022 " + meal.getStrIngredient20());
        }

        /* FORMATTING MEASURE */
        if (!meal.getStrMeasure1().isEmpty() && !Character.isWhitespace(meal.getStrMeasure1().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure1());
        }
        if (!meal.getStrMeasure2().isEmpty() && !Character.isWhitespace(meal.getStrMeasure2().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure2());
        }
        if (!meal.getStrMeasure3().isEmpty() && !Character.isWhitespace(meal.getStrMeasure3().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure3());
        }
        if (!meal.getStrMeasure4().isEmpty() && !Character.isWhitespace(meal.getStrMeasure4().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure4());
        }
        if (!meal.getStrMeasure5().isEmpty() && !Character.isWhitespace(meal.getStrMeasure5().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure5());
        }
        if (!meal.getStrMeasure6().isEmpty() && !Character.isWhitespace(meal.getStrMeasure6().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure6());
        }
        if (!meal.getStrMeasure7().isEmpty() && !Character.isWhitespace(meal.getStrMeasure7().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure7());
        }
        if (!meal.getStrMeasure8().isEmpty() && !Character.isWhitespace(meal.getStrMeasure8().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure8());
        }
        if (!meal.getStrMeasure9().isEmpty() && !Character.isWhitespace(meal.getStrMeasure9().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure9());
        }
        if (!meal.getStrMeasure10().isEmpty() && !Character.isWhitespace(meal.getStrMeasure10().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure10());
        }
        if (!meal.getStrMeasure11().isEmpty() && !Character.isWhitespace(meal.getStrMeasure11().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure11());
        }
        if (!meal.getStrMeasure12().isEmpty() && !Character.isWhitespace(meal.getStrMeasure12().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure12());
        }
        if (!meal.getStrMeasure13().isEmpty() && !Character.isWhitespace(meal.getStrMeasure13().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure13());
        }
        if (!meal.getStrMeasure14().isEmpty() && !Character.isWhitespace(meal.getStrMeasure14().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure14());
        }
        if (!meal.getStrMeasure15().isEmpty() && !Character.isWhitespace(meal.getStrMeasure15().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure15());
        }
        if (!meal.getStrMeasure16().isEmpty() && !Character.isWhitespace(meal.getStrMeasure16().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure16());
        }
        if (!meal.getStrMeasure17().isEmpty() && !Character.isWhitespace(meal.getStrMeasure17().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure17());
        }
        if (!meal.getStrMeasure18().isEmpty() && !Character.isWhitespace(meal.getStrMeasure18().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure18());
        }
        if (!meal.getStrMeasure19().isEmpty() && !Character.isWhitespace(meal.getStrMeasure19().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure19());
        }
        if (!meal.getStrMeasure20().isEmpty() && !Character.isWhitespace(meal.getStrMeasure20().charAt(0))) {
            measure.append("\n : " + meal.getStrMeasure20());
        }

        /* SET YOUTUBE ONCLICK */
        youtube.setOnClickListener(v -> {
            /* IF getStrYoutube() FROM meal(MODEL) IS NOT EMPTY */
            if (!meal.getStrYoutube().isEmpty()) {

                /* CREATE INTENT TO GO TO URL */
                Intent intentYoutube = new Intent(Intent.ACTION_VIEW);
                intentYoutube.setData(Uri.parse(meal.getStrYoutube()));
                startActivity(intentYoutube);
            }

            /* IF ITS EMPTY */
            else{
                Toast.makeText(this, "No youtube link found!", Toast.LENGTH_SHORT).show();
            }
        });

        /* SET SHARE ONCLICK */
        share.setOnClickListener(v -> {

            /* CREATE INTENT TO SEND URL LINK TO SHARE */
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, meal.getStrSource());
            startActivity(Intent.createChooser(intent, "Share using"));

        });

        /* SET SOURCE ONCLICK */
        source.setOnClickListener(v -> {

            /* IF getStrSource() FROM meal(MODEL) IS NOT EMPTY */
            if (!meal.getStrSource().isEmpty()) {

                /* CREATE INTENT TO OPEN WEBSITE OF SOURCED RECIPE */
                Intent intentSource = new Intent(Intent.ACTION_VIEW);
                intentSource.setData(Uri.parse(meal.getStrSource()));
                startActivity(intentSource);
            }

            /* IF NO SOURCE LINK FOUND */
            else {
                Toast.makeText(this, "No source link found!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* CREATE FUNCTION IF ERROR ON LOAD, SHOWS A DIALOG MESSAGE ABOUT THE ERROR */
    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Error:" ,message);
    }
}