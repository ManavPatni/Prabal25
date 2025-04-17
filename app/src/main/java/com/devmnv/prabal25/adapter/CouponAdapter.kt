package com.devmnv.prabal25.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devmnv.prabal25.R
import com.devmnv.prabal25.model.Coupon
import com.devmnv.prabal25.sharedPrefs.AuthSharedPref
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import org.json.JSONObject
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

class CouponAdapter(
    private var coupons: MutableList<Coupon>
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupons, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]
        holder.couponCode.text = coupon.couponCode
        // Reset barcode image and visibility based on flip state
        if (coupon.isFlipped) {
            holder.frontView.visibility = View.GONE
            holder.backView.visibility = View.VISIBLE
            holder.backView.rotationY = 0f
            // Barcode will be set during flip if not already set
        } else {
            holder.frontView.visibility = View.VISIBLE
            holder.backView.visibility = View.GONE
            holder.frontView.rotationY = 0f
            holder.barcodeImage.setImageBitmap(null) // Clear barcode when showing front
        }
        holder.itemView.setOnClickListener {
            holder.flipCard(coupon)
        }
    }

    override fun getItemCount(): Int = coupons.size

    fun updateData(newList: List<Coupon>) {
        coupons.clear()
        coupons.addAll(newList)
        notifyDataSetChanged()
    }

    class CouponViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frontView: View = view.findViewById(R.id.front_view)
        val backView: View = view.findViewById(R.id.back_view)
        val couponCode: TextView = view.findViewById(R.id.tv_couponCode)
        val barcodeImage: ImageView = view.findViewById(R.id.iv_qr_code)

        init {
            frontView.cameraDistance = 8000f
            backView.cameraDistance = 8000f
        }

        fun flipCard(coupon: Coupon) {
            if (coupon.isFlipped) {
                // Flip back to front (opposite direction)
                val flipOut = ObjectAnimator.ofFloat(backView, "rotationY", 0f, -90f) // Rotate left
                flipOut.duration = 300
                flipOut.interpolator = AccelerateInterpolator()
                flipOut.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        backView.visibility = View.GONE
                        frontView.rotationY = 90f // Start from right
                        frontView.visibility = View.VISIBLE
                        val flipIn = ObjectAnimator.ofFloat(frontView, "rotationY", 90f, 0f)
                        flipIn.duration = 300
                        flipIn.interpolator = DecelerateInterpolator()
                        flipIn.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                coupon.isFlipped = false
                                barcodeImage.setImageBitmap(null) // Clear barcode
                            }
                        })
                        flipIn.start()
                    }
                })
                flipOut.start()
            } else {
                // Flip to back (original direction)
                val flipOut = ObjectAnimator.ofFloat(frontView, "rotationY", 0f, 90f) // Rotate right
                flipOut.duration = 300
                flipOut.interpolator = AccelerateInterpolator()
                flipOut.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        frontView.visibility = View.GONE
                        backView.rotationY = -90f // Start from left
                        backView.visibility = View.VISIBLE
                        // Generate barcode before flipping to back
                        val barcodeBitmap = generateBarcode(coupon.couponCode, AuthSharedPref(itemView.context).uid())
                        barcodeImage.setImageBitmap(barcodeBitmap)
                        val flipIn = ObjectAnimator.ofFloat(backView, "rotationY", -90f, 0f)
                        flipIn.duration = 300
                        flipIn.interpolator = DecelerateInterpolator()
                        flipIn.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                coupon.isFlipped = true
                            }
                        })
                        flipIn.start()
                    }
                })
                flipOut.start()
            }
        }

        private fun generateBarcode(couponCode: String, participantId: String?): Bitmap {
            val width = 800 // Wider for barcode readability
            val height = 200 // Shorter height for linear barcode
            val jsonData = JSONObject().apply {
                put("couponCode", couponCode)
                put("participantId", participantId ?: 0) // Fallback to 0 if null
            }.toString()
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(
                jsonData,
                BarcodeFormat.CODE_128,
                width,
                height
            )
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.TRANSPARENT)
                }
            }
            return bitmap
        }
    }
}