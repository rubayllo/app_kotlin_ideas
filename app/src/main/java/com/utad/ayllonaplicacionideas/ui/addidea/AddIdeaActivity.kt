package com.utad.ayllonaplicacionideas.ui.addidea

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.utad.ayllonaplicacionideas.databinding.ActivityAddIdeaBinding
import com.utad.ayllonaplicacionideas.model.room_data_base.MyAplicationRoom
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Idea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddIdeaActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityAddIdeaBinding
    private val binding: ActivityAddIdeaBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddIdeaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etNewIdeaName.doAfterTextChanged { checkData() }
        binding.etNewIdeaDescrip.doAfterTextChanged { checkData() }
        binding.rbLow.setOnClickListener { checkData() }
        binding.rbMiddle.setOnClickListener { checkData() }
        binding.rbHigh.setOnClickListener { checkData() }

        binding.btnSaveNewIdea.setOnClickListener { saveInDataBase() }

        binding.btnImgSelect.setOnClickListener {
            val externalStoragePermission: String = Manifest.permission.READ_EXTERNAL_STORAGE
            val permissionStatus =
                ContextCompat.checkSelfPermission(this, externalStoragePermission)

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                intent.type = "image/*"
                imageGalleryLauncher.launch(intent)
            } else {
                checkStatusExternalStoragePermission()
            }
        }
    }

    private fun checkData() {
        val name = binding.etNewIdeaName.text
        val descrip = binding.etNewIdeaDescrip.text
        val priority: Boolean =
            (binding.rbLow.isChecked || binding.rbMiddle.isChecked || binding.rbHigh.isChecked)

        binding.btnSaveNewIdea.isEnabled = (!name.isNullOrBlank() && !descrip.isNullOrBlank() && priority)
        if (binding.btnSaveNewIdea.isEnabled) {
            binding.btnSaveNewIdea.setBackgroundColor(Color.parseColor("#3F51B5"))
        } else {
            binding.btnSaveNewIdea.setBackgroundColor(Color.parseColor("#B9BBBC"))
        }
    }

    private fun saveInDataBase() {
        val name = binding.etNewIdeaName.text.toString()
        val descript = binding.etNewIdeaDescrip.text.toString()
        val priority: String? = when {
            binding.rbLow.isChecked -> binding.rbLow.text.toString()
            binding.rbMiddle.isChecked -> binding.rbMiddle.text.toString()
            binding.rbHigh.isChecked -> binding.rbHigh.text.toString()
            else -> {
                "Baja"
            }
        }
        if (ideaImage == null) { // En caso de null carga imagen por defecto
            ideaImage = binding.ivNewIdeaImage.drawToBitmap()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            //Como en la entity tenemos puesto en la primaryKey el atributo "autogenerate",
            //si pasamos un 0 al valor, se pondrá automáticamente un id válido
            val idea = Idea(0, name, descript, priority!!, "Pendiente", ideaImage)

            //Accedemos a la clase Application y le hacemos un cast al tipo MyAplicationRoom
            // que tiene nuestra base de datos
            (application as MyAplicationRoom)
                .room //Accedemos a la variable pública de nuestra base de datos
                .daoDataBase() //Accedemos al dao para poder interactuar con la base de datos
                .saveIdeaList(idea) //Llamamos al @Insert que guarda nuestra idea nueva

            //Pasamos al hilo principal para poder matar este fragment y volver a la pantalla anterior
            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }


    //PARA ABRIR GALERIA Y OBTENER IMAGEN
    private var ideaImage: Bitmap? = null
    private var imageGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selectedImageUri: Uri? = data.data
                    ideaImage = convertUriToBitmap(selectedImageUri)
                    binding.ivNewIdeaImage.setImageBitmap(ideaImage)
                } else {
                    showErrorMessageNoImage()
                }
            } else {
                showErrorMessageNoImage()
            }
        }

    private fun convertUriToBitmap(uri: Uri?): Bitmap? {
        try {
            //A partir del Uri de la imagen recibida, obtenemos el Bitmap
            val inputStream = contentResolver.openInputStream(uri!!)
            val image = BitmapFactory.decodeStream(inputStream)
            //Si es una imagen pequeña devolveremos el Bitmap
            if (image.byteCount <= 2500000) {
                return image
            } else {
                //En caso de no serlo, comprimiremos la imagen hasta que se pueda guardar en Room
                var compressedImage = image
                do {
                    var scaleWidth = compressedImage.width / 2
                    var scaleHeight = compressedImage.height / 2

                    compressedImage =
                        Bitmap.createScaledBitmap(image, scaleWidth, scaleHeight, true)
                } while (compressedImage.byteCount >= 2500000)
                //Cuando sea lo suficientemente pequeña, devolveremos la imagen comprimida
                return compressedImage
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun showErrorMessageNoImage() {
        Toast.makeText(this, "No has seleccionado ninguna imagen", Toast.LENGTH_SHORT).show()
    }


    //PARA LA PETICION Y GESTION DE PERMISOS

    private fun checkStatusExternalStoragePermission() {
        //Mediante Manifest.permission.NuestroPermiso
        // accedemos al permiso que queremos comprobar
        val externalStoragePermission: String = Manifest.permission.READ_EXTERNAL_STORAGE
        //El método ContextCompat.checkSelfPermission() nos devuelve el estado
        //del permiso que pasemos cómo parámetro. Lo guardamos en una variable
        val permissionStatus = ContextCompat.checkSelfPermission(this, externalStoragePermission)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            // Ya tenemos permiso, podemos realizar la acción que lo necesita.
            showGrantedPermissionMessage()
        } else {
            // Debemos comprobar si ya hemos pedido con anterioridad el permiso
            val shouldRequestPermission =
                shouldShowRequestPermissionRationale(externalStoragePermission)
            // De ser así, debemos explicar al usuario porqué es necesario el permiso
            if (shouldRequestPermission) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("El permiso es necesario")
                    .setMessage("Es necesario para poder cargar tu imagen de empleado y completar tu ficha")
                    .setPositiveButton("Okay") { dialog, which ->
                        //Si el usuario acepta, pedimos de nuevo los permisos
                        requestPermissionLauncher.launch(externalStoragePermission)
                    }
                    .setNegativeButton("Denegar") { dialog, which ->
                        Snackbar.make(
                            binding.root,
                            "El usuario no acepta el permiso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    .show()
            } else {
                requestPermissionLauncher.launch(externalStoragePermission)
            }
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //El permiso ha sido concedido, podemos realizar la acción que lo necesitaba
                Snackbar.make(
                    binding.root,
                    "El usuario nos concedió el permiso",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                //El permiso ha sido denegado, deberemos restringir el acceso a esta parte de la app
                Snackbar.make(binding.root, "El usuario lo denegó", Snackbar.LENGTH_SHORT).show()
            }
        }

    private fun showGrantedPermissionMessage() {
        Snackbar.make(
            binding.root,
            "El usuario había concedido el permiso previamente",
            Snackbar.LENGTH_SHORT
        ).show()
    }


}
