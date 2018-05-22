package com.gerforce.traelc.zhsrhj


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView


//ViewHolder中的属性使用关键字lateinit延迟初始化
class CollectionListAdapter(private val context: Context, private val CollectionList: List<AssignmentTemplate>) : BaseAdapter() {
    override fun getCount(): Int = CollectionList.size

    override fun getItem(position: Int): Any = CollectionList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_view, null)
            holder = ViewHolder()


            holder.tbAddress = view.findViewById(R.id.tbAddress)
            holder.tbDistrict = view.findViewById(R.id.tbDistrict)
            holder.tbModeName = view.findViewById(R.id.tbModeName)
            holder.tbName = view.findViewById(R.id.tbName)
            holder.tbStartTime = view.findViewById(R.id.tbStartTime)
            holder.tbStreet = view.findViewById(R.id.tbStreet)
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