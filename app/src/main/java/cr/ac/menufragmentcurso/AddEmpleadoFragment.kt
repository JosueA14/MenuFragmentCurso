package cr.ac.menufragmentcurso

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.repository.EmpleadoRepository
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "empleado"

private const val PICK_IMAGE = 100

/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    //private var argum1: String? = null
    lateinit var img_avatar: ImageView
    private var empleado: Empleado? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_add_empleado, container, false)

        val newID = view.findViewById<EditText>(R.id.addIDEmpleado)
        val newNombre = view.findViewById<EditText>(R.id.addNombreEmpleado)
        val newPuesto = view.findViewById<EditText>(R.id.addPuestoEmpleado)
        val newDepartamento = view.findViewById<EditText>(R.id.addDepartamentoEmpleado)
        img_avatar = view.findViewById(R.id.addImagenEmpleado)

        if (empleado?.avatar != "") {
            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }

        val builder = AlertDialog.Builder(context)

        view.findViewById<Button>(R.id.buttonAgregarEmpleado).setOnClickListener {

            builder.setMessage("¿Seguro que desea agregarlo?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->

                    var idEmpleado: Int = EmpleadoRepository.instance.datos().size + 1
                    var avatarEmpleado: String =
                        encodeImage(img_avatar.drawable.toBitmap()).toString()

                    var empleado: Empleado = Empleado(
                        idEmpleado, newID?.text.toString(),
                        newNombre?.text.toString(),
                        newPuesto?.text.toString(),
                        newDepartamento?.text.toString(), avatarEmpleado
                    )

                    EmpleadoRepository.instance.save(empleado)


                    val fragmento: Fragment = CameraFragment.newInstance("Cámara")
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                    activity?.setTitle("Cámara")

                }
                .setNegativeButton("No") { dialog, id ->
                }
            val alert = builder.create()
            alert.show()
        }
        img_avatar.setOnClickListener {
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            var imageUri = data?.data

            Picasso.get()
                .load(imageUri)
                .resize(120, 120)
                .centerCrop()
                .into(img_avatar)
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n", "")
    }

    private fun decodeImage(b64: String): Bitmap {
        val imageBytes = Base64.decode(b64, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: Empleado) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}