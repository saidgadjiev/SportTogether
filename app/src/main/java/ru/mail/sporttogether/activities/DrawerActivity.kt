package ru.mail.sporttogether.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import ru.mail.sporttogether.R

class DrawerActivity : AbstractActivity() {
    protected lateinit var drawer: Drawer;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var toolbar = Toolbar(this)
        var drawerBuilder: DrawerBuilder = DrawerBuilder().withActivity(this).withToolbar(toolbar)
        drawer = drawerBuilder.build()
        setContentView(R.layout.activity_drawer)
    }
}
