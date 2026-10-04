package com.example.taskflow

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

/** Gives every screen the same toolbar behaviour. */
fun AppCompatActivity.attachToolbar(toolbar: MaterialToolbar, titleRes: Int, showBackButton: Boolean) {
    setSupportActionBar(toolbar)
    supportActionBar?.setTitle(titleRes)
    supportActionBar?.setDisplayHomeAsUpEnabled(showBackButton)
    toolbar.setNavigationOnClickListener { finish() }
}