package com.devmnv.prabal25.fragment

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.devmnv.prabal25.R
import com.devmnv.prabal25.auth.AuthManager
import com.devmnv.prabal25.databinding.FragmentHomeBinding
import com.devmnv.prabal25.databinding.FragmentPassBinding
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PassFragment : Fragment() {

    private var _binding: FragmentPassBinding? = null
    private val binding get() = _binding!!
    private lateinit var qrBitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPassBinding.inflate(inflater, container, false)

        binding.tvTeamName.text = AuthSharedPref(requireContext()).teamName()

        // Generate QR code in background thread
        CoroutineScope(Dispatchers.IO).launch {
            qrBitmap = generateQRCode()
            withContext(Dispatchers.Main) {
                Glide.with(requireContext())
                    .load(qrBitmap)
                    .into(binding.ivQr)
            }
        }

        return binding.root
    }

    // Function to generate the QR code bitmap
    private fun generateQRCode(): Bitmap {
        val width = 400
        val height = 400
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            AuthManager.getTeamId(requireContext()),
            BarcodeFormat.QR_CODE,
            width,
            height
        )

        // Use ARGB_8888 to support transparency
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Your desired QR color
        val qrColor = Color.parseColor("#8D470C")

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) qrColor else Color.TRANSPARENT)
            }
        }

        return bitmap
    }

}