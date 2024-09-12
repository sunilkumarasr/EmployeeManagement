package com.royalitpark.employeemanagement.ui.documents

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bookiron.itpark.services.RetrofitClient.CMS_IMAGE_PATH
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.royalitpark.employeemanagement.models.EmpDocuments
import com.royalitpark.employeemanagement.R

class DocumentListAdapter(val documentsListActivity: DocumentsListActivity) :RecyclerView.Adapter<DocumentListAdapter.DocumentViewHolder>() {
    lateinit var documentsLIst:ArrayList<EmpDocuments>

    init{
        documentsLIst=ArrayList<EmpDocuments>()
    }
    class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_doc_title=view.findViewById<TextView>(R.id.txt_doc_title)
        val txt_doc_desc=view.findViewById<TextView>(R.id.txt_doc_desc)
        val img_doc_download=view.findViewById<ImageView>(R.id.img_doc_download)
        val img_doc=view.findViewById<ImageView>(R.id.img_doc)
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_document_item,parent,false)

        return DocumentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return documentsLIst.size
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {

        val empDoc=documentsLIst.get(position)
        holder.txt_doc_title.setText("${empDoc.displayName}")
        holder.txt_doc_desc.setText("${empDoc.documentName}")
        Glide.with(holder.img_doc.context).load(CMS_IMAGE_PATH+empDoc.documentFile)
            .apply(RequestOptions().placeholder(R.drawable.img_dummy_doc).error(R.drawable.img_dummy_doc))
            .into(holder.img_doc)
        holder.img_doc_download.setOnClickListener {
            Log.d(" empDoc.documentFile"," empDoc.documentFile ${ empDoc.documentFile}")
            empDoc.documentFile?.let {
                    it1 -> documentsListActivity.downloadImage(CMS_IMAGE_PATH+it1)
            }
        }
    }
    fun setDocList(documentsLIsts:ArrayList<EmpDocuments>)
    {
        documentsLIst.clear()
        documentsLIst.addAll(documentsLIsts)
        notifyDataSetChanged()
    }
}