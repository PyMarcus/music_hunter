package br.com.ifmg.musichunter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.ifmg.musichunter.databinding.ActivitySongItemBinding
import br.com.ifmg.musichunter.model.MusicModel

class MusicItemAdapter(private var songs: List<MusicModel>): RecyclerView.Adapter<MusicItemAdapter.MusicItemHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MusicItemAdapter.MusicItemHolder {
        val binding: ActivitySongItemBinding = ActivitySongItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicItemHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicItemAdapter.MusicItemHolder, position: Int) {
        holder.bind(this.songs[position])
    }

    override fun getItemCount(): Int {
        return this.songs.size
    }

    class MusicItemHolder(private var binding: ActivitySongItemBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bind(get: MusicModel){
            binding.trackname.text = get.trackName
            binding.collectionname.text = get.collectionName
            binding.releasedate.text = get.releaseDate
        }
    }
}