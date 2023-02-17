package com.example.dillus.stockapp.AppAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dillus.stockapp.AppData.ClsProducto;
import com.example.dillus.stockapp.R;

import java.util.List;

public class ListaProductoAdapter extends  RecyclerView.Adapter<ListaProductoAdapter.ViewHolder>{

    List<ClsProducto> dataset;
    //OnInscripcionClickListener onInscripcionClickListener;
    Context context;

    public ListaProductoAdapter(List<ClsProducto> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /*private CardView cardView_Inscripcion;
        private ImageView imageView_itemInscripcion_Imagen;
        private TextView textView_itemInscripcion_Dia;
        private TextView textView_itemInscripcion_Hora;
        private TextView textView_itemInscripcion_Nombre;
        private ImageButton imageButton_itemInscripcion_More;*/

        public ViewHolder(final View itemView) {
            super(itemView);

            /*cardView_Inscripcion = itemView.findViewById(R.id.cardView_Inscripcion);
            imageView_itemInscripcion_Imagen = itemView.findViewById(R.id.imageView_itemInscripcion_Imagen);
            textView_itemInscripcion_Dia = itemView.findViewById(R.id.textView_itemInscripcion_Dia);
            textView_itemInscripcion_Hora = itemView.findViewById(R.id.textView_itemInscripcion_Hora);
            textView_itemInscripcion_Nombre = itemView.findViewById(R.id.textView_itemInscripcion_Nombre);
            imageButton_itemInscripcion_More = itemView.findViewById(R.id.imageButton_itemInscripcion_More);*/
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_producto_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

    }
}
