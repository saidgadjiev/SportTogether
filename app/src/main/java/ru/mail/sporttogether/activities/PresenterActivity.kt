package ru.mail.sporttogether.activities

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.eventbus.PermissionGrantedMessage
import ru.mail.sporttogether.eventbus.PermissionMessage
import ru.mail.sporttogether.mvp.presenters.IPresenter
import ru.mail.sporttogether.mvp.views.IView

/**
 * Created by bagrusss on 30.09.16.
 *
 */
abstract class PresenterActivity<T : IPresenter> : AppCompatActivity(), IView {

    protected lateinit var presenter: T
    protected val injector = App.injector.useViewComponent()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    override fun showToast(@StringRes messageRes: Int, duration: Int) {
        Toast.makeText(this, messageRes, duration).show()

    }

    override fun showProgressDialog() {

    }

    override fun hideProgressDialog() {

    }

    override fun showSnackbar(message: String, duration: Int) {

    }

    override fun showSnackbar(@StringRes messageRes: Int, duration: Int) {

    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {
            finish()
            return true
        }
        if (item.itemId === R.id.post) {
            shareToSocial()
        }
        return super.onOptionsItemSelected(item)
    }

    fun shareToSocial() {
        val content = ShareLinkContent
                .Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .setContentTitle("Test post")
                .setContentDescription("Welcome to sport together")
                .build()

        ShareDialog.show(this, content)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        return true
    }

    fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msg: PermissionMessage) {
        requestPermissions(msg.permissionsList.toTypedArray(), msg.reqCode)
    }

    // сука, ебучий гугл, у вложенныех фрагментов не вызывается onRequestPermissionsResult
    // костыль, по пока что так
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var isEverythingAllowed = true
        for (result in grantResults) {
            isEverythingAllowed = isEverythingAllowed and (result == PackageManager.PERMISSION_GRANTED)
        }

        if (isEverythingAllowed) {
            presenter.onPermissionsGranted(requestCode)
            EventBus.getDefault().post(PermissionGrantedMessage(requestCode))
        } else {
            presenter.onPermissionNotGranted(requestCode)
        }
    }


}