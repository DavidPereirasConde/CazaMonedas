package com.es.cazamonedas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Elementos de juego
    private TextView scoreLabel, startLabel;
    private ImageView box, dos, uno, black;

    //tamaños
    private int screenWidth;
    private int frameHeight;
    private int frameWidth;
    private int boxSize;

    //posiciones
    private float boxX;
    private float boxY;

    private float dosX, dosY;
    private float unoX, unoY;
    private float blackX, blackY;

    //velocidad
    private int unoSpeed, dosSpeed, blackSpeed;

    //dinero
    private int dinero;

    //temporizadores
    private Timer timer = new Timer();
    private Handler handler = new Handler();

    //Estado
    private boolean start_flg = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        scoreLabel = findViewById(R.id.scoreLabel);

        box = findViewById(R.id.box);
        dos = findViewById(R.id.dos);
        uno = findViewById(R.id.uno);
        black = findViewById(R.id.black);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        int screenHeight = size.y;

        dosSpeed = Math.round(screenWidth / 60.0f); // 768 / 60 = 12.8 => 13
        unoSpeed = Math.round(screenWidth / 36.0f); // 768 / 36 = 21.333 => 21
        blackSpeed = Math.round(screenWidth / 45.0f); // 768 / 45 = 17.06... => 17


        //Posiciones iniciales negativas ya que una vez cuando se analiza la posición se ubica de forma aleatoria
        dos.setX(-80.0f);
        dos.setY(-2000);
        uno.setX(-80.0f);
        uno.setY(-2000);
        black.setX(-80.0f);
        black.setY(-2000);

        FrameLayout frameLayout = findViewById(R.id.frame);
        frameHeight = frameLayout.getHeight();
        frameWidth = frameLayout.getWidth();



        //MOVIMIENTO DEL PERSONAJE
        box.setOnTouchListener(new View.OnTouchListener() {
            private boolean start_flg = true;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        boxX = view.getX() - event.getRawX();

                        box.setX(boxX);
                        box.setY(frameLayout.getHeight()-140);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate().x(event.getRawX() + boxX).setDuration(0).start();
                        box.setX(boxX);
                        box.setY(frameLayout.getHeight()-140);
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });


        //MOVIMIENTO DE LAS MONEDAS

        if(start_flg = true){

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mueveMonedas();
                        }
                    });
                }
            }, 0, 20);


        }


        }

    @SuppressLint("StringFormatInvalid")
    private void mueveMonedas() {

        if(chocaMoneda2(box, dos))
        {
            dos.setX(-80.0f);
            dos.setY(-3000);
            dosY = 3000;
            dinero += 2;
            this.scoreLabel.setText("Euros: "+dinero);
            //scoreLabel.setText(getString(R.string.score, dinero));
        }
        if(chocaMoneda1(box, uno))
        {
            uno.setX(-80.0f);
            uno.setY(-3000);
            unoY = 3000;
            dinero += 1;
            this.scoreLabel.setText("Euros: "+dinero);
            //scoreLabel.setText(getString(R.string.score, dinero));
        }
        if(chocaBomba(box, black))
        {
            dos.setX(-80.0f);
            dos.setY(-2000);
            uno.setX(-80.0f);
            uno.setY(-2000);
            black.setX(-80.0f);
            black.setY(-2000);
            //scoreLabel.setText(getString(R.string.score, dinero));
            this.scoreLabel.setText("Euros: "+dinero);
            timer.cancel();
            timer = null;
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("Dinero:", dinero);
            startActivity(intent);
        }
        //doseuros
        dosY = dosY + 20;
        if(dosY > 1800){
            dosY = -80.0f;
            dosX = (float)Math.floor(Math.random()* 1080 - 0)
;        }
        dos.setX(dosX);
        dos.setY(dosY);


        //unEuro

        unoY = unoY + 12;

        if(unoY > 1800){
            unoY = -80.0f;
            unoX = (float)Math.floor(Math.random()* 1080 - 0)
            ;        }
        uno.setX(unoX);
        uno.setY(unoY);


        //bomba

        blackY =blackY + 40;

        if(blackY > 1800){
            blackY = -80.0f;
            blackX = (float)Math.floor(Math.random()* 1080 - 0)
            ;        }
        black.setX(blackX);
        black.setY(blackY);

    }

    private boolean chocaMoneda2(ImageView box, ImageView dos) {
        Rect BallRect = new Rect();
        box.getHitRect(BallRect);
        Rect NetRect = new Rect();
        dos.getHitRect(NetRect);


        return BallRect.intersect(NetRect);
    }
    private boolean chocaMoneda1(ImageView box, ImageView uno) {
        Rect BallRect = new Rect();
        box.getHitRect(BallRect);
        Rect NetRect = new Rect();
        uno.getHitRect(NetRect);

        return BallRect.intersect(NetRect);
    }
    private boolean chocaBomba(ImageView box, ImageView bomba) {
        Rect BallRect = new Rect();
        box.getHitRect(BallRect);
        Rect NetRect = new Rect();
        bomba.getHitRect(NetRect);
        return BallRect.intersect(NetRect);
    }

}