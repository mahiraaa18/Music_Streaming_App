import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.content.pm.PackageManager
import np.com.bimalkafle.musicstream.R

class ProfilePictureActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private lateinit var btnChangeProfile: Button

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_GALLERY_PICK = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilepicture_activity)

        imgProfile = findViewById(R.id.imgProfile)
        btnChangeProfile = findViewById(R.id.btnChangeProfile)

        btnChangeProfile.setOnClickListener {
            showImagePickerDialog()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Foto Profil")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "Tidak ada aplikasi kamera yang tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imgProfile.setImageBitmap(imageBitmap)
                    Toast.makeText(this, "Foto Profil Berhasil Diubah", Toast.LENGTH_SHORT).show()
                }
                REQUEST_GALLERY_PICK -> {
                    val selectedImage = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    imgProfile.setImageBitmap(bitmap)
                    Toast.makeText(this, "Foto Profil Berhasil Diubah", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
