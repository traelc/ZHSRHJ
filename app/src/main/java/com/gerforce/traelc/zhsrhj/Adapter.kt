package com.gerforce.traelc.zhsrhj


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import org.jetbrains.anko.find


class CollectionListAdapter(private val context: Context, private val CollectionList: List<AssignmentTemplate>) : BaseAdapter() {
    override fun getCount(): Int = CollectionList.size

    override fun getItem(position: Int): Any = CollectionList[position]

    override fun getItemId(position: Int): Long = CollectionList[position].AssignmentID.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_view_template, null)
            holder = ViewHolder()

            holder.tbAddress = view.find(R.id.tbAddress)
            holder.tbDistrict = view.find(R.id.tbDistrict)
            holder.tbModeName = view.find(R.id.tbModeName)
            holder.tbName = view.find(R.id.tbName)
            holder.tbStartTime = view.find(R.id.tbStartTime)
            holder.tbStreet = view.find(R.id.tbStreet)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        val planet = CollectionList[position]

        holder.tbAddress.text = planet.Address
        holder.tbDistrict.text = planet.DistrictName
        holder.tbModeName.text = planet.ModeName
        holder.tbName.text = planet.Name
        holder.tbStartTime.text = planet.AssignmentDate
        holder.tbStreet.text = planet.StreetName
        return view!!
    }

    inner class ViewHolder {
        lateinit var tbAddress: TextView
        lateinit var tbDistrict: TextView
        lateinit var tbModeName: TextView
        lateinit var tbName: TextView
        lateinit var tbStartTime: TextView
        lateinit var tbStreet: TextView
    }
}