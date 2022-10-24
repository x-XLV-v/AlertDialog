package com.example.dialogs

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dialogs.databinding.ActivityLevel1Binding
import com.example.dialogs.entities.AvailableVolumeValues
import kotlin.properties.Delegates.notNull

class DialogsLevel1Activity: AppCompatActivity() {

    private lateinit var binding: ActivityLevel1Binding

    private var volume by notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alertDialogDefaultButton.setOnClickListener {
            showAlertDialog()
        }

        binding.alertDialogSingleChoiceButton.setOnClickListener {
            showSingleChoiceAlertDialog()
        }

        volume = savedInstanceState?.getInt(KEY_VOLUME) ?: 50

        updateUi()
    }

    //----------------------------Alert Dialog Default----------------------------//

    private fun showAlertDialog() {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> showToast(R.string.uninstall_confirmed)
                DialogInterface.BUTTON_NEGATIVE -> showToast(R.string.uninstall_rejected)
                DialogInterface.BUTTON_NEUTRAL -> {
                    showToast(R.string.uninstall_ignored)
                }
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setIcon(R.mipmap.ic_launcher_round)
            .setTitle(R.string.default_alert_title)
            .setMessage(R.string.default_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .setOnCancelListener {
                showToast(R.string.dialog_cancelled)
            }
            .setOnDismissListener {
                Log.d(TAG, "Dialog dismissed")
            }
            .create()

        dialog.show()
    }

    private fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

    //----------------------------Alert Dialog Single Choice----------------------------//

    private fun showSingleChoiceAlertDialog() {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val volumeTextItems = volumeItems.values
            .map { getString(R.string.volume_description, it) }
            .toTypedArray()

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.volume_setup)
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex) { dialog, which ->
                volume = volumeItems.values[which]
                updateUi()
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }


    private fun updateUi() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
    }

    companion object {
        @JvmStatic private val KEY_VOLUME = "KEY_VOLUME"
    }
}