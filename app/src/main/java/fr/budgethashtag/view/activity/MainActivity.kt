package fr.budgethashtag.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import fr.budgethashtag.R
import fr.budgethashtag.databinding.ActivityMainBinding
import fr.budgethashtag.viewmodel.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var mainToolbar: Toolbar

    public override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = MainActivityViewModel(this)
        binding.viewModel = viewModel

        mainToolbar = binding.toolbarMain as Toolbar
        setSupportActionBar(mainToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        viewModel.onCreate(savedInstanceState)
    }

}
