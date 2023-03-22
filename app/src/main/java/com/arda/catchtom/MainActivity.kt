package com.arda.catchtom

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var score=0
    private var highScore=0
    private var imageArray=ArrayList<ImageView>()
    private var myHandler=Handler(Looper.getMainLooper())
    private var myRunnable:Runnable= Runnable {  }
    private lateinit  var mySharedPreferences:SharedPreferences
    private var myGetPreferences:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this@MainActivity,"Welcome to the game!!",Toast.LENGTH_SHORT).show()
        mySharedPreferences=this.getSharedPreferences("com.arda.catchtom", MODE_PRIVATE)
        myGetPreferences=mySharedPreferences.getInt("High Score",-1)
        if (myGetPreferences!=-1){
            highScoreText.text="High Score:$myGetPreferences"
            highScore=myGetPreferences!!
        }else
        {
            highScoreText.text="High Score:0"
        }



        // Secili gorseli gorunmez yapar.(imageView.visibility=View.INVISIBLE)
        imageArray.add(imageView)
        imageArray.add(imageView2)
        imageArray.add(imageView3)
        imageArray.add(imageView4)
        imageArray.add(imageView5)
        imageArray.add(imageView6)
        imageArray.add(imageView7)
        imageArray.add(imageView8)
        imageArray.add(imageView9)
        hideShowImages()

        //CountDown Timer(App acildiginda direk baslamasini istedigim icin timer'i onCreate fonksiyonunun altina yazdim
        object:CountDownTimer(15000,1000){
            override fun onTick(millisUntilFinished: Long) {
                timeText.text="Time:${millisUntilFinished/1000}"
            }
            override fun onFinish() {//Zaman bitince ne olacak onu gosterir.Oyun bitince yazacagimiz seyler burdadir.
                myHandler.removeCallbacks(myRunnable)//Benim runnablemi durdur diyorum.
                for (image in imageArray){
                    image.visibility=View.INVISIBLE//Butun resimleri tekrardan gorunmez hale getiriyorum.
                }


                //Alert
                val myAlert=AlertDialog.Builder(this@MainActivity)
                myAlert.setTitle("Game Over")
                myAlert.setMessage("Restart the Game?")
                myAlert.setPositiveButton("Yes",DialogInterface.OnClickListener { dialog, which ->
                    //Restart The Game.
                    if (score>highScore){
                        highScore=score
                        mySharedPreferences.edit().putInt("High Score",highScore).apply()

                        if(score>=highScore){
                            Toast.makeText(this@MainActivity,"Congratulations you beat your high score!",Toast.LENGTH_SHORT).show()
                        }

                        val myintent=intent //getIntent'ten gelir.
                        finish()//Aktiviteyi komple kapatirim.OnDestroy'u cagiririm.
                        startActivity(myintent)//Aktivite kendini yeniden baslatacaktir.

                    }else{
                        Toast.makeText(this@MainActivity, "You can do better!!", Toast.LENGTH_SHORT).show()
                        highScoreText.text="High Score:$highScore"
                        println("My high score is:$highScore")
                        val myintent=intent //getIntent'ten gelir.
                        finish()//Aktiviteyi komple kapatirim.OnDestroy'u cagiririm.
                        startActivity(myintent)//Aktivite kendini yeniden baslatacaktir.

                    }

                })
                myAlert.setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this@MainActivity, "Game Over Thank You!!", Toast.LENGTH_SHORT).show()
                    if (score>highScore){
                         highScore=score
                    mySharedPreferences.edit().putInt("High Score",highScore).apply()
                        highScoreText.text="High Score:$highScore"
                        if(score>=highScore){
                            Toast.makeText(this@MainActivity,"Congratulations you beat your high score!",Toast.LENGTH_SHORT).show()
                        }
                        println("My high score is:$highScore")
                    }else{
                        highScoreText.text="High Score:$highScore"
                        println("My high score is:$highScore")
                        Toast.makeText(this@MainActivity, "You can do better!!", Toast.LENGTH_SHORT).show()
                    }
                })
                myAlert.show()

            }
        }.start()
    }

    fun hideShowImages(){
        myRunnable=object:Runnable {
            override fun run() { // Bu fonksiyonda yaptigim hersey arka planda istedigim periyotta calistirilir.
                for (myimage in imageArray){
                    myimage.visibility=View.INVISIBLE
                }
                val random=Random // Random sinifini kullaniyorum.
                val randomIndex= random.nextInt(9)//random'i kullanarak olusturuyorum.[0-9) arasinda rasgele bir int olustur demektir.
                imageArray[randomIndex].visibility=View.VISIBLE // Arraylisteki imageleri teker teker random bir sekilde gorunur yapiyorum.
                myHandler.postDelayed(this,500)//Burada run fonksiyonunun kac saniye delay ile calisacagini ayarliyorum.
            }
        }
        myHandler.post(myRunnable)
    }

    fun increaseScore(view: View){
        score++
        scoreText.text="Score:$score"
    }




}