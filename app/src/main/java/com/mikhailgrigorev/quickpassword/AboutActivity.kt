package com.mikhailgrigorev.quickpassword

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mikhailgrigorev.quickpassword.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private val _keyTHEME = "themePreference"
    private val _preferenceFile = "quickPassPreference"

    private var condition = true
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // Set Theme

        val pref = getSharedPreferences(_preferenceFile, Context.MODE_PRIVATE)
        when(pref.getString(_keyTHEME, "none")){
            "yes" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "no" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "none", "default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "battery" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }

        super.onCreate(savedInstanceState)

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.setDecorFitsSystemWindows(false)
                }
                else{
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }

        // Finish app after some time

        val handler = Handler(Looper.getMainLooper())
        val r = Runnable {
            if(condition) {
                condition=false
                val intent = Intent(this, LoginAfterSplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val time: Long =  100000
        val sharedPref = getSharedPreferences(_preferenceFile, Context.MODE_PRIVATE)
        val lockTime = sharedPref.getString("appLockTime", "6")
        if(lockTime != null) {
            if (lockTime != "0")
                handler.postDelayed(r, time * lockTime.toLong())
        }
        else
            handler.postDelayed(r, time*6L)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cardRadius = sharedPref.getString("cardRadius", "none")
        if(cardRadius != null)
            if(cardRadius != "none") {
                binding.gitHub.radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cardRadius.toFloat(), resources.displayMetrics)
                binding.social.radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cardRadius.toFloat(), resources.displayMetrics)
            }


        // Exit from activity

        binding.back.setOnClickListener {
            finish()
        }

        // My link to Telegram
        binding.telegram.setOnClickListener {
            condition=false
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/grigorevmp"))
            startActivity(i)
        }

        // My link to VK
        binding.vkontakte.setOnClickListener {
            condition=false
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/grigorevmp"))
            startActivity(i)
        }

        // My link to GitHub
        binding.gitHub.setOnClickListener {
            condition=false
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/grigorevmp/QuickPass-Mobile-Password-manager/"))
            startActivity(i)
        }

        // Direct Mail sending
        binding.mail.setOnClickListener {
            sendEmail()
        }

    }

    private fun sendEmail() {
        condition=false
        val recipient = "16112000m@gmai.com"
        val subject = "Quick password app"
        val message = "Hello, Mikhail \n"

        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)
        try {
            startActivity(Intent.createChooser(mIntent, getString(R.string.chooseEmail)))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }
}