package com.example.consejeroapp.utils


import android.graphics.Color
import android.support.v4.os.IResultReceiver._Parcel
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.consejeroapp.data.Consejo
import com.example.consejeroapp.databinding.ItemMainBinding
import com.google.android.material.checkbox.MaterialCheckBox.OnCheckedStateChangedListener

class ConsejoAdapter(private var dataSet: List<Consejo> = emptyList(),
                   private val onItemClickListener: (Int) -> Unit,
                   private val onDeleteClickListener: (Int) -> Unit,
                   private val onCheckBoxChangedListener: (Int) -> Unit,
                   private val onEditClickListener: (Int) -> Unit ) : RecyclerView.Adapter<TareaViewHolder>(){

    private var highlightText: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        //return SuperheroViewHolder(ItemSuperheroBinding.bind(parent.rootView))
        return TareaViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener{
            //onItemClickListener(position)
            onItemClickListener(holder.adapterPosition) // es la posicion más real, se calcula dinamicamente!!
        }
        holder.binding.deleteImageButton.setOnClickListener(){
            onDeleteClickListener(holder.adapterPosition)
        }


        holder.binding.realizadoCheckBox.setOnCheckedChangeListener {chk , _ ->
            if (chk.isPressed)
                onCheckBoxChangedListener(holder.adapterPosition)

        }

        holder.binding.editImageButton.setOnClickListener{
            onEditClickListener(holder.adapterPosition)
        }


    }

    fun updateData(dataSet: List<Consejo> ) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    /*fun updateData(dataSet: List<SuperheroResponse>, highlight: String? ){
        this.highlightText = highlight
        this.dataSet = dataSet
        notifyDataSetChanged()
    }*/


}

class TareaViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun render(tarea: Consejo){
        binding.nameTextView.text = tarea.texto
        binding.realizadoCheckBox.isChecked = tarea.leida
    }

    // Subraya el texto que coincide con la busqueda
    fun highlight(text: String) {
        try {
            val highlighted = binding.nameTextView.text.toString().highlight(text)
            //textView.text = highlighted
        } catch (e: Exception) { }
    }
    val imageUrl = "https://www.example.com/imagen.jpg"


    //ejemplo de extensión para el metodo String, que hace resaltar el texto buscado
    fun String.highlight(text: String) : SpannableString {
        val str = SpannableString(this)
        val startIndex = str.indexOf(text, 0, true)
        str.setSpan(BackgroundColorSpan(Color.rgb(244,144,255)), startIndex, startIndex + text.length, 0)
        return str
    }

}