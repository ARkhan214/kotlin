package com.emranhss.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrgAdapter(private val orgList: List<Organization>) : RecyclerView.Adapter<OrgAdapter.OrgViewHolder>() {

    class OrgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivLogo: ImageView = view.findViewById(R.id.ivLogo)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrgViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_organization, parent, false)
        return OrgViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrgViewHolder, position: Int) {
        val org = orgList[position]
        holder.tvName.text = org.name
        holder.tvLocation.text = org.location
        holder.ivLogo.setImageResource(org.logoResId)

        holder.itemView.setOnClickListener {
            showEditDialog(holder.itemView.context, position) //Position Pas
        }
    }

    //-------------------
    private fun showEditDialog(context: android.content.Context, position: Int) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Edit Organization")

        val input = android.widget.EditText(context)
        input.setText(orgList[position].name) // current name
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, _ ->
            val newName = input.text.toString()

            // update list data(orgList a MutableList)
            val currentOrg = orgList[position]

            // Replace with new name ( data class 'val')
            (orgList as MutableList)[position] = currentOrg.copy(name = newName)

            // for adapter to awar change position
            notifyItemChanged(position)

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
    //-------------------

    override fun getItemCount(): Int = orgList.size
}