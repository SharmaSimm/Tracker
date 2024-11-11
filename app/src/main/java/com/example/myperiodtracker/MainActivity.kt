package com.example.periodtracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myperiodtracker.databinding.ActivityMainBinding
import com.example.periodtracker.databinding.ActivityMainBinding
import com.example.periodtracker.databinding.ListItemPhotoBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentPhotoPath: String? = null
    private val photoList = mutableListOf<String>()
    private lateinit var photoAdapter: PhotoAdapter

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView for displaying captured photos
        photoAdapter = PhotoAdapter(photoList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = photoAdapter

        // Capture photo button click listener
        binding.btnCapturePhoto.setOnClickListener {
            capturePhoto()
        }
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = createImageFile()
        if (photoFile != null) {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",  // Make sure to set this in your manifest
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(null)
        return try {
            val imageFile = File.createTempFile(
                "JPEG_${timeStamp}_",  // Prefix
                ".jpg",  // Suffix
                storageDir   // Directory
            )
            currentPhotoPath = imageFile.absolutePath
            imageFile
        } catch (ex: IOException) {
            Log.e("MainActivity", "Error creating image file: ${ex.message}")
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            currentPhotoPath?.let { path ->
                // Add the photo path to the list and update the RecyclerView
                photoList.add(path)
                photoAdapter.notifyDataSetChanged()
            }
        }
    }

    // Adapter for RecyclerView to display captured photos
    class PhotoAdapter(private val photoList: List<String>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val binding = ListItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PhotoViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            val photoPath = photoList[position]
            // You can use Glide or Picasso to load images into ImageView here
            holder.binding.imgPhoto.setImageURI(Uri.parse(photoPath))
        }

        override fun getItemCount(): Int = photoList.size

        class PhotoViewHolder(val binding: ListItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)
    }
}
