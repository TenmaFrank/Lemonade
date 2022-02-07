package com.tenmafrank.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.tenmafrank.lemonade.R.drawable.lemon_drink
import com.tenmafrank.lemonade.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    private val SELECT = "select"
    private val SQUEEZE = "squeeze"
    private val DRINK = "drink"
    private val RESTART = "restart"
    private var lemonadeState = "select"
    private var lemonSize = -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    //private var lemonImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }

        lemonSize = lemonTree.pick()

        binding.lemonadeImageButton.setOnClickListener {
            if (lemonadeState != SQUEEZE){
                lemonadeState = clickOnLemon(lemonadeState)
            }
        }
        binding.lemonadeImageButton.setOnLongClickListener {
            if (lemonadeState == SQUEEZE){
                showSnackbar()
            }else{
                false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    private fun clickOnLemon(lemonState: String): String{
        when(lemonState){
            SELECT -> {
                setViewElements(R.string.lemon_squeeze,R.drawable.lemon_squeeze)
                return SQUEEZE
            }
            SQUEEZE -> {
                setViewElements(R.string.lemon_drink,R.drawable.lemon_drink)
                return DRINK
            }
            DRINK -> {
                setViewElements(R.string.lemon_empty_glass,R.drawable.lemon_restart)
                return RESTART
            }
            RESTART -> {
                setViewElements(R.string.lemon_select,R.drawable.lemon_tree)
                return SELECT
            }
            else ->{
                setViewElements(R.string.lemon_select,R.drawable.lemon_tree)
                return SELECT
            }
        }
    }

    private fun setViewElements(textID: Int,drawableID: Int) {
        binding.lemonadeTextView.text = getText(textID)
        binding.lemonadeImageButton.background = ResourcesCompat.getDrawable(this.resources, drawableID, null)
    }

    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount ++)
        Snackbar.make(
            findViewById(R.id.constraint_layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()

        if (squeezeCount == lemonSize)
        {
            lemonadeState = clickOnLemon(SQUEEZE)
        }
        return true
    }
}