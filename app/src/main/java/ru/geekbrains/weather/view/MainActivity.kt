package ru.geekbrains.weather.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import ru.geekbrains.weather.R

class MainActivity : AppCompatActivity() {

    private val receiver = MainBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO) //Добавил для себя, чтобы не выключать НР на телефоне
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitAllowingStateLoss()
        }

    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}