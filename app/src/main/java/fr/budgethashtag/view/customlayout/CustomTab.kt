package fr.budgethashtag.view.customlayout

import android.content.Context
import android.support.annotation.Nullable
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout

import android.util.AttributeSet
import android.util.Log
import android.view.View
import fr.budgethashtag.R

class CustomTab : TabLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setupWithViewPager(@Nullable viewPager: ViewPager?) {
        super.setupWithViewPager(viewPager)
        Log.i("TAG", "setupWithViewPager")
        if (viewPager!!.adapter == null) {
            return
        }
        createTabIcons()
    }

    private fun createTabIcons() {
        Log.i("TAG", "createTabIcons")
        val tabOne = LayoutInflater.from(context).inflate(R.layout.custom_tab, null) as LinearLayout
        (tabOne.findViewById<View>(R.id.text_title) as TextView).text = "tab 1"
        this.getTabAt(0)!!.customView = tabOne

        val tabTwo = LayoutInflater.from(context).inflate(R.layout.custom_tab, null) as LinearLayout
        (tabOne.findViewById<View>(R.id.text_title) as TextView).text = "tab 2"
        this.getTabAt(1)!!.customView = tabTwo

        val tabThree = LayoutInflater.from(context).inflate(R.layout.custom_tab, null) as LinearLayout
        (tabOne.findViewById<View>(R.id.text_title) as TextView).text = "tab 3"
        this.getTabAt(2)!!.customView = tabThree
    }
}