package com.nilsnahooy.roomdatabasedemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.nilsnahooy.roomdatabasedemo.databinding.ItemRowBinding

class ItemAdapter(private val items: ArrayList<EmployeeEntity>,
                  private val updateListener:(id:Int) -> Unit,
                  private val deleteListener:(id:Int) -> Unit)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(b: ItemRowBinding): RecyclerView.ViewHolder(b.root){
        val clMain = b.clMain
        val tvName = b.tvName
        val tvMail = b.tvMail
        val ivEdit = b.ivEdit
        val ivDelete = b.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c =  holder.itemView.context
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvMail.text = item.email
        holder.clMain.setBackgroundColor(
            if(position%2==0){
                getColor(c, R.color.white)
            }else{
                getColor(c,R.color.grey)
        })

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}