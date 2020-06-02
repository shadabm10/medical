package com.rootscare.ui.splash

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.rootscare.BR
import com.rootscare.R
import com.rootscare.databinding.ActivitySplashBinding
import com.rootscare.ui.base.BaseActivity
import com.rootscare.ui.home.HomeActivity
import com.rootscare.ui.login.LoginActivity
import java.io.IOException
import java.io.InputStream


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {
    private var activitySplashBinding: ActivitySplashBinding? = null
    private var splashViewModel: SplashViewModel? = null
    private val SPLASH_DISPLAY_LENGTH = 1000

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_splash

    override val viewModel: SplashViewModel
        get() {
            splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java!!)
            return splashViewModel as SplashViewModel
        }
    companion object {
        val TAG = SplashActivity::class.java.simpleName

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, SplashActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashViewModel!!.navigator = this
        activitySplashBinding = viewDataBinding
        try { // get input stream
            val ims: InputStream = assets.open("splash_bg_new.png")
            // load image as Drawable
            val d = Drawable.createFromStream(ims, null)
            // set image to ImageView
            activitySplashBinding?.imgSplash?.setImageDrawable(d)
            ims.close()
        } catch (ex: IOException) {
            return
        }

//


        Handler().postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            runOnUiThread {
                //     startActivity(IntroActivity.newIntent(this@SplashActivity))
//                if (splashViewModel?.appSharedPref?.isIntoPageOn!!) {
//                    startActivity(IntroActivity.newIntent(this@SplashActivity))
//                } else{
//
//                    //startActivity(LoginActivity.newIntent(this@SplashActivity))
//                    if (!splashViewModel?.appSharedPref?.userId.equals("") && splashViewModel?.appSharedPref?.userId!=null){
//                        if(splashViewModel?.appSharedPref?.userType!=null){
//                            if(splashViewModel?.appSharedPref?.userType.equals("subscriber")){
//                                startActivity(HomeActivity.newIntent(this@SplashActivity))
//                            }else if(splashViewModel?.appSharedPref?.userType.equals("Trainer")){
//                                startActivity(TrainerHomeActivity.newIntent(this@SplashActivity))
//                            }
//                        }else{
//                            startActivity(ActivityLogInOption.newIntent(this@SplashActivity))
//                        }
//
//
//                    } else{
//                        startActivity(ActivityLogInOption.newIntent(this@SplashActivity))
//                    }
//
//                }

                if(!splashViewModel?.appSharedPref?.isloginremember.equals("") && splashViewModel?.appSharedPref?.isloginremember!=null){
                    if(splashViewModel?.appSharedPref?.isloginremember.equals("true")){
                        startActivity(HomeActivity.newIntent(this@SplashActivity))
                        finish()
                    }else{
                        startActivity(LoginActivity.newIntent(this@SplashActivity))
                        finish()
                    }
                }
                else{
                    startActivity(LoginActivity.newIntent(this@SplashActivity))
                    finish()
                }


            }
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    override fun reloadPageAfterConnectedToInternet() {

    }
}
