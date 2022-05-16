package cr.ac.menufragmentcurso.adapter

import android.R.color
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import cr.ac.menufragmentcurso.R
import cr.ac.menufragmentcurso.entity.Empleado
import java.io.ByteArrayOutputStream


class EmpleadoAdapter(context: Context, empleados: List<Empleado>) :
    ArrayAdapter<Empleado>(context, 0, empleados) {

    lateinit var img_avatar : ImageView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.empleado_item, parent, false)

        val nombre = rowView.findViewById<TextView>(R.id.empleado_nombre)
        val puesto = rowView.findViewById<TextView>(R.id.empleado_puesto)

        val empleado = getItem(position)

        nombre.setText(empleado?.nombre)
        puesto.setText(empleado?.puesto)

        img_avatar = rowView.findViewById(R.id.empleado_imagen)

        if(empleado?.avatar != ""){
            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }else{
            img_avatar.setImageResource(R.drawable.ic_launcher_foreground)
        }

        return rowView
    }

    private fun decodeImage (b64 : String): Bitmap {
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}