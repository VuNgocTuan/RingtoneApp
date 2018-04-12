package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import ringtone.studio.vuetu.ringtoneapp.R
import ringtone.studio.vuetu.ringtoneapp.repository.model.Ringtone

/**
 * Created by vungoctuan on 4/12/18.
 */
class NewRingtoneAdapter : RecyclerView.Adapter<NewRingtoneAdapter.ViewHolder>() {
    var mRingtoneList = mutableListOf<Ringtone>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextName.text = mRingtoneList[position].name
        holder.mTextAuthor.text = mRingtoneList[position].author
    }

    override fun getItemCount(): Int {
        return mRingtoneList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.item_ringtone, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImageBackground = view.findViewById<ImageView>(R.id.image_item_background)
        val mButtonPlay = view.findViewById<ImageButton>(R.id.button_play)
        val mTextName = view.findViewById<TextView>(R.id.text_ringtone_name)
        val mTextAuthor = view.findViewById<TextView>(R.id.text_ringtone_author)
        val mButtonDownload = view.findViewById<ImageButton>(R.id.button_download)
    }

    fun setData(list: MutableList<Ringtone>) {
        mRingtoneList.clear()
        mRingtoneList.addAll(list)
        notifyDataSetChanged()
    }

    fun addRingtone(ringtone: Ringtone) {
        mRingtoneList.add(mRingtoneList.size, ringtone)
        notifyItemInserted(mRingtoneList.size - 1)
    }
}