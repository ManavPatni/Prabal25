package com.devmnv.prabal25.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devmnv.prabal25.R
import com.devmnv.prabal25.model.Coupon

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
    }

    override fun getItemCount(): Int = coupons.size

    fun updateData(newList: List<Coupon>) {
        coupons.clear()
        coupons.addAll(newList)
        notifyDataSetChanged()
    }

    fun getUpdatedParticipants(): List<Coupon> = coupons

    class CouponViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val couponCode: TextView = view.findViewById(R.id.tv_couponCode)
    }
}
